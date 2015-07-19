package stallum.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.DadosGeraisDao;
import stallum.dao.EmpresaDao;
import stallum.dao.SindicatoDao;
import stallum.dto.FiltroDadosGerais;
import stallum.dto.FiltroEmpresa;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoDadosGerais;
import stallum.dto.Paginacao;
import stallum.enums.DiaSemana;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.DadosGerais;
import stallum.modelo.Empresa;
import stallum.modelo.Encargo;
import stallum.modelo.HorarioTrabalho;
import stallum.modelo.Sindicato;
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

@Resource
public class DadosGeraisController extends AbstractController {

	private Logger log = Logger.getLogger(DadosGeraisController.class);
	
	private final DadosGeraisDao dao;
	private final EmpresaDao empresasDao;
	private final SindicatoDao sindicatoDao;

	public DadosGeraisController(DadosGeraisDao dao, EmpresaDao empresaDao, SindicatoDao sindicatoDao, 
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.empresasDao = empresaDao;
		this.sindicatoDao = sindicatoDao;
	}

	@Get @Path({"/dadosGerais/novo", "/dadosGerais/{dadosGerais.id}/editar"})
	public DadosGerais formDadosGerais(DadosGerais dadosGerais) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			Collection<Encargo> encargos = new ArrayList<Encargo>();
			Collection<HorarioTrabalho> horarios = new ArrayList<HorarioTrabalho>();
			
			if (dadosGerais == null) {	// Novo
				dadosGerais = new DadosGerais();				
				encargos = criaEncargos();				
				horarios = criaHorarios();				
			} else if (dadosGerais.getId() != null && dadosGerais.getVersao() == null) { // Editar
				dadosGerais = consultarPorId(dadosGerais.getId());
				encargos = criaEncargos(dadosGerais.getEncargos());
				horarios = criaHorarios(dadosGerais.getHorarios());
			} else { // Erro
				encargos = dadosGerais.getEncargos();
				horarios = dadosGerais.getHorarios();
			}
			
