package stallum.controller;

import static br.com.caelum.vraptor.view.Results.json;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.xml.sax.SAXException;

import stallum.dao.ApontamentoDao;
import stallum.dao.CentroCustoDao;
import stallum.dao.DadosGeraisDao;
import stallum.dao.EmpresaDao;
import stallum.dao.FuncaoDao;
import stallum.dao.FuncionarioDao;
import stallum.dao.ObraDao;
import stallum.dao.SindicatoDao;
import stallum.dto.FiltroApontamento;
import stallum.dto.FiltroCentroCusto;
import stallum.dto.FiltroEmpresa;
import stallum.dto.FiltroFuncao;
import stallum.dto.FiltroFuncionario;
import stallum.dto.FiltroObra;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoFuncionario;
import stallum.dto.Paginacao;
import stallum.enums.Estado;
import stallum.enums.MotivoFalta;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusFuncionario;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.infra.XmlWriter;
import stallum.modelo.Apontamento;
import stallum.modelo.DadosGerais;
import stallum.modelo.Empresa;
import stallum.modelo.Funcao;
import stallum.modelo.Funcionario;
import stallum.modelo.Obra;
import stallum.modelo.Ponto;
import stallum.relatorio.FuncionarioRelatorio;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.mysql.jdbc.StringUtils;

@Resource
public class FuncionariosController extends AbstractController {

	private Logger log = Logger.getLogger(FuncionariosController.class);
	
	private final FuncionarioDao dao;
	private final ApontamentoDao apontDao;
	private final EmpresaDao empresaDao;
	private final SindicatoDao sindicatoDao;
	private final FuncaoDao funcaoDao;
	private final DadosGeraisDao dadosGeraisDao;
	private final ObraDao obraDao;
	private final CentroCustoDao centroCustoDao;
	
	public FuncionariosController(FuncionarioDao dao, EmpresaDao empresaDao, FuncaoDao funcaoDao, ObraDao obraDao,
			DadosGeraisDao dadosGeraisDao, CentroCustoDao centroCustoDao, ApontamentoDao apontDao, SindicatoDao sindicatoDao,
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.empresaDao = empresaDao;
		this.apontDao = apontDao;
		this.funcaoDao = funcaoDao;
		this.dadosGeraisDao = dadosGeraisDao;
		this.obraDao = obraDao;
		this.centroCustoDao = centroCustoDao;	
		this.sindicatoDao = sindicatoDao;
	}

	@Get @Path({"/funcionarios/novo", "/funcionarios/{funcionario.id}/editar"})
	public Funcionario formFuncionario(Funcionario funcionario) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (funcionario == null)
				funcionario = new Funcionario();
			else if (funcionario.getId() != null && funcionario.getVersao() == null)
				funcionario = consultarPorId(funcionario.getId());
			
