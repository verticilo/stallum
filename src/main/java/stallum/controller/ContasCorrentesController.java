package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.ContaCorrenteDao;
import stallum.dto.FiltroContaCorrente;
import stallum.dto.OrdenacaoContaCorrente;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.ContaCorrente;
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
public class ContasCorrentesController extends AbstractController {

	private Logger log = Logger.getLogger(ContasCorrentesController.class);
	
	private final ContaCorrenteDao dao;

	public ContasCorrentesController(ContaCorrenteDao dao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
	}
	
	@Get @Path({"/contasCorrentes/novo", "/contasCorrentes/{contaCorrente.id}/editar"})
	public ContaCorrente formContaCorrente(ContaCorrente contaCorrente) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (contaCorrente == null)
				contaCorrente = new ContaCorrente();
			else if (contaCorrente.getId() != null && contaCorrente.getVersao() == null)
				contaCorrente = consultarPorId(contaCorrente.getId());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaCorrente.falha.carregar"));
			result.redirectTo("/contasCorrentes/listar");
		}
		
		return contaCorrente;
	}
	
	@Get @Post @Path("/contasCorrentes/listar")
	public void listarContasCorrentes(FiltroContaCorrente filtro, Long idObra) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
					
			if (filtro == null || filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroContaCorrente.newInstance(getRegistrosPagina());

			for (OrdenacaoContaCorrente ordem: OrdenacaoContaCorrente.values())
				filtro.addOrdencacao(ordem);

			filtro.getPaginacao().atualizar();
		
			
			result.include("filtro", filtro);
			result.include("listaContasCorrentes", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarContasCorrentes.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Get @Path("/contasCorrentes/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		ContaCorrente contaCorrente = consultarPorId(id);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {		
			
			dao.excluir(contaCorrente);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("contaCorrente.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaCorrente.falha.remover", id));
		}
		
		result.redirectTo("/contasCorrentes/listar");
	}

	
	@Put @Post @Path("/contasCorrentes")
	public void salvar(final ContaCorrente contaCorrente, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(contaCorrente.getNomeBanco()), "contaCorrente.nomeBanco", "obrigatorio");
			that(preenchido(contaCorrente.getNumBanco()), "contaCorrente.numBanco", "obrigatorio");
			that(preenchido(contaCorrente.getNumAgencia()), "contaCorrente.numAgencia", "obrigatorio");
			that(preenchido(contaCorrente.getNumConta()), "contaCorrente.numConta", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formContaCorrente(contaCorrente);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			if (contaCorrente.getId() == null)
				dao.incluir(contaCorrente);								
			else
				dao.alterar(contaCorrente);
						
			tx.commit();
			
			sessao.addMensagem(getMessage("formContaCorrente.sucesso"));
			result.redirectTo("/contasCorrentes/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formContaCorrente.falha.salvar"));
			result.redirectTo(this).formContaCorrente(contaCorrente);
		}
		
	}
		
	private ContaCorrente consultarPorId(Long id) {
		
		ContaCorrente contaCorrente = null;
		try {
			
			contaCorrente = dao.consultarPorId(id);

			if (contaCorrente == null)
				validator.add(new ValidationMessage(getMessage("contaCorrente.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("contaCorrente.falha.recuperar", id));
			result.forwardTo("/contasCorrentes/listar");
		}
		
		return contaCorrente;
	}
	
}