			result.include("encargos", encargos);
			result.include("horarios", horarios);
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDadosGerais.falha.carregar"));
			result.redirectTo("/dadosGerais/listar");
		}
		
		return dadosGerais;
	}

	@Put @Post @Path("/dadosGerais")
	public void salvar(final DadosGerais dadosGerais, List<Encargo> encargos, List<HorarioTrabalho> horarios) {
		
		dadosGerais.setEncargos(encargos);
		dadosGerais.setHorarios(horarios);
		
		validator.checking(new Validations() {{
			that(preenchido(dadosGerais.getData()), "dadosGerais.data", "obrigatoria");
			that(preenchido(dadosGerais.getDescricao()), "dadosGerais.descricao", "obrigatoria");
			that(preenchido(dadosGerais.getTetoSalFamilia()), "dadosGerais.tetoSalFamilia", "obrigatorio");
			that(preenchido(dadosGerais.getAdicDependente()), "dadosGerais.adicDependente", "obrigatorio");
			that(preenchido(dadosGerais.getPercAnuenio()), "dadosGerais.percAnuenio", "obrigatorio");
			that(preenchido(dadosGerais.getValeRefeicao()), "dadosGerais.valeRefeicao", "obrigatorio");
			that(preenchido(dadosGerais.getJornadaDiaMin()), "dadosGerais.jornadaDiaMin", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formDadosGerais(dadosGerais);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			if (dadosGerais.getId() == null) {
				encargos = criaEncargos(encargos);
				horarios = criaHorarios(horarios);
			} else {
				DadosGerais dadosBanco = consultarPorId(dadosGerais.getId());
				encargos = criaEncargos(dadosBanco.getEncargos(), encargos);
				horarios = criaHorarios(dadosBanco.getHorarios(), horarios);
			}
			
			for (Encargo encargo: encargos) {
				encargo.setDadosGerais(dadosGerais);
				if (encargo.getId() == null)
					dao.incluir(encargo);
				else
					dao.alterar(encargo);
			}
			
			for (HorarioTrabalho horario: horarios) {
				horario.setDadosGerais(dadosGerais);
				if (dadosGerais.getId() == null)
					dao.incluir(horario);
				else
					dao.alterar(horario);
					
			}
			
			dadosGerais.setEncargos(encargos);
			dadosGerais.setHorarios(horarios);		
					
			if (dadosGerais.getId() == null)
				dao.incluir(dadosGerais);								
			else
				dao.alterar(dadosGerais);
						
			tx.commit();
			
			sessao.addMensagem(getMessage("formDadosGerais.sucesso"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDadosGerais.falha.salvar"));
		}
		
		result.redirectTo("dadosGerais/listar");
		
	}
	
	@Get @Path("/dadosGerais/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			DadosGerais dadosGerais = consultarPorId(id);
			
			for (Encargo encargo: dadosGerais.getEncargos())
				dao.excluir(encargo);
			
			for (HorarioTrabalho horario: dadosGerais.getHorarios())
				dao.excluir(horario);
			
			dao.excluir(dadosGerais);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("dadosGerais.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("dadosGerais.falha.remover", id));
		}
		
		result.redirectTo("/dadosGerais/listar");
	}
	
	private DadosGerais consultarPorId(Long id) {
		
		DadosGerais dadosGerais = null;
		try {
			
			dadosGerais = dao.consultarPorId(id);

			if (dadosGerais == null)
				validator.add(new ValidationMessage(getMessage("dadosGerais.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("dadosGerais.falha.recuperar", id));
			result.forwardTo(this).listarDadosGerais(null);
		}
		
		return dadosGerais;
	}
	
	@Get @Post @Path("/dadosGerais/listar")
	public void listarDadosGerais(FiltroDadosGerais filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroDadosGerais.newInstance(getRegistrosPagina());

			for (OrdenacaoDadosGerais ordem: OrdenacaoDadosGerais.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaDadosGerais", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarDadosGerais.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	
	private List<HorarioTrabalho> criaHorarios() {
		return this.criaHorarios(null, null);
	}
	private List<HorarioTrabalho> criaHorarios(Collection<HorarioTrabalho> horarios) {
		return this.criaHorarios(horarios, null);
	}
	private List<HorarioTrabalho> criaHorarios(Collection<HorarioTrabalho> horarios, Collection<HorarioTrabalho> horariosNovos) {
		
		List<HorarioTrabalho> atuais = new ArrayList<HorarioTrabalho>();
		if (horarios != null)
			atuais.addAll(horarios);
		
		List<HorarioTrabalho> novos = new ArrayList<HorarioTrabalho>();
		if (horariosNovos != null)
			novos.addAll(horariosNovos);
		
		List<HorarioTrabalho> ret = new ArrayList<HorarioTrabalho>();
		
		for (DiaSemana dia: DiaSemana.values()) {
			
			HorarioTrabalho horario = new HorarioTrabalho();
			horario.setDiaSemana(dia);
			
			if (novos.contains(horario))
				horario = novos.get(novos.indexOf(horario));
			else if (atuais.contains(horario))
				horario = atuais.get(atuais.indexOf(horario));

			ret.add(horario);
		}
		
		return ret;
	}

	private List<Encargo> criaEncargos() {
		return this.criaEncargos(null, null);
	}
	private List<Encargo> criaEncargos(Collection<Encargo> encargos) {
		return this.criaEncargos(encargos, null);
	}
	private List<Encargo> criaEncargos(Collection<Encargo> encargos, Collection<Encargo> encargosNovos) {
		
		List<Encargo> atuais = new ArrayList<Encargo>();
		if (encargos != null)
			atuais.addAll(encargos);
		
		List<Encargo> novos = new ArrayList<Encargo>();
		if (encargosNovos != null)
			novos.addAll(encargosNovos);
		
		List<Encargo> ret = new ArrayList<Encargo>();
		
		Collection<Empresa> empresas = empresasDao.filtrar(FiltroEmpresa.newInstance(-1));
		
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
