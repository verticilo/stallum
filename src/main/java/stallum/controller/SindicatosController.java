package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.SindicatoDao;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoSindicato;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
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
public class SindicatosController extends AbstractController {

	private Logger log = Logger.getLogger(SindicatosController.class);
	
	private final SindicatoDao dao;

	public SindicatosController(SindicatoDao dao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
	}
	
	@Get @Path({"/sindicatos/novo", "/sindicatos/{sindicato.id}/editar"})
	public Sindicato formSindicato(Sindicato sindicato) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (sindicato == null)
				sindicato = new Sindicato();
			else if (sindicato.getId() != null && sindicato.getVersao() == null)
				sindicato = consultarPorId(sindicato.getId());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formSindicato.falha.carregar"));
			result.redirectTo(this).formSindicato(sindicato);
		}
		
		return sindicato;
	}
	
	@Get @Post @Path("/sindicatos/listar")
	public void listarSindicatos(FiltroSindicato filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroSindicato.newInstance(getRegistrosPagina());

			for (OrdenacaoSindicato ordem: OrdenacaoSindicato.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaSindicatos", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarSindicatos.falha.listar"));
			result.redirectTo("/");
		}	
	}
		
	@Put @Post @Path("/sindicatos")
	public void salvar(final Sindicato sindicato, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(sindicato.getNome()), "sindicato.nome", "obrigatorio");
			that(preenchido(sindicato.getDataDissidio()), "sindicato.dataDissidio", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).formSindicato(sindicato);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			boolean novo = true;
			
			if (sindicato.getId() == null) {
				dao.incluir(sindicato);								
			} else {
				dao.alterar(sindicato);
				novo = false;
			}
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formSindicato.sucesso"));
			
			if (novo)
				result.redirectTo("/sindicatos/novo");
			else
				result.redirectTo("/sindicatos/listar");				
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formSindicato.falha.salvar"));
			result.redirectTo(this).formSindicato(sindicato);
		}
		
	}
	
	@Get @Path("/sindicatos/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.OPERADOR);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Sindicato sindicato = consultarPorId(id);
			
			dao.excluir(sindicato);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("sindicato.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("sindicato.falha.remover", id));
		}
		
		result.redirectTo("/sindicatos/listar");
	}

	private Sindicato consultarPorId(Long id) {
		
		Sindicato sindicato = null;
		try {
			
			sindicato = dao.consultarPorId(id);

			if (sindicato == null)
				validator.add(new ValidationMessage(getMessage("sindicato.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("sindicato.falha.recuperar", id));
			result.forwardTo("/sindicatos/listar");
		}
		
		return sindicato;
	}
	
}
