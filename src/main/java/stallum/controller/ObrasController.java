package stallum.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.xml.sax.SAXException;

import stallum.dao.AditivoDao;
import stallum.dao.ApontamentoDao;
import stallum.dao.CentroCustoDao;
import stallum.dao.ClienteDao;
import stallum.dao.DespesaDao;
import stallum.dao.EmpresaDao;
import stallum.dao.MedicaoDao;
import stallum.dao.ObraDao;
import stallum.dao.SindicatoDao;
import stallum.dto.FiltroAditivo;
import stallum.dto.FiltroApontamento;
import stallum.dto.FiltroCentroCusto;
import stallum.dto.FiltroCliente;
import stallum.dto.FiltroDespesa;
import stallum.dto.FiltroEmpresa;
import stallum.dto.FiltroMedicao;
import stallum.dto.FiltroObra;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoApontamento;
import stallum.dto.OrdenacaoObra;
import stallum.dto.Paginacao;
import stallum.enums.Estado;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusObra;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.infra.XmlWriter;
import stallum.modelo.Aditivo;
import stallum.modelo.Apontamento;
import stallum.modelo.Despesa;
import stallum.modelo.Medicao;
import stallum.modelo.Obra;
import stallum.modelo.Ponto;
import stallum.relatorio.ObraRelatorio;
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
public class ObrasController extends AbstractController {

	private Logger log = Logger.getLogger(ObrasController.class);
	
	private final ObraDao dao;
	private final AditivoDao aditivoDao;
	private final DespesaDao despesaDao;
	private final MedicaoDao medicaoDao;
	private final ApontamentoDao apontDao;
	private final ClienteDao clienteDao;
	private final CentroCustoDao centroCustoDao;
	private final EmpresaDao empresaDao;
	private final SindicatoDao sindicatoDao;

	public ObrasController(ObraDao dao, AditivoDao aditivoDao, ApontamentoDao apontDao, DespesaDao despesaDao, MedicaoDao medicaoDao, 
			ClienteDao clienteDao, EmpresaDao empresaDao, CentroCustoDao centroCustoDao, SindicatoDao sindicatoDao,
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.aditivoDao = aditivoDao;
		this.apontDao = apontDao;
		this.medicaoDao = medicaoDao;
		this.despesaDao = despesaDao;
		this.clienteDao = clienteDao;
		this.empresaDao = empresaDao;
		this.centroCustoDao = centroCustoDao;
		this.sindicatoDao = sindicatoDao;
	}
	
	@Get @Path({"/obras/nova", "/obras/{obra.id}/editar"})
	public Obra formObra(Obra obra) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (obra == null) {
				obra = new Obra();
				obra.setStatus(StatusObra.ATIVA);
			} else if (obra.getId() != null && obra.getVersao() == null) {
				obra = consultarPorId(obra.getId());
			}
			
