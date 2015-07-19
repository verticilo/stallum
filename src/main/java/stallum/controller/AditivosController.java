package stallum.controller;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.AditivoDao;
import stallum.dao.ObraDao;
import stallum.dto.FiltroAditivo;
import stallum.dto.FiltroObra;
import stallum.dto.OrdenacaoAditivo;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.Aditivo;
import stallum.modelo.Obra;
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
public class AditivosController extends AbstractController {

	private Logger log = Logger.getLogger(AditivosController.class);
	
	private final AditivoDao dao;
	private final ObraDao obraDao;

	public AditivosController(AditivoDao dao, ObraDao obraDao, Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.obraDao = obraDao;
	}
	
	@Get @Path({"/aditivos/novo", "/aditivos/{aditivo.id}/editar"})
	public Aditivo formAditivo(Aditivo aditivo) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (aditivo == null)
				aditivo = new Aditivo();
			else if (aditivo.getId() != null && aditivo.getVersao() == null)
				aditivo = consultarPorId(aditivo.getId());
			
			result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formAditivo.falha.carregar"));
			result.redirectTo("/aditivos/listar");
		}
		
		return aditivo;
	}
	
	@Get @Post @Path({"/aditivos/listar", "/aditivos/obra/{idObra}/listar"})
	public void listarAditivos(FiltroAditivo filtro, Long idObra) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
					
			if (filtro == null || filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroAditivo.newInstance(getRegistrosPagina());

			for (OrdenacaoAditivo ordem: OrdenacaoAditivo.values())
				filtro.addOrdencacao(ordem);

			if (filtro.getObra() != null)
				filtro.setObra(buscarObra(filtro.getObra().getId()));
			
			filtro.getPaginacao().atualizar();
		
			if (idObra != null) {
				Obra obra = new Obra();
				obra.setId(idObra);
				filtro.setObra(obra);
			}
			
			result.include("filtro", filtro);
			result.include("listaAditivos", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarAditivos.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Get @Path("/aditivos/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.ADMIN);
		
		Aditivo aditivo = consultarPorId(id);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {		
			
			dao.excluir(aditivo);
			
			// Atualiza a numeracao dos aditivos para cada obra
			FiltroAditivo filtro = FiltroAditivo.newInstance(-1);
			filtro.setOrdenacao(OrdenacaoAditivo.DATA);
			Collection<Aditivo> aditivos = dao.filtrar(filtro);
			Obra obra = null;
			Integer cont = 0;
			for (Aditivo adt: aditivos) {
				if (!adt.getObra().equals(obra)) {
					cont = 1;
					obra = adt.getObra();
				}
				adt.setNumero("000".substring(0, 3 - cont.toString().length()) + cont.toString());
				dao.alterar(adt);
				cont++;
			}
			
			tx.commit();
			
			sessao.addMensagem(getMessage("aditivo.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("aditivo.falha.remover", id));
		}
		
		result.redirectTo("/aditivos/listar");
	}

	
	@Put @Post @Path("/aditivos")
	public void salvar(final Aditivo aditivo, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(aditivo.getObra()) && preenchido(aditivo.getObra().getId()), "aditivo.obra", "obrigatoria");
			that(preenchido(aditivo.getDescricao()), "aditivo.descricao", "obrigatoria");
			that(preenchido(aditivo.getValor()), "aditivo.valor", "obrigatorio");
			that(preenchido(aditivo.getData()), "aditivo.data", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).formAditivo(aditivo);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			aditivo.setObra(obraDao.buscarPorId(aditivo.getObra().getId()));
			
			aditivo.setNumero(dao.proximoNumero(aditivo.getObra()));
			
			if (aditivo.getId() == null)
				dao.incluir(aditivo);								
			else
				dao.alterar(aditivo);
						
			tx.commit();
			
			sessao.addMensagem(getMessage("formAditivo.sucesso"));
			result.redirectTo("/aditivos/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formAditivo.falha.salvar"));
			result.redirectTo(this).formAditivo(aditivo);
		}
		
	}
		
	private Aditivo consultarPorId(Long id) {
		
		Aditivo aditivo = null;
		try {
			
			aditivo = dao.consultarPorId(id);

			if (aditivo == null)
				validator.add(new ValidationMessage(getMessage("aditivo.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("aditivo.falha.recuperar", id));
			result.forwardTo("/aditivos/listar");
		}
		
		return aditivo;
	}
	
	private Obra buscarObra(Long id) {

		if (id == null)
			return null;
		
		Obra obra = null;
		
		try {
			
			obra = obraDao.buscarPorId(id);

			if (obra == null)
				validator.add(new ValidationMessage(getMessage("obra.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("aditivo.falha.recuperar", id));
			result.forwardTo("/aditivos/listar");
		}
		
		return obra;
	}
	
}
