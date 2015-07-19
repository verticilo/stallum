package stallum.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.DadosGeraisDao;
import stallum.dao.DissidioDao;
import stallum.dao.EmpresaDao;
import stallum.dao.FuncaoDao;
import stallum.dao.FuncionarioDao;
import stallum.dao.ObraDao;
import stallum.dao.SindicatoDao;
import stallum.dto.FiltroDissidio;
import stallum.dto.FiltroEmpresa;
import stallum.dto.FiltroFuncao;
import stallum.dto.FiltroObra;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoDissidio;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.DadosGerais;
import stallum.modelo.Dissidio;
import stallum.modelo.Empresa;
import stallum.modelo.Encargo;
import stallum.modelo.Funcao;
import stallum.modelo.HorarioTrabalho;
import stallum.modelo.Obra;
import stallum.modelo.Sindicato;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;

@Resource
public class DissidiosController extends AbstractController {

	private Logger log = Logger.getLogger(DissidiosController.class);
	
	private final DissidioDao dao;
	private final FuncaoDao funcaoDao;
	private final FuncionarioDao funcionarioDao;
	private final ObraDao obraDao;
	private final SindicatoDao sindicatoDao;
	private final EmpresaDao empresaDao;
	private final DadosGeraisDao dadosGeraisDao;

	public DissidiosController(DissidioDao dao, FuncaoDao funcaoDao, FuncionarioDao funcionarioDao, 
			ObraDao obraDao, SindicatoDao sindicatoDao, EmpresaDao empresaDao, DadosGeraisDao dadosGeraisDao,
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.funcaoDao = funcaoDao;
		this.funcionarioDao = funcionarioDao;
		this.obraDao = obraDao;
		this.sindicatoDao = sindicatoDao;
		this.empresaDao = empresaDao;
		this.dadosGeraisDao = dadosGeraisDao;
	}

	@Get @Path({"/dissidios/novo", "/dissidios/{dissidio.id}/editar"})
	public Dissidio formDissidio(Dissidio dissidio) {

		validaSessao(PerfilUsuario.OPERADOR);
		
		try {
			
			if (dissidio == null)
				dissidio = new Dissidio();
			
			if (dissidio.getId() != null && dissidio.getVersao() == null) {
				
				Collection<Dissidio> dissidiosBanco = consultarPorId(dissidio.getId());
				List<Date> datas = new ArrayList<Date>();
				List<BigDecimal> reajustes = new ArrayList<BigDecimal>();
				for (Dissidio dissidioBanco: dissidiosBanco) {
					datas.add(dissidioBanco.getData());
					reajustes.add(dissidioBanco.getPercReajuste());					
				}
				dissidio = (Dissidio) dissidiosBanco.toArray()[0];
				
				List<Encargo> encargos = new ArrayList<Encargo>();
				DadosGerais dadosGerais = dadosGeraisDao.consultarPorData(dissidio.getData());
				for (Encargo encargo: dadosGerais.getEncargos()) {
					if (dissidio.getFuncao().getSindicato().equals(encargo.getSindicato()))
						encargos.add(encargo);
				}
				
				result.forwardTo(this).formDissidio(dissidio, datas, reajustes, encargos);
			}
			
			result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDissidio.falha.carregar"));
			result.redirectTo("/dissidios/listar");
		}
		
		return dissidio;
				
	}
	