			result.include("empresas", empresaDao.filtrar(FiltroEmpresa.newInstance(-1)));
			result.include("clientes", clienteDao.filtrar(FiltroCliente.newInstance(-1)));
			result.include("centrosCusto", centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1)));
			result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
			result.include("estados", Estado.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formObra.falha.carregar"));
			result.redirectTo("/obras/listar");
		}
		
		return obra;
	}
	
	@Get @Post @Path("/obras/listar")
	public void listarObras(FiltroObra filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroObra.newInstance(getRegistrosPagina());

			for (OrdenacaoObra ordem: OrdenacaoObra.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaObras", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarObras.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Put @Post @Path("/obras")
	public void salvar(final Obra obra, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(obra.getNome()), "obra.nome", "obrigatorio");
			that(preenchido(obra.getEmpresa().getId()), "obra.empresa", "obrigatoria");
			that(preenchido(obra.getCliente().getId()), "obra.cliente", "obrigatorio");
			that(preenchido(obra.getSindicato().getId()), "obra.sindicato", "obrigatorio");
			that(preenchido(obra.getDataInicio()), "obra.dataInicio", "obrigatoria");
			if (sessao.isGerente())
				that(obra.getValorInicial() != null && !obra.getValorInicial().equals(BigDecimal.ZERO), "obra.valorInicial", "obrigatorio");
			that(preenchido(obra.getEndereco().getLogradouro()), "endereco.logradouro", "obrigatorio");
			that(preenchido(obra.getEndereco().getNumero()), "endereco.numero", "obrigatorio");
			that(preenchido(obra.getEndereco().getCidade()), "endereco.cidade", "obrigatorio");
			that(preenchido(obra.getEndereco().getEstado()) && !obra.getEndereco().getEstado().equalsIgnoreCase("XX"), "endereco.estado", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formObra(obra);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			boolean novo = true;
			
			obra.setEmpresa(empresaDao.buscarPorId(obra.getEmpresa().getId()));
			obra.setCliente(clienteDao.buscarPorId(obra.getCliente().getId()));
			obra.setSindicato(sindicatoDao.buscarPorId(obra.getSindicato().getId()));
			
			if (obra.getCentroCusto().getId() != null)
				obra.setCentroCusto(centroCustoDao.buscarPorId(obra.getCentroCusto().getId()));
			else
				obra.setCentroCusto(null);
			
			if (obra.getId() == null) {
				obra.setStatus(StatusObra.ATIVA);
				dao.incluir(obra);								
			} else {
				dao.alterar(obra);
				novo = false;
			}
			
			if (obra.getEndereco() != null && preenchido(obra.getEndereco().getLogradouro())) {
				
				if (obra.getEndereco().getId() == null)
					dao.incluir(obra.getEndereco());
				else
					dao.alterar(obra.getEndereco());
				
			} else {
				obra.setEndereco(null);
			}
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formObra.sucesso"));
			
			if (novo)
				result.redirectTo("/obras/nova");
			else
				result.redirectTo("/obras/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formObra.falha.salvar"));
			result.redirectTo(this).formObra(obra);
		}
		
	}
	
	@Get @Path({"/obras/{idRemover}/remover", "/obras/{idRecuperar}/recuperar"})
	public void removerRecuperar(Long idRemover, Long idRecuperar) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Obra obra = null;
			String msg = getMessage("obra.sucesso.remover", idRemover);
			
			if (idRemover != null) {
				obra = consultarPorId(idRemover);
				obra.setStatus(StatusObra.REMOVIDA);
			} else {
				obra = consultarPorId(idRecuperar);
				obra.setStatus(StatusObra.ATIVA);
				msg = getMessage("obra.sucesso.recuperar", idRecuperar);
			}
			
			dao.alterar(obra);
			
			tx.commit();
			
			sessao.addMensagem(msg);
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			if (idRemover != null)
				sessao.addErro(getMessage("obra.falha.remover", idRemover));
			else
				sessao.addErro(getMessage("obra.falha.recuperar", idRecuperar));
		}
		
		result.redirectTo("/obras/listar");
	}
	
	private Obra consultarPorId(Long id) {
		
		Obra obra = null;
		try {
			
			obra = dao.consultarPorId(id);
			
			if (obra == null)
				validator.add(new ValidationMessage(getMessage("obra.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("obra.falha.recuperar", id));
			result.forwardTo("/obras/listar");
		}
		
		return obra;
	}
	
	@Get @Path("/relatorios/obra")
	public ObraRelatorio relatorioObra(ObraRelatorio relatorio) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		try {

			result.include("obras", dao.filtrar(FiltroObra.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha.carregar"));
			result.redirectTo("/");
		}
		
		return relatorio;		
	}
	
	@Post @Path({"/relatorios/obra","/relatorios/obra.xls"})
	public void emitirRelatorioObra(final ObraRelatorio relatorio, HttpServletRequest request, HttpServletResponse response) {
		
		validator.checking(new Validations() {{
			that(preenchido(relatorio.getObra().getId()), "relatorioObra.obra", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).relatorioObra(relatorio);
		
		relatorio.setObra(consultarPorId(relatorio.getObra().getId()));

		if (relatorio.getDataDe() == null) 
			relatorio.setDataDe(new Date(relatorio.getObra().getDataInicio().getTime()));
		if (relatorio.getDataAte() == null)
			relatorio.setDataAte(new Date());
		if (relatorio.getObra().getDataStatus() == null)
			relatorio.getObra().setDataStatus(relatorio.getObra().getDataInicio());
		
		String template = "relatorioObra";
		
		URL urlTemplate = Resources.getResource("relat/" + template + ".jasper");
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("SUBREPORT_DIR", urlTemplate.getPath().subSequence(0, urlTemplate.getPath().lastIndexOf("/") + 1));
		
		int diasTrab = Util.diferencaDiasUteis(relatorio.getDataDe(), relatorio.getDataAte());
		params.put("PERIODO", Util.dataToString(relatorio.getDataDe()) + " - " + Util.dataToString(relatorio.getDataAte()));
		params.put("DIAS_UTEIS", diasTrab + "");
		
		params.put("NOME_OBRA", relatorio.getObra().getNome().toUpperCase());
		params.put("END_OBRA", relatorio.getObra().getEndereco().getCompleto().toUpperCase());
		params.put("NOME_CLIENTE", relatorio.getObra().getCliente().getNomeCurto().toUpperCase());
		params.put("NOME_EMPRESA", relatorio.getObra().getEmpresa().getNomeCurto().toUpperCase());
		params.put("SINDICATO", relatorio.getObra().getSindicato().getNome());
		params.put("CONTRATO", relatorio.getObra().getContrato().toUpperCase());
		params.put("DATA_INICIO", Util.dataToString(relatorio.getObra().getDataInicio()));
		params.put("STATUS", relatorio.getObra().getStatus().toString());
		params.put("DATA_STATUS", Util.dataToString(relatorio.getObra().getDataStatus()));
		
		if (sessao.isGerente()) {
			params.put("VALOR_INICIAL", Util.valorToString(relatorio.getObra().getValorInicial()));
			params.put("TOTAL_ADITIVOS", Util.valorToString(relatorio.getObra().getTotalAditivos()));
			params.put("VALOR_TOTAL", Util.valorToString(relatorio.getObra().getTotalObra()));
			params.put("TOTAL_APONTAMENTOS", Util.valorToString(relatorio.getObra().getTotalApontamentos()));
			params.put("DESP_DIRETAS", Util.valorToString(relatorio.getObra().getTotalDespesas()));
			params.put("CUSTOS_INDIRETOS", Util.valorToString(relatorio.getObra().getTotalCustosIndiretos()));
			params.put("CUSTO_TOTAL", Util.valorToString(relatorio.getObra().getCustoTotal()));
			params.put("TOTAL_MEDICOES", Util.valorToString(relatorio.getObra().getTotalMedicoes()));
			params.put("RESULTADO_ATUAL", Util.valorToString(relatorio.getObra().getResultadoAtual()));
			params.put("SALDO_OBRA", Util.valorToString(relatorio.getObra().getSaldoReceber()));
			params.put("TOTAL_REAJUSTES", Util.valorToString(relatorio.getObra().getTotalReajustes()));
			params.put("MEDIA_FUNCS", Util.valorToString(relatorio.getObra().getMediaFuncionarios(), 0));
			params.put("REND_FUNC", Util.valorToString(relatorio.getObra().getResultFuncionario()));
			params.put("PERC_RESULT", Util.valorToString(relatorio.getObra().getPercResultado()));
			params.put("PERC_SALDO", Util.valorToString(relatorio.getObra().getPercSaldo()));
		}
		
		ServletOutputStream sos = null;
		
		try {
			
			XmlWriter xml = null;
			if (relatorio.isAnalitico()) {
				xml = new XmlWriter(template);
				xml.abre();
			}
			
			// Aditivos
			if (Boolean.TRUE.equals(relatorio.isAditivos())) {
				
				params.put("imprimirAditivos", "S");
				
				FiltroAditivo filtroAditivo = FiltroAditivo.newInstance(-1);
				filtroAditivo.setObra(relatorio.getObra());
				filtroAditivo.setDataDe(Util.diaSemHora(relatorio.getDataDe()));
				filtroAditivo.setDataAte(Util.diaCompleto(relatorio.getDataAte()));
				Collection<Aditivo> aditivos = aditivoDao.filtrar(filtroAditivo);
	
	//			BigDecimal totalAditivos = BigDecimal.ZERO;
				
				for (Aditivo aditivo: aditivos) {
	//				totalAditivos = totalAditivos.add(aditivo.getValor());
					if (Boolean.TRUE.equals(relatorio.isAditivos())) {
						xml.abreTag("ADITIVO");
						xml.novoElemento("ADT_NUMERO", aditivo.getNumero().toUpperCase());
						xml.novoElemento("ADT_DATA", Util.dataToString(aditivo.getData()));
						xml.novoElemento("ADT_DESCRICAO", aditivo.getDescricao().toUpperCase());
						xml.novoElemento("ADT_VALOR", Util.valorToString(aditivo.getValor()));
						xml.fechaTag("ADITIVO");
					}
				}
	//			params.put("TOTAL_ADITIVOS", Util.valorToString(totalAditivos));
				
			}
			
			// Despesas
			if (Boolean.TRUE.equals(relatorio.isMedicoes())) {
				
				params.put("imprimirMedicoes", "S");
				
				FiltroDespesa filtroDespesa = FiltroDespesa.newInstance(-1);
				filtroDespesa.setObra(relatorio.getObra());
				filtroDespesa.setDataDe(Util.diaSemHora(relatorio.getDataDe()));
				filtroDespesa.setDataAte(Util.diaCompleto(relatorio.getDataAte()));
				Collection<Despesa> despesas = despesaDao.filtrar(filtroDespesa);
				
	//			BigDecimal totalDespesas = BigDecimal.ZERO;
				
				for (Despesa despesa: despesas) {
	//				totalDespesas = totalDespesas.add(despesa.getValor());
					if (Boolean.TRUE.equals(relatorio.isDespesas())) {
						xml.abreTag("DESPESA");
						xml.novoElemento("DES_DATA", Util.dataToString(despesa.getData()));
						xml.novoElemento("DES_DESCRICAO", despesa.getDescricao().toUpperCase());
						xml.novoElemento("DES_VALOR", Util.valorToString(despesa.getValor()));
						xml.fechaTag("DESPESA");
					}
				}
	//			params.put("TOTAL_DESPESAS", Util.valorToString(totalDespesas));
				
			}
			
			// Medicoes
			if (Boolean.TRUE.equals(relatorio.isDespesas())) {
				
				params.put("imprimirDespesas", "S");
			
				FiltroMedicao filtroMedicao = FiltroMedicao.newInstance(-1);
				filtroMedicao.setObra(relatorio.getObra());
				filtroMedicao.setDataDe(Util.diaSemHora(relatorio.getDataDe()));
				filtroMedicao.setDataAte(Util.diaCompleto(relatorio.getDataAte()));
				Collection<Medicao> medicoes = medicaoDao.filtrar(filtroMedicao);
				
	//			BigDecimal totalMedicoes = BigDecimal.ZERO;
				
				for (Medicao medicao: medicoes) {
//					if (!Boolean.TRUE.equals(medicao.getCancelada()))
	//					totalMedicoes = totalMedicoes.add(medicao.getValor());
					if (Boolean.TRUE.equals(relatorio.isMedicoes())) {
						xml.abreTag("MEDICAO");
						xml.novoElemento("MED_DATA", Util.dataToString(medicao.getData()));
						xml.novoElemento("MED_NF", medicao.getNotaFiscal());
						xml.novoElemento("MED_VALOR", Util.valorToString(medicao.getValor()));
						xml.novoElemento("MED_ISS", medicao.getIss() != null ? Util.valorToString(medicao.getIss()) : " ");
						xml.novoElemento("MED_INSS", medicao.getInss() != null ? Util.valorToString(medicao.getInss()) : " ");
						xml.novoElemento("MED_CLSS", medicao.getClss() != null ? Util.valorToString(medicao.getClss()) : " ");
						xml.novoElemento("MED_IRRF", medicao.getIrrf() != null ? Util.valorToString(medicao.getIrrf()) : " ");
						xml.novoElemento("MED_ESTORNADO", Boolean.TRUE.equals(medicao.getCancelada()) ? "X" : " ");
						xml.fechaTag("MEDICAO");
					}
				}
	//			params.put("TOTAL_MEDICOES", Util.valorToString(totalMedicoes));
			
			}
			
			// Apontamentos
			if (Boolean.TRUE.equals(relatorio.isApontamentos())) {
				
				params.put("imprimirApontamentos", "S");
				
				FiltroApontamento filtroApontamento = FiltroApontamento.newInstance(-1);
				filtroApontamento.setObra(relatorio.getObra());
				filtroApontamento.setData(Util.diaSemHora(relatorio.getDataDe()));
				filtroApontamento.setDataFim(Util.diaCompleto(relatorio.getDataAte()));
				filtroApontamento.setOrdenacao(OrdenacaoApontamento.DATA);
				Collection<Apontamento> aponts = apontDao.filtrarComPontos(filtroApontamento);
				
	//			BigDecimal totalApont = BigDecimal.ZERO;
				
				for (Apontamento apont: aponts) {
					if (apont.getPontos() == null)
						continue;
					
					List<Ponto> pontos = (List<Ponto>) apont.getPontos();
					Collections.sort(pontos);
					
					for (Ponto ponto: pontos) {
	//					totalApont = totalApont.add(ponto.getCustoTotal());
						if (Boolean.TRUE.equals(relatorio.isApontamentos())) {
							xml.abreTag("APONTAMENTO");
							xml.novoElemento("APT_DATA", Util.dataToString(apont.getData()));
							xml.novoElemento("APT_FUNCIONARIO", ponto.getFuncionario().getNomeCurto().toUpperCase());
							xml.novoElemento("APT_HEH", ponto.getHoraExtra() != null ? Util.decimalToHora(ponto.getHoraExtra()) : " ");
							xml.novoElemento("APT_HEP", ponto.getPercHoraExtra() != null ? Util.valorToString(ponto.getPercHoraExtra()) : " ");
							xml.novoElemento("APT_MOTFALTA", ponto.getMotivoFalta() != null ? ponto.getMotivoFalta().toString() : " ");
							xml.novoElemento("APT_MOTABONO", !StringUtils.isNullOrEmpty(ponto.getMotivoAbono()) ? ponto.getMotivoAbono().toUpperCase() + " " : " ");
							xml.fechaTag("APONTAMENTO");
						}
					}
				}					
	//			params.put("TOTAL_APONTAMENTOS", Util.valorToString(totalApont));
				
			}

			byte[] bytes;
			String arqXml = Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + template + ".xml";
			
			if (relatorio.isExcel()) {

				String impressao;

				if (relatorio.isAnalitico()) {
					xml.fecha();
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
				
				if (relatorio.isAnalitico()) {
					xml.fecha();
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
				result.redirectTo("/relatorios/obra");
			}
		} catch (JRException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/obra");
		} catch (TransformerConfigurationException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/obra");
		} catch (SAXException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("relatorio.falha"));
			result.redirectTo("/relatorios/obra");
		}
		
		result.nothing();
		
	}
			
}
