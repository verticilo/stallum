package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.ContaGerencialDao;
import stallum.dto.FiltroContaGerencial;
import stallum.dto.OrdenacaoContaGerencial;
import stallum.dto.Paginacao;
import stallum.enums.ClasseContaGerencial;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.ContaGerencial;
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
public class PlanoContasController extends AbstractController {

	private Logger log = Logger.getLogger(PlanoContasController.class);
	
	private final ContaGerencialDao dao;

	public PlanoContasController(ContaGerencialDao dao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
	}
	
	@Get @Path({"/planoContas/novo", "/planoContas/{contaGerencial.id}/editar"})
	public ContaGerencial formContaGerencial(ContaGerencial contaGerencial) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (contaGerencial == null)
				contaGerencial = new ContaGerencial();
			else if (contaGerencial.getId() != null && contaGerencial.getVersao() == null)
				contaGerencial = consultarPorId(contaGerencial.getId());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaGerencial.falha.carregar"));
			result.redirectTo("/planoContas/listar");
		}
		
		return contaGerencial;
	}
	
	@Get @Post @Path("/planoContas/listar")
	public void listarPlanoContas(FiltroContaGerencial filtro, Long idObra) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
					
			if (filtro == null || filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroContaGerencial.newInstance(getRegistrosPagina());

			for (OrdenacaoContaGerencial ordem: OrdenacaoContaGerencial.values())
				filtro.addOrdencacao(ordem);

			filtro.getPaginacao().atualizar();
		
			
			result.include("filtro", filtro);
			result.include("listaPlanoContas", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarPlanoContas.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Get @Path("/planoContas/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		ContaGerencial contaGerencial = consultarPorId(id);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {		
			
			dao.excluir(contaGerencial);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("contaGerencial.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaGerencial.falha.remover", id));
		}
		
		result.redirectTo("/planoContas/listar");
	}

	
	@Put @Post @Path("/planoContas")
	public void salvar(final ContaGerencial contaGerencial, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(contaGerencial.getNumero()), "contaGerencial.numero", "obrigatorio");
			that(preenchido(contaGerencial.getNome()), "contaGerencial.nome", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formContaGerencial(contaGerencial);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			contaGerencial.setClasse(ClasseContaGerencial.get(contaGerencial.getNumero().substring(0, 1)));
			
			boolean novo = contaGerencial.getId() == null;
			
			if (novo)
				dao.incluir(contaGerencial);								
			else
				dao.alterar(contaGerencial);
						
			tx.commit();
			
			sessao.addMensagem(getMessage("formContaGerencial.sucesso"));
			if (novo)
				result.redirectTo("/planoContas/novo");
			else
				result.redirectTo("/planoContas/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaGerencial.falha.salvar"));
			result.redirectTo(this).formContaGerencial(contaGerencial);
		}
		
	}
		
	private ContaGerencial consultarPorId(Long id) {
		
		ContaGerencial contaGerencial = null;
		try {
			
			contaGerencial = dao.consultarPorId(id);

			if (contaGerencial == null)
				validator.add(new ValidationMessage(getMessage("contaGerencial.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaGerencial.falha.recuperar", id));
			result.forwardTo("/planoContas/listar");
		}
		
		return contaGerencial;
	}
	
}