	@Post @Path("/dissidios/detalhar")
	public Dissidio formDissidio(final Dissidio dissidio, List<Date> datas, List<BigDecimal> reajustes, List<Encargo> encargos) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		validator.checking(new Validations() {{
			that(preenchido(dissidio.getFuncao().getSindicato().getId()), "funcao.sindicato", "obrigatorio");
			that(preenchido(dissidio.getData()), "dissidio.data", "obrigatoria");
			that(preenchido(dissidio.getPercReajuste()), "dissidio.percReajuste", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formDissidio(dissidio);
		
		try {
			
			FiltroFuncao filtroFuncao = FiltroFuncao.newInstance(-1);
			filtroFuncao.setSindicato(dissidio.getFuncao().getSindicato());
			Collection<Funcao> funcoes = funcaoDao.filtrar(filtroFuncao);
			
			BigDecimal percReajuste = dissidio.getPercReajuste().divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE);				

			Collection<Dissidio> dissidios = new ArrayList<Dissidio>();
			
			List<Sindicato> sindicatos = (List<Sindicato>)sindicatoDao.filtrar(FiltroSindicato.newInstance(-1));
			Sindicato sindicato = sindicatos.get(sindicatos.indexOf(dissidio.getFuncao().getSindicato()));
			
			int idx = 0;
			for (Funcao funcao: funcoes) {
								
				Dissidio dis = new Dissidio();
				dis.setData(dissidio.getData());
				dis.setPercReajuste(dissidio.getPercReajuste());
				
				// So recalcula salario se for novo
				if (dissidio.getId() == null)
					funcao.setSalario(funcao.getSalario().multiply(percReajuste));
				else
					dis.setPercReajuste(reajustes.get(idx));
				
				dis.setFuncao(funcao);
				
				dissidios.add(dis);
				
				idx++;
				
			}

			DadosGerais dadosGerais = dadosGeraisDao.buscarVigente();
			
			if (Boolean.TRUE.equals(sindicato.getSalarioMinimo()))
				dissidio.setBeneficio(dadosGerais.getBonificacao());
			else
				dissidio.setBeneficio(dadosGerais.getValeRefeicao());
			
			if (encargos == null) {
			
				Collection<Empresa> empresas = empresaDao.filtrar(FiltroEmpresa.newInstance(-1));
				encargos = new ArrayList<Encargo>();
				for (Empresa empresa: empresas) {
					Encargo encargo = new Encargo();
					encargo.setEmpresa(empresa);
					encargo.setSindicato(sindicato);
					encargo.setAliquota1(dadosGerais.getEncargo(empresa, dissidio.getFuncao().getSindicato(), DadosGerais.ENCARGO1));
					encargo.setAliquota2(dadosGerais.getEncargo(empresa, dissidio.getFuncao().getSindicato(), DadosGerais.ENCARGO2));
					encargos.add(encargo);
				}
				
			}
				
			result.include("sindicatos", sindicatos);
			result.include("encargos", encargos);
			result.include("dissidios", dissidios);
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDissidio.falha.carregar"));
			result.redirectTo(this).formDissidio(dissidio);
		}
		
		return dissidio;
	}
	
	@Post @Path("/dissidios")
	public void salvar(final Dissidio dissidio, List<BigDecimal> reajustes, List<BigDecimal> salarios, List<Encargo> encargos) {
		
		validaSessao(PerfilUsuario.OPERADOR);
		
		validator.checking(new Validations() {{
			that(preenchido(dissidio.getFuncao().getSindicato().getId()), "funcao.sindicato", "obrigatorio");
			that(preenchido(dissidio.getData()), "dissidio.data", "obrigatoria");
			that(preenchido(dissidio.getPercReajuste()), "dissidio.percReajuste", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formDissidio(dissidio);
		
		Transaction tx = dao.getSession().beginTransaction();		
		
		try {
			
			BigDecimal percReajuste = dissidio.getPercReajuste().divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE);
			
			Sindicato sindicato = sindicatoDao.consultarPorId(dissidio.getFuncao().getSindicato().getId());
			sindicato.setValorBeneficio(dissidio.getBeneficio());
			dao.alterar(sindicato);			
			
			DadosGerais dadosGerais;
			
			ArrayList<Dissidio> dissidiosBanco = null;
			if (dissidio.getId() != null) {

				// Se for alteracao, busca todos os dissidios do sindicato na data
			
				FiltroDissidio filtroDis = FiltroDissidio.newInstance(-1);
				filtroDis.setSindicato(sindicato);
				filtroDis.setDataDissidio(dissidio.getData());
				dissidiosBanco = (ArrayList<Dissidio>)dao.filtrar(filtroDis);
				
				dadosGerais = dadosGeraisDao.consultarPorData(dissidio.getData());

			} else {
				
				dadosGerais = dadosGeraisDao.buscarVigente();
				dadosGerais.setDescricao(getMessage("dadosGerais.inclusaoDissidio", sindicato.getNome(), Util.dataToString(dissidio.getData())));
				
			}
			
			if (Boolean.TRUE.equals(sindicato.getSalarioMinimo())) {
				if (dissidio.getData().compareTo(dadosGerais.getData()) > 0) {
					dadosGerais.setTetoSalFamilia(dadosGerais.getTetoSalFamilia().multiply(percReajuste));
					dadosGerais.setAdicDependente(dadosGerais.getAdicDependente().multiply(percReajuste));
				}
				dadosGerais.setBonificacao(dissidio.getBeneficio());
			} else {
				dadosGerais.setValeRefeicao(dissidio.getBeneficio());
			}
			
			int idx = 0;
			boolean novoDissidio = false;
			
			FiltroFuncao filtroFunc = FiltroFuncao.newInstance(-1);
			filtroFunc.setSindicato(sindicato);
			Collection<Funcao> funcoes = funcaoDao.filtrar(filtroFunc);			
			
			for (Funcao funcao: funcoes) {								
				
				// Só altera a funcao se a data do dissidio for igual ou porterior a data do ultimo dissidio 
				if (funcao.getDataUltimoDissidio() == null || dissidio.getData().compareTo(funcao.getDataUltimoDissidio()) >= 0) {				
					funcao.setSalario(salarios.get(idx));
					funcaoDao.alterar(funcao);
					funcionarioDao.atualizaSalario(funcao);
				}	
				
				Dissidio dissidioNovo = new Dissidio();
				dissidioNovo.setFuncao(funcao);
				dissidioNovo.setData(dissidio.getData());
				dissidioNovo.setPercReajuste(reajustes.get(idx));
				
				
				if (dissidiosBanco != null && dissidiosBanco.contains(dissidioNovo)) {

					Dissidio dissidioBanco = dissidiosBanco.get(dissidiosBanco.indexOf(dissidioNovo));
					dissidioBanco.setPercReajuste(reajustes.get(idx));					
					dao.alterar(dissidioBanco);
					
					
					if (idx == 0) recalculaSaldoObra(dissidio, dissidioBanco);
				
				} else {
					novoDissidio = true;

					dao.incluir(dissidioNovo);
					if (idx == 0) recalculaSaldoObra(dissidio, null);
				}
				
				
				idx++;
			}
			
			if (novoDissidio && !dadosGerais.getData().equals(dissidio.getData())) {
				
				dadosGerais.setId(null);
				dadosGerais.setAlteradoEm(null);
				dadosGerais.setData(dissidio.getData());
				dao.incluir(dadosGerais);
				
				// Salva horarios
				for (HorarioTrabalho horario: dadosGerais.getHorarios()) {
					horario.setId(null);
					horario.setAlteradoEm(null);
					horario.setDadosGerais(dadosGerais);
					dao.incluir(horario);
				}
				
			} else {
				dao.alterar(dadosGerais);
			}
			
			// Completa e grava os encargos para gravacao dos dados gerais
			for (Encargo encargo: criaEncargos(dadosGerais.getEncargos(), encargos)) {
				encargo.setDadosGerais(dadosGerais);
				if (novoDissidio || encargo.getId() == null) {
					encargo.setId(null);
					encargo.setAlteradoEm(null);
					dao.incluir(encargo);
				} else {
					dao.alterar(encargo);
				}
			}
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formDissidio.sucesso"));
			
			if (novoDissidio)
				result.redirectTo("/dissidios/novo");
			else
				result.redirectTo("/dissidios/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDissidio.falha.carregar"));
			result.redirectTo(this).formDissidio(dissidio);
		} catch (IndexOutOfBoundsException e) {
			sessao.addErro(getMessage("formDissidio.falha.campoVazio"));
			result.redirectTo(this).formDissidio(dissidio);			
		}
	}
	
	private void recalculaSaldoObra(Dissidio dissidioNovo, Dissidio dissidioBanco) throws HibernateException {
		
		if (dissidioBanco != null && dissidioNovo.getPercReajuste().equals(dissidioBanco.getPercReajuste()))
			return;
		
		FiltroObra filtro = FiltroObra.newInstance(-1);
		filtro.setSindicato(dissidioNovo.getFuncao().getSindicato());
		Collection<Obra> obras = obraDao.filtrar(filtro);
		
		for (Obra obra: obras) {
			
			BigDecimal percReajuste = dissidioNovo.getPercReajuste();
			boolean ajusteNeg = false;
			
			if (dissidioBanco != null)
				percReajuste = percReajuste.subtract(dissidioBanco.getPercReajuste());
			
			if (percReajuste.equals(BigDecimal.ZERO))
				continue;
			
			if (percReajuste.compareTo(BigDecimal.ZERO) < 0)
				ajusteNeg = true;
			
			percReajuste = percReajuste.abs();
			
			BigDecimal difSaldo = obra.getSaldoReceber().multiply(percReajuste.divide(BigDecimal.valueOf(100)));
			
			if (!ajusteNeg) {
				obra.setSaldoReceber(obra.getSaldoReceber().add(difSaldo));
				obra.setTotalReajustes(obra.getTotalReajustes().add(difSaldo));
			} else {
				obra.setSaldoReceber(obra.getSaldoReceber().subtract(difSaldo));
				obra.setTotalReajustes(obra.getTotalReajustes().subtract(difSaldo));				
			}
				
			obraDao.atualizaSaldo(obra);
			
		}
				
	}

	@Get @Post @Path("/dissidios/listar")
	public void listarDissidios(FiltroDissidio filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroDissidio.newInstance(getRegistrosPagina());

			for (OrdenacaoDissidio ordem: OrdenacaoDissidio.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaDissidios", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarDissidios.falha.listar"));
			result.redirectTo("/");
		}	
	}
		
	private Collection<Dissidio> consultarPorId(Long id) {
		
		Collection<Dissidio> dissidios = null;
		try {
			
			Dissidio dissidio = dao.buscarPorId(id);
			if (dissidio == null)
				validator.add(new ValidationMessage(getMessage("dissidio.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
			dissidios = dao.consultar(dissidio.getFuncao().getSindicato(), dissidio.getData());
						
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("dissidio.falha.recuperar", id));
			result.forwardTo("/dissidios/listar");
		}
		
		return dissidios;
	}
	
	private List<Encargo> criaEncargos(Collection<Encargo> encargos, Collection<Encargo> encargosNovos) {
		
		List<Encargo> atuais = new ArrayList<Encargo>();
		if (encargos != null)
			atuais.addAll(encargos);
		
		List<Encargo> novos = new ArrayList<Encargo>();
		if (encargosNovos != null)
			novos.addAll(encargosNovos);
		
		List<Encargo> ret = new ArrayList<Encargo>();
		
		Collection<Empresa> empresas = empresaDao.filtrar(FiltroEmpresa.newInstance(-1));
		
		for (Empresa empresa: empresas) {				
			
			for (Sindicato sindicato: sindicatoDao.filtrar(FiltroSindicato.newInstance(-1))) {
				
				Encargo encargo = new Encargo();
				encargo.setEmpresa(empresa);
				encargo.setSindicato(sindicato);
				
				if (novos.contains(encargo))
					encargo = novos.get(novos.indexOf(encargo));				
				else if (atuais.contains(encargo))
					encargo = atuais.get(atuais.indexOf(encargo));				
					
				ret.add(encargo);
			}		
			
		}
		
		return ret;
	}
	
}