			result.include("empresas", empresaDao.filtrar(FiltroEmpresa.newInstance(-1)));
			result.include("funcoes", funcaoDao.filtrar(FiltroFuncao.newInstance(-1)));
			result.include("centrosCusto", centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1)));
			result.include("status", StatusFuncionario.values());
			result.include("estados", Estado.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formFuncionario.falha.carregar"));
			result.redirectTo("/funcionarios/listar");
		}
		
		return funcionario;
	}
	
	@Put @Post @Path("/funcionarios")
	public void salvar(final Funcionario funcionario) {
		
		Transaction tx = dao.getSession().beginTransaction();
		validator.checking(new Validations() {{
			that(preenchido(funcionario.getNome()), "funcionario.nome", "obrigatorio");
			if (preenchido(funcionario.getNome()) && funcionario.getId() == null)
				that(!dao.existeNome(funcionario.getNome()), "funcionario.nome", "jaCadastrado");
			that(preenchido(funcionario.getEmpresa().getId()), "funcionario.empresa", "obrigatoria");
			that(preenchido(funcionario.getFuncao().getId()), "funcionario.funcao", "obrigatoria");
			that(preenchido(funcionario.getCtps()), "funcionario.ctps", "obrigatorio");
			that(preenchido(funcionario.getSerieCtps()), "funcionario.serieCtps", "obrigatoria");
			that(preenchido(funcionario.getDataAdmissao()), "funcionario.dataAdmissao", "obrigatoria");
			that(preenchido(funcionario.getRg()), "funcionario.rg", "obrigatorio");
			that(preenchido(funcionario.getOrgaoRg()), "funcionario.orgaoRg", "obrigatorio");
			that(preenchido(funcionario.getDataRg()), "funcionario.dataRg", "obrigatoria");
			that(preenchido(funcionario.getNomePai()), "funcionario.nomePai", "obrigatorio");
			that(preenchido(funcionario.getNomeMae()), "funcionario.nomeMae", "obrigatorio");
			that(preenchido(funcionario.getCpf()), "funcionario.cpf", "obrigatorio");
			that(cpfCnpjValido(funcionario.getCpf()), "funcionario.cpf", "invalido");
			if (preenchido(funcionario.getCpf()) && cpfCnpjValido(funcionario.getCpf()) && !funcionario.getCpf().equals("000.000.000-00"))
				that(!dao.existeCpf(funcionario), "funcionario.cpf", "jaCadastrado");
			that(preenchido(funcionario.getDataNascimento()), "funcionario.dataNascimento", "obrigatoria");
			that(preenchido(funcionario.getPisPasep()), "funcionario.pisPasep", "obrigatoria");
			if (preenchido(funcionario.getEmail()))
				that(emailValido(funcionario.getEmail()), "funcionario.email", "invalido");
			that(preenchido(funcionario.getEndereco().getLogradouro()), "endereco.logradouro", "obrigatorio");
			that(preenchido(funcionario.getEndereco().getNumero()), "endereco.numero", "obrigatorio");
			that(preenchido(funcionario.getEndereco().getCidade()), "endereco.cidade", "obrigatorio");
			that(preenchido(funcionario.getEndereco().getEstado()) && !funcionario.getEndereco().getEstado().equalsIgnoreCase("XX"), "endereco.estado", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formFuncionario(funcionario);
		
		
		try {
			
			boolean novo = true;
			
			funcionario.setEmpresa(empresaDao.buscarPorId(funcionario.getEmpresa().getId()));
			
			if (funcionario.getCentroCusto() != null)
				funcionario.setCentroCusto(centroCustoDao.buscarPorId(funcionario.getCentroCusto().getId()));
			
			// Calculo do anuenio
			long anos = Util.diferencaAnos(funcionario.getDataAdmissao(), new Date());
			funcionario.setAnuenioSalario(BigDecimal.valueOf(anos).multiply(buscarDadosGerais().getPercAnuenio()));
			
			if (funcionario.getId() == null) {
				
				funcionario.setFuncao(funcaoDao.buscarPorId(funcionario.getFuncao().getId()));
				funcionario.setSalarioBase(funcionario.getFuncao().getSalario());
				funcionario.setSindicato(funcionario.getFuncao().getSindicato());
				funcionario.setStatus(StatusFuncionario.ATIVO);				
				
				dao.incluir(funcionario);			
				
			} else {
				dao.alterar(funcionario);
				novo = false;
			}
			
			if (funcionario.getEndereco() != null && preenchido(funcionario.getEndereco().getLogradouro())) {
				
				if (funcionario.getEndereco().getId() == null)
					dao.incluir(funcionario.getEndereco());
				else
					dao.alterar(funcionario.getEndereco());
				
			} else {
				funcionario.setEndereco(null);
			}
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formFuncionario.sucesso"));
			
			if (novo)
				result.redirectTo("/funcionarios/novo");
			else
				result.redirectTo("/funcionarios/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formFuncionario.falha.salvar"));
			result.redirectTo(this).formFuncionario(funcionario);
		}
		
	}
		
	@Get @Path({"/funcionarios/parametrosJson/{idEmpresa}/{idFuncao}", "/funcionarios/parametrosJson/{idEmpresa}/{idFuncao}/{idFuncionario}"})
	public void buscaJson(Long idEmpresa, Long idFuncao, Long idFuncionario) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		try {
			
			Empresa empresa = empresaDao.consultarPorId(idEmpresa);
			Funcao funcao = funcaoDao.consultarPorId(idFuncao);
			DadosGerais dadosGerais = dadosGeraisDao.buscarVigente();
			
			Funcionario funcionario = new Funcionario();
			
			if (idFuncionario != null)
				funcionario = dao.consultarPorId(idFuncionario);
			
			funcionario.setEmpresa(empresa);
			funcionario.setFuncao(funcao);
			funcionario.setSalarioBase(funcao.getSalario());
			funcionario.setSindicato(funcao.getSindicato());
			funcionario.setEncargo1(dadosGerais.getEncargo(funcionario, DadosGerais.ENCARGO1));
			funcionario.setEncargo2(dadosGerais.getEncargo(funcionario, DadosGerais.ENCARGO2));

			result.use(json()).withoutRoot().from(funcionario).serialize();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("funcionario.falha.recuperar"));
			result.redirectTo("/funcionarios/listar");
		}
		
	}
	
	@Get @Path({"/funcionarios/{idRemover}/remover", "/funcionarios/{idRecuperar}/recuperar"})
	public void removerRecuperar(Long idRemover, Long idRecuperar) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Funcionario funcionario = null;
			String msg = getMessage("funcionario.sucesso.remover", idRemover);
			
			if (idRemover != null) {
				funcionario = consultarPorId(idRemover);
				funcionario.setStatus(StatusFuncionario.REMOVIDO);
			} else {
				funcionario = consultarPorId(idRecuperar);
				funcionario.setStatus(StatusFuncionario.DEMITIDO);
				msg = getMessage("funcionario.sucesso.recuperar", idRecuperar);
			}
			
			dao.alterar(funcionario);
			
			tx.commit();
			
			sessao.addMensagem(msg);
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			if (idRemover != null)
				sessao.addErro(getMessage("funcionario.falha.remover", idRemover));
			else
				sessao.addErro(getMessage("funcionario.falha.recuperar", idRecuperar));
		}
		
		result.redirectTo("/funcionarios/listar");
	}

	private Funcionario consultarPorId(Long id) {
		
		Funcionario funcionario = null;
		try {
			
			funcionario = dao.consultarComCusto(id, new Date());

			if (funcionario == null)
				validator.add(new ValidationMessage(getMessage("funcionario.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
			DadosGerais dadosGerais = dadosGeraisDao.buscarVigente();
			
			if (funcionario.getSalarioMes().compareTo(dadosGerais.getTetoSalFamilia()) <= 0)
				funcionario.setSalarioFamilia(dadosGerais.getAdicDependente().multiply(BigDecimal.valueOf(funcionario.getQuantDependentes())));
			
			funcionario.setEncargo1(dadosGerais.getEncargo(funcionario, DadosGerais.ENCARGO1));
			funcionario.setEncargo2(dadosGerais.getEncargo(funcionario, DadosGerais.ENCARGO2));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("funcionario.falha.recuperar", id));
			result.forwardTo(this).listarFuncionarios(null);
		}
		
		return funcionario;
	}
	
	private DadosGerais buscarDadosGerais() {
		
		DadosGerais dadosGerais = null;
		try {
			
			dadosGerais = dadosGeraisDao.buscarVigente();
			
			if (dadosGerais == null)
				validator.add(new ValidationMessage(getMessage("dadosGerais.falha.naoCadastrado"), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("dadosGerais.falha.recuperar"));
			result.forwardTo(this).listarFuncionarios(null);
		}
		
		return dadosGerais;
	}
	
	@Get @Post @Path("/funcionarios/listar")
	public void listarFuncionarios(FiltroFuncionario filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroFuncionario.newInstance(getRegistrosPagina());

			for (OrdenacaoFuncionario ordem: OrdenacaoFuncionario.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaFuncionarios", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarFuncionarios.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Get @Path("/relatorios/funcionarios")
	public FuncionarioRelatorio relatorioFuncionario(FuncionarioRelatorio relatorio) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		try {

			FiltroFuncionario filtro = FiltroFuncionario.newInstance(-1);
			filtro.setRemovidos(null);
			result.include("funcionarios", dao.filtrar(filtro));
			result.include("listaStatus", StatusFuncionario.values());
			result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
			result.include("centrosCusto", centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorioFuncionario.falha.carregar"));
			result.redirectTo("/");
		}
		
		return relatorio;		
		
	}
	
	@Post @Path({"/relatorios/funcionarios", "/relatorios/funcionarios.xls"})
	public void relatorioFuncionario(final FuncionarioRelatorio relatorio, HttpServletRequest request, HttpServletResponse response) {
		
		validator.checking(new Validations() {{
			that(preenchido(relatorio.getFuncionario().getId()), "relatorioFuncionario.funcionario", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).relatorioFuncionario(relatorio);
		
		String template = "relatorioFuncionario";
		
		relatorio.setFuncionario(consultarPorId(relatorio.getFuncionario().getId()));
		
		if (relatorio.getDataDe() == null) 
			relatorio.setDataDe(new Date(relatorio.getFuncionario().getDataAdmissao().getTime()));
		if (relatorio.getDataAte() == null)
			relatorio.setDataAte(new Date());
		
		URL urlTemplate = Resources.getResource("relat/" + template + ".jasper");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("SUBREPORT_DIR", urlTemplate.getPath().subSequence(0, urlTemplate.getPath().lastIndexOf("/") + 1));
		
		int diasTrab = Util.diferencaDiasUteis(relatorio.getDataDe(), relatorio.getDataAte());
		params.put("PERIODO", Util.dataToString(relatorio.getDataDe()) + " - " + Util.dataToString(relatorio.getDataAte()));
		params.put("DIAS_UTEIS", diasTrab + "");
		
		params.put("NOME", relatorio.getFuncionario().getNome().toUpperCase());
		params.put("ENDERECO", relatorio.getFuncionario().getEndereco().getCompleto().toUpperCase());
		params.put("APELIDO", relatorio.getFuncionario().getApelido().toUpperCase());
		params.put("EMPRESA", relatorio.getFuncionario().getEmpresa().getNomeCurto().toUpperCase());
		params.put("FUNCAO", relatorio.getFuncionario().getFuncao().getNome().toUpperCase());
		params.put("SINDICATO", relatorio.getFuncionario().getSindicato().getNome().toUpperCase());
		if (relatorio.getFuncionario().getCentroCusto() != null)
			params.put("CENTRO_CUSTO", relatorio.getFuncionario().getCentroCusto().getNomeCurto().toUpperCase());
		else
			params.put("CENTRO_CUSTO", "");
		params.put("DATA_ADMISSAO", Util.dataToString(relatorio.getFuncionario().getDataAdmissao()));
		
		String status = relatorio.getFuncionario().getStatus().toString();
		if (!status.equals(StatusFuncionario.DEMITIDO.toString()))
			params.put("SITUACAO", status);
		else
			params.put("SITUACAO", status + " EM " + Util.dataToString(relatorio.getFuncionario().getDataDemissao()));
			
		params.put("QUANT_DEPEND",  relatorio.getFuncionario().getQuantDependentes() + "");
		
		params.put("SALARIO_BASE", Util.valorToString(relatorio.getFuncionario().getSalarioBase()));
		params.put("ANUENIO", Util.valorToString(relatorio.getFuncionario().getAnuenioSalario()));
		params.put("BONUS", Util.valorToString(relatorio.getFuncionario().getBonusSalario()));
		params.put("ANUENIO_BONUS", Util.valorToString(relatorio.getFuncionario().getAnuenioBonus()));
		params.put("ENCARGO", Util.valorToString(relatorio.getFuncionario().getEncargo1()));
		params.put("ENCARGO_BONUS", Util.valorToString(relatorio.getFuncionario().getEncargo2()));
		params.put("SAL_FAMILIA", Util.valorToString(relatorio.getFuncionario().getSalarioFamilia()));
		params.put("SAL_HORA", Util.valorToString(relatorio.getFuncionario().getSalarioHora()));
		params.put("SAL_MES", Util.valorToString(relatorio.getFuncionario().getSalarioMes()));
		
		ServletOutputStream sos = null;
		
		try {
			
			XmlWriter xml = new XmlWriter(template);
			xml.abre();
			
			int qtdFaltas = 0;
			int qtdAbonos = 0;
			int qtdFerias = 0;
			
			FiltroApontamento filtroApontamento = FiltroApontamento.newInstance(-1);
			filtroApontamento.setFuncionario(relatorio.getFuncionario());
			filtroApontamento.setData(Util.diaSemHora(relatorio.getDataDe()));
			filtroApontamento.setStatusFuncionario(relatorio.getStatus());
			filtroApontamento.setDataFim(Util.diaCompleto(relatorio.getDataAte()));				
			
			if (FuncionarioRelatorio.FALTAS.equals(relatorio.getFiltroLista()))
				filtroApontamento.setMotivoFalta(MotivoFalta.FALTA);
			else if (FuncionarioRelatorio.ABONOS.equals(relatorio.getFiltroLista()))
				filtroApontamento.setMotivoFalta(MotivoFalta.ABONO);
			else if (FuncionarioRelatorio.FERIAS.equals(relatorio.getFiltroLista()))
				filtroApontamento.setMotivoFalta(MotivoFalta.FERIAS);
			
			if (relatorio.getObra().getId() != null)
				filtroApontamento.setObra(relatorio.getObra());
			else if (relatorio.getCentroCusto().getId() != null)
				filtroApontamento.setCentroCusto(relatorio.getCentroCusto());
			
			Collection<Apontamento> aponts = apontDao.filtrarComPontos(filtroApontamento);
			
			for (Apontamento apont: aponts) {
				if (apont.getPontos() == null)
					continue;
				
				for (Ponto ponto: apont.getPontos()) {
					xml.abreTag("PONTO");
					xml.novoElemento("APT_DATA", Util.dataToString(apont.getData()));
					if (apont.getObra() != null)
						xml.novoElemento("APT_OBRA", apont.getObra().getNomeCurto().toUpperCase());
					else
						xml.novoElemento("APT_OBRA", apont.getCentroCusto().getNomeCurto().toUpperCase());
					xml.novoElemento("APT_HENTRADA", ponto.getHoraEntrada() != null ? Util.decimalToHora(Util.horasEmDecimal(ponto.getHoraEntrada())) : " ");
					xml.novoElemento("APT_HSAIDA", ponto.getHoraSaida() != null ? Util.decimalToHora(Util.horasEmDecimal(ponto.getHoraSaida())) : " ");
					xml.novoElemento("APT_MOTFALTA", ponto.getMotivoFalta() != null ? ponto.getMotivoFalta().toString() : " ");
					xml.novoElemento("APT_MOTABONO", !StringUtils.isNullOrEmpty(ponto.getMotivoAbono()) ? ponto.getMotivoAbono().toUpperCase() : " ");
					xml.fechaTag("PONTO");
					
					if (MotivoFalta.FALTA.equals(ponto.getMotivoFalta()))
						qtdFaltas++;
					else if (MotivoFalta.ABONO.equals(ponto.getMotivoFalta()))
						qtdAbonos++;
					else if (MotivoFalta.FERIAS.equals(ponto.getMotivoFalta()))
						qtdFerias++;
				}
			}
			
			xml.fecha();
			String arqXml = Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + template + ".xml";
			
			params.put("ABONOS", qtdAbonos + "");
			params.put("FALTAS", qtdFaltas + "");
			params.put("FERIAS", qtdFerias + "");
			
			byte[] bytes;
			
			if (relatorio.isExcel()) {

				String impressao;
				
				if (!Util.isVazioOuNulo(relatorio.getFiltroLista())) {
					params.put("imprimirPontos", "S");
					impressao = JasperFillManager.fillReportToFile(urlTemplate.getFile(), params, new JRXmlDataSource(arqXml, "/XML"));			
				} else {
					impressao = JasperFillManager.fillReportToFile(urlTemplate.getFile(), params, new JREmptyDataSource());
				}
				
				JRXlsExporter xlsExporter = new JRXlsExporter();
				File arqXls = new File(Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + template + ".xls");
				xlsExporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, impressao);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, arqXls.getAbsolutePath());
				xlsExporter.exportReport();

				bytes = Files.toByteArray(arqXls);
				response.setContentType("application/xls");

			} else {
			
				JasperPrint impressao; 			
				
				if (!Util.isVazioOuNulo(relatorio.getFiltroLista())) {
					params.put("imprimirPontos", "S");
					impressao = JasperFillManager.fillReport(urlTemplate.getFile(), params, new JRXmlDataSource(arqXml, "/XML"));			
				} else {
					impressao = JasperFillManager.fillReport(urlTemplate.getFile(), params, new JREmptyDataSource());
				}
				
				bytes = JasperExportManager.exportReportToPdf(impressao);
				response.setContentType("application/pdf");
			}
			
			
			byte[] arquivo = bytes;
			
			response.setContentLength(arquivo.length);
			sos = response.getOutputStream();
			sos.write(arquivo, 0, arquivo.length);
			sos.flush();
			sos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			if (sos != null) {
				try {
					sos.flush();
					sos.close();
				} catch (IOException e1) {
					log.error(e.getMessage());
				}
				result.redirectTo("/");
			}
		} catch (JRException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/funcionarios");
		} catch (TransformerConfigurationException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/funcionarios");
		} catch (SAXException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/funcionarios");
		}
		
		result.nothing();
		
	}	

	@Get @Path("/relatorios/ocorrencias")
	public FuncionarioRelatorio relatorioOcorrencia(FuncionarioRelatorio relatorio) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		FiltroFuncionario filtro = FiltroFuncionario.newInstance(-1);
		filtro.setRemovidos(null);
		result.include("funcionarios", dao.filtrar(filtro));
		result.include("motivos", MotivoFalta.values());
		result.include("status", StatusFuncionario.values());
		result.include("empresas", empresaDao.filtrar(FiltroEmpresa.newInstance(-1)));
		result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
		result.include("centrosCusto", centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1)));
		
		return relatorio;		
		
	}
	
	@Post @Path({"/relatorios/ocorrencias", "/relatorios/ocorrencias.xls"})
	public void relatorioFuncionarioPeriodo(final FuncionarioRelatorio relatorio, HttpServletRequest request, HttpServletResponse response) {
		
		if (relatorio.getDataDe() == null) 
			relatorio.setDataDe(Util.stringToData(Util.getConfig("config.dataInicio")));
		if (relatorio.getDataAte() == null)
			relatorio.setDataAte(new Date());
		
		
		String template = "relatorioOcorrencia";
		
		URL urlTemplate = Resources.getResource("relat/" + template + ".jasper");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("SUBREPORT_DIR", urlTemplate.getPath().subSequence(0, urlTemplate.getPath().lastIndexOf("/") + 1));
		
		int diasTrab = Util.diferencaDiasUteis(relatorio.getDataDe(), relatorio.getDataAte());
		params.put("PERIODO", Util.dataToString(relatorio.getDataDe()) + " - " + Util.dataToString(relatorio.getDataAte()));
		params.put("DIAS_UTEIS", diasTrab + "");
		
		if (relatorio.getEmpresa().getId() != null) {
			relatorio.setEmpresa(empresaDao.buscarPorId(relatorio.getEmpresa().getId()));
			params.put("EMPRESA", relatorio.getEmpresa().getNomeCurto());
		} else {
			params.put("EMPRESA", Util.getMessage("todas"));
		}
		
		if (relatorio.getObra().getId() != null) {
			relatorio.setObra(obraDao.buscarPorId(relatorio.getObra().getId()));
			params.put("OBRA_CC", relatorio.getObra().getNomeCurto());
		} else if (relatorio.getCentroCusto().getId() != null) {
			relatorio.setCentroCusto(centroCustoDao.buscarPorId(relatorio.getCentroCusto().getId()));
			params.put("OBRA_CC", relatorio.getCentroCusto().getNomeCurto());
		} else {
			params.put("OBRA_CC", Util.getMessage("todas"));
		}
		
		ServletOutputStream sos = null;
		
		try {
			
			XmlWriter xml = new XmlWriter(template);
			xml.abre();

			List<Ponto> pontos = listarPontos(relatorio);
			
			Long idAtual = 0l;
									
			if (Boolean.TRUE.equals(relatorio.getAnalitico())) {
			
				// Analitico
				params.put("analitico", "S");
				
				for (Ponto ponto: pontos) {
					
					if (!ponto.getFuncionario().getId().equals(idAtual)) {
						
						idAtual = ponto.getFuncionario().getId();
						
						xml.abreTag("PONTO");
						xml.novoElemento("APT_FUNCIONARIO", " ");
						xml.novoElemento("APT_DATA", " ");
						xml.novoElemento("APT_MOTABONO", " ");
						xml.novoElemento("APT_HEH", " ");
						xml.novoElemento("APT_HEP", " ");
						xml.novoElemento("APT_MOTFALTA", " ");					
						xml.fechaTag("PONTO");
						
						xml.abreTag("PONTO");
						xml.novoElemento("APT_FUNCIONARIO", ponto.getFuncionario().getNome().toUpperCase());
						xml.novoElemento("APT_DATA", " ");
						if (ponto.getDescontos() != null && ponto.getDescontos().compareTo(BigDecimal.ZERO) > 0)
							xml.novoElemento("APT_MOTABONO", "DESCONTO: " + Util.decimalToHora(ponto.getDescontos()) + " hs");
						else
							xml.novoElemento("APT_MOTABONO", " ");
						xml.novoElemento("APT_HEH", ponto.getHoraExtra() != null ? Util.decimalToHora((ponto.getHoraExtra())) : " ");
						xml.novoElemento("APT_HEP", ponto.getPercHoraExtra() != null ? Util.valorToString(ponto.getPercHoraExtra()) : " ");
						xml.novoElemento("APT_MOTFALTA", ponto.getMotivoFalta() != null ? ponto.getMotivoFalta().toString() : " ");	
						xml.fechaTag("PONTO");
						
						xml.abreTag("PONTO");
						String status = " (" + Util.dataToString(ponto.getFuncionario().getDataAdmissao());
						if (StatusFuncionario.DEMITIDO.equals(ponto.getFuncionario().getStatus()))
							status += " - " + Util.dataToString(ponto.getFuncionario().getDataDemissao());
						status += ")";
						xml.novoElemento("APT_FUNCIONARIO", status);
						xml.novoElemento("APT_DATA", " ");
						xml.novoElemento("APT_MOTABONO", " ");
						xml.novoElemento("APT_HEH", " ");
						xml.novoElemento("APT_HEP", " ");
						xml.novoElemento("APT_MOTFALTA", " ");					
						xml.fechaTag("PONTO");
						
					} else {
						
						xml.abreTag("PONTO");
						xml.novoElemento("APT_FUNCIONARIO", " ");
						xml.novoElemento("APT_DATA", Util.dataToString(ponto.getApontamento().getData()));
						xml.novoElemento("APT_MOTABONO", !StringUtils.isNullOrEmpty(ponto.getMotivoAbono()) ? ponto.getMotivoAbono().toUpperCase() + " " : " ");
						xml.novoElemento("APT_HEH", ponto.getHoraExtra() != null ? Util.decimalToHora((ponto.getHoraExtra())) : " ");
						xml.novoElemento("APT_HEP", ponto.getPercHoraExtra() != null ? Util.valorToString(ponto.getPercHoraExtra()) : " ");
						xml.novoElemento("APT_MOTFALTA", ponto.getMotivoFalta() != null ? ponto.getMotivoFalta().toString() : " ");					
						xml.fechaTag("PONTO");
					}
					
				}
				
			} else {
				
				// Sintetico
				params.put("analitico", "N");
				
				List<Ponto> pontosRelat = new ArrayList<Ponto>();
				Ponto pontoRelat = new Ponto();
				for (Ponto ponto: pontos) {
					if (!ponto.getFuncionario().getId().equals(idAtual)) {
						idAtual = ponto.getFuncionario().getId();
						pontoRelat = new Ponto();
						pontoRelat.setFuncionario(ponto.getFuncionario());
						pontoRelat.setDescontos(ponto.getDescontos());
						pontosRelat.add(pontoRelat);
					}
					if (ponto.getHoraExtra() != null)
						pontoRelat.setHoraExtra(pontoRelat.getHoraExtra() != null ? pontoRelat.getHoraExtra().add(ponto.getHoraExtra()) : ponto.getHoraExtra());
						
					if (ponto.getMotivoFalta() != null) {
						switch (ponto.getMotivoFalta()) {
						case FALTA:
							pontoRelat.setFaltas(pontoRelat.getFaltas() != null ? pontoRelat.getFaltas() + 1 : 1);
							break;
						case FERIAS:
							pontoRelat.setFerias(pontoRelat.getFerias() != null ? pontoRelat.getFerias() + 1 : 1);
							break;
						case ABONO:
							pontoRelat.setAbonos(pontoRelat.getAbonos() != null ? pontoRelat.getAbonos() + 1 : 1);
							break;
						}
					}
				}
				
				int num = 1;
				for (Ponto ponto: pontosRelat) {
					xml.abreTag("PONTO");
					xml.novoElemento("APT_NUM", num++ + "");
					xml.novoElemento("APT_FUNCIONARIO", ponto.getFuncionario().getNome().toUpperCase());
					
					String status = ponto.getFuncionario().getStatus().name() + " (";
					if (!StatusFuncionario.DEMITIDO.equals(ponto.getFuncionario().getStatus()))
						status += Util.dataToString(ponto.getFuncionario().getDataAdmissao());
					else
						status += Util.dataToString(ponto.getFuncionario().getDataDemissao());
					status += ")";
					xml.novoElemento("APT_STATUS", status);
					
					xml.novoElemento("APT_FALTAS", ponto.getFaltas() != null && ponto.getFaltas() > 0 ? ponto.getFaltas().toString() : " ");
					xml.novoElemento("APT_FERIAS", ponto.getFerias() != null && ponto.getFerias() > 0 ? ponto.getFerias().toString() : " ");
					xml.novoElemento("APT_ABONOS", ponto.getAbonos() != null && ponto.getAbonos() > 0 ? ponto.getAbonos().toString() : " ");
					xml.novoElemento("APT_HEXTRAS", ponto.getDescontos() != null ? Util.decimalToHora(ponto.getDescontos()) : " ");
					xml.fechaTag("PONTO");
				}
				
			}
			
			xml.fecha();
			String arqXml = Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + template + ".xml";
			byte[] bytes;
			
			if (relatorio.isExcel()) {

				String impressao = JasperFillManager.fillReportToFile(urlTemplate.getFile(), params, new JRXmlDataSource(arqXml, "/XML"));
				JRXlsExporter xlsExporter = new JRXlsExporter();
				File arqXls = new File(Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + template + ".xls");
				xlsExporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, impressao);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, arqXls.getAbsolutePath());
				xlsExporter.exportReport();

				bytes = Files.toByteArray(arqXls);
				response.setContentType("application/xls");

			} else {
			
				JasperPrint impressao = JasperFillManager.fillReport(urlTemplate.getFile(), params, new JRXmlDataSource(arqXml, "/XML"));			
				
				bytes = JasperExportManager.exportReportToPdf(impressao);
				response.setContentType("application/pdf");
			}
			
			byte[] arquivo = bytes;
			
			response.setContentLength(arquivo.length);
			sos = response.getOutputStream();
			sos.write(arquivo, 0, arquivo.length);
			sos.flush();
			sos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			if (sos != null) {
				try {
					sos.flush();
					sos.close();
				} catch (IOException e1) {
					log.error(e.getMessage());
				}
				result.redirectTo("/");
			}
		} catch (JRException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/ocorrencias");
		} catch (TransformerConfigurationException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/ocorrencias");
		} catch (SAXException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/ocorrencias");
		}
		
		result.nothing();
		
	}
	
	private List<Ponto> listarPontos(FuncionarioRelatorio relatorio) {
		
		FiltroApontamento filtroApontamento = FiltroApontamento.newInstance(-1);
		filtroApontamento.setOcorrencias(true);
		filtroApontamento.setData(Util.diaSemHora(relatorio.getDataDe()));
		filtroApontamento.setDataFim(Util.diaCompleto(relatorio.getDataAte()));
		filtroApontamento.setStatusFuncionario(relatorio.getStatus());
		if (relatorio.getFuncionario().getId() != null)
			filtroApontamento.setFuncionario(relatorio.getFuncionario());
		if (relatorio.getEmpresa().getId() != null)
			filtroApontamento.setEmpresa(relatorio.getEmpresa());
		
		Collection<Apontamento> aponts = apontDao.filtrarComPontosPorData(filtroApontamento);
		
		List<Ponto> pontos = new ArrayList<Ponto>();
		
		DadosGerais dadosGerais = null;
		
		Map<Integer, BigDecimal> compSemana = new HashMap<Integer, BigDecimal>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(filtroApontamento.getData());
		int semana = cal.get(Calendar.WEEK_OF_YEAR);
		int mesAtual = 0;
		BigDecimal limiteMes = BigDecimal.ZERO;
		BigDecimal comp = BigDecimal.ZERO;
		
		while (cal.getTime().compareTo(filtroApontamento.getDataFim()) <= 0) {

			if (dadosGerais == null || cal.get(Calendar.MONTH) != mesAtual) {
				dadosGerais = dadosGeraisDao.consultarPorData(filtroApontamento.getData());
				mesAtual = cal.get(Calendar.MONTH);
			}
			
			limiteMes = limiteMes.add(BigDecimal.valueOf(dadosGerais.getJornadaDiaMin() / 60.0));

			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
			
				if (cal.get(Calendar.WEEK_OF_YEAR) != semana) {
					compSemana.put(semana, comp);
					comp = BigDecimal.ZERO;
					semana = cal.get(Calendar.WEEK_OF_YEAR);
				}
				
				comp = comp.add(dadosGerais.getCompensacao(cal.getTime()));
			}
			
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		compSemana.put(semana, comp);

		Set<String> ocorrSemana = new HashSet<String>();
		
		for (Apontamento apont: aponts) {
			cal.setTime(apont.getData());
			
			BigDecimal cargaHoraria = dadosGerais.getCargaHoraria(Util.diaCompleto(apont.getData()));
			
			if (relatorio.getObra().getId() != null && !relatorio.getObra().equals(apont.getObra()))
				continue;
			if (relatorio.getCentroCusto().getId() != null && !relatorio.getCentroCusto().equals(apont.getCentroCusto()))
				continue;
			
			for (Ponto ponto: apont.getPontos()) {
				
				boolean falta = MotivoFalta.FALTA.equals(ponto.getMotivoFalta());
				boolean atrazo = ponto.getHoraExtra() != null && ponto.getHoraExtra().compareTo(BigDecimal.ZERO) < 0 && !MotivoFalta.ABONO.equals(ponto.getMotivoFalta());

				if (falta)
					ponto.setDescontos(cargaHoraria);
				else if (atrazo)
					ponto.setDescontos(ponto.getHoraExtra().negate());
				
				if ((falta || atrazo) && !ocorrSemana.contains(ponto.getFuncionario().getId().toString() + cal.get(Calendar.WEEK_OF_YEAR))) {
					ocorrSemana.add(ponto.getFuncionario().getId().toString() + cal.get(Calendar.WEEK_OF_YEAR));
					ponto.setCompensacao(compSemana.get(cal.get(Calendar.WEEK_OF_YEAR)));
				}
				
				ponto.setApontamento(apont);
				pontos.add(ponto);
				
			}
			
		}
		
		Collections.sort(pontos);

		List<Ponto> aux = new ArrayList<Ponto>();

		for (Ponto ponto: pontos) {

			Ponto pto = new Ponto();
			
			if (!aux.contains(ponto)) {
				pto.setFuncionario(ponto.getFuncionario());
				pto.setDescontos(ponto.getDescontos());
				aux.add(pto);
			} else {
				pto = aux.get(aux.indexOf(ponto));
				pto.setDescontos(pto.getDescontos().add(ponto.getDescontos()));
			}

			if (ponto.getCompensacao() != null)
				pto.setDescontos(pto.getDescontos().add(ponto.getCompensacao()));

			ponto.setDescontos(null);
		}
		
		Long idAtual = 0l;
		List<Ponto> ret = new ArrayList<Ponto>();
		for (Ponto ponto: pontos) {
			if (!ponto.getFuncionario().getId().equals(idAtual)) {
				idAtual = ponto.getFuncionario().getId();
				Ponto pto = aux.get(aux.indexOf(ponto));
				if (pto.getDescontos().compareTo(limiteMes) > 0)
					pto.setDescontos(limiteMes);
				ret.add(pto);
			}
			ret.add(ponto);
		}
		
		return ret;
		
	}
	
	@Get @Path("/relatorios/ponto")
	public FuncionarioRelatorio relatorioPonto(FuncionarioRelatorio relatorio) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		try {

			result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
			result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorioFuncionario.falha.carregar"));
			result.redirectTo("/");
		}
		
		return relatorio;		
		
	}
	
	@Post @Path({"/relatorios/ponto", "/relatorios/ponto.xls"})
	public void relatorioPonto(final FuncionarioRelatorio relatorio, HttpServletRequest request, HttpServletResponse response) {
		
		validator.checking(new Validations() {{
			that(preenchido(relatorio.getObra().getId()) || preenchido(relatorio.getSindicato().getId()), "relatorioFuncionario.sindicato", "obrigatorio");
			that(preenchido(relatorio.getDataDe()), "relatorioFuncionario.dataDe", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).relatorioPonto(relatorio);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(relatorio.getDataDe());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		relatorio.setDataDe(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		relatorio.setDataAte(cal.getTime());
		
		String template = "relatorioPonto";

		List<Obra> obras;
		
		if (relatorio.getObra().getId() != null) {
		
			Obra obra = obraDao.buscarPorId(relatorio.getObra().getId());
			relatorio.setSindicato(obra.getSindicato());
			obras = new ArrayList<Obra>();
			obras.add(obra);

		} else {
		
			FiltroObra filtroObra = FiltroObra.newInstance(-1);
			filtroObra.setAtivas(true);
			filtroObra.setSindicato(relatorio.getSindicato());
			obras = new ArrayList<Obra>(obraDao.filtrar(filtroObra));
		}
		
		List<Funcionario> funcionarios = new ArrayList<Funcionario>(dao.listarAtivos(relatorio.getDataDe()));
		
		URL urlTemplate = Resources.getResource("relat/" + template + ".jasper");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("SUBREPORT_DIR", urlTemplate.getPath().subSequence(0, urlTemplate.getPath().lastIndexOf("/") + 1));
		params.put("PERIODO", Util.dataToString(relatorio.getDataDe()) + " - " + Util.dataToString(relatorio.getDataAte()));
		
		ServletOutputStream sos = null;
		
		try {
			
			XmlWriter xml = new XmlWriter(template);
			xml.abre();
			
			for (Obra obra: obras) {
				if (relatorio.getSindicato().getId() != null && !obra.getSindicato().equals(relatorio.getSindicato()))
					continue;

				xml.abreTag("PONTO");
				xml.novoElemento("OBRA", obra.getNome().toUpperCase());
				xml.fechaTag("PONTO");
				
			}
			
			int qtd = 0;
			for (Funcionario funcionario: funcionarios) {
				if (!funcionario.getSindicato().equals(relatorio.getSindicato()))
					continue;
				
				xml.abreTag("SUB_PONTO");
				xml.novoElemento("FUNCIONARIO", WordUtils.capitalizeFully(funcionario.getNomeCurto()));
				xml.fechaTag("SUB_PONTO");
				qtd++;
			}
			
			double pags = qtd / 33.00;
			int ext = (int) pags;
			if (pags > ext)
				ext++;
			ext = (ext * 33) - qtd;
			for (int x = 0; x < ext; x++) {
				xml.abreTag("SUB_PONTO");
				xml.novoElemento("FUNCIONARIO", " ");
				xml.fechaTag("SUB_PONTO");
			}
			
			xml.fecha();
			String arqXml = Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + template + ".xml";
			
			byte[] bytes;
			
			if (relatorio.isExcel()) {
				
				String impressao = JasperFillManager.fillReportToFile(urlTemplate.getFile(), params, new JRXmlDataSource(arqXml, "/XML"));			
				JRXlsExporter xlsExporter = new JRXlsExporter();
				File arqXls = new File(Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + template + ".xls");
				xlsExporter.setParameter(JRExporterParameter.INPUT_FILE_NAME, impressao);
				xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, arqXls.getAbsolutePath());
				xlsExporter.exportReport();
				
				bytes = Files.toByteArray(arqXls);
				response.setContentType("application/xls");
				
			} else {
				
				JasperPrint impressao = JasperFillManager.fillReport(urlTemplate.getFile(), params, new JRXmlDataSource(arqXml, "/XML"));			
				bytes = JasperExportManager.exportReportToPdf(impressao);
				response.setContentType("application/pdf");
			}
			
			byte[] arquivo = bytes;
			
			response.setContentLength(arquivo.length);
			sos = response.getOutputStream();
			sos.write(arquivo, 0, arquivo.length);
			sos.flush();
			sos.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			if (sos != null) {
				try {
					sos.flush();
					sos.close();
				} catch (IOException e1) {
					log.error(e.getMessage());
				}
				result.redirectTo("/");
			}
		} catch (JRException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/ponto");
		} catch (TransformerConfigurationException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/ponto");
		} catch (SAXException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/ponto");
		}
		
		result.nothing();
		
	}
	
}
