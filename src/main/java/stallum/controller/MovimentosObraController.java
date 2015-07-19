package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.MovimentoObraDao;
import stallum.dao.ObraDao;
import stallum.dto.FiltroMovimentoObra;
import stallum.dto.OrdenacaoMovimentoObra;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.enums.StatusObra;
import stallum.infra.SessaoUsuario;
import stallum.modelo.MovimentoObra;
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
public class MovimentosObraController extends AbstractController {

	private Logger log = Logger.getLogger(MovimentosObraController.class);
	
	private final MovimentoObraDao dao;
	private final ObraDao obraDao;

	public MovimentosObraController(MovimentoObraDao dao, ObraDao obraDao, 
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.obraDao = obraDao;
	}
	
	
	@Get @Path({"/obras/{movimentoObra.obra.id}/movimentosObra/novo", "/movimentosObra/{movimentoObra.id}/editar"})
	public MovimentoObra formMovimentoObra(MovimentoObra movimentoObra) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (movimentoObra.getId() == null) {
				Obra obra = buscarObra(movimentoObra.getObra().getId());
				movimentoObra = new MovimentoObra();
				movimentoObra.setObra(obra);
			} else if (movimentoObra.getId() != null && movimentoObra.getVersao() == null) {
				movimentoObra = consultarPorId(movimentoObra.getId());
			}
			
			result.include("listaStatus", StatusObra.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formMovimentoObra.falha.carregar"));
			result.redirectTo("/obras/listar");
		}
		
		return movimentoObra;
		
	}
		
	@Get @Post @Path("/obras/{filtro.obra.id}/movimentosObra/listar")
	public void listarMovimentosObra(FiltroMovimentoObra filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			Obra obra = buscarObra(filtro.getObra().getId());
			
			if (filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroMovimentoObra.newInstance(getRegistrosPagina());

			for (OrdenacaoMovimentoObra ordem: OrdenacaoMovimentoObra.values())
				filtro.addOrdencacao(ordem);
			
			filtro.setObra(obra);
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaMovimentos", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarMovimentosObra.falha.listar"));
			result.redirectTo("/obras/listar");
		}	
	}
		
	@Put @Post @Path("/movimentosObra")
	public void salvar(final MovimentoObra movimentoObra, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(movimentoObra.getObra().getId()), "movimentoObra.obra", "obrigatorio");
			that(preenchido(movimentoObra.getData()), "movimentoObra.data", "obrigatoria");
			that(preenchido(movimentoObra.getNovoStatus()), "movimentoObra.novoStatus", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formMovimentoObra(movimentoObra);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Obra obra = buscarObra(movimentoObra.getObra().getId());
			
			if (movimentoObra.getNovoStatus() != null) {
				obra.setDataStatus(movimentoObra.getData());
				obra.setStatus(movimentoObra.getNovoStatus());
			}
			
			if (movimentoObra.getId() == null)
				dao.incluir(movimentoObra);
			else
				dao.alterar(movimentoObra);
				
			dao.alterar(obra);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formMovimentoObra.sucesso"));
			result.redirectTo("/obras/" + movimentoObra.getObra().getId() + "/movimentosObra/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formMovimentoObra.falha.salvar"));
			result.redirectTo(this).formMovimentoObra(movimentoObra);
		}
		
	}
		
	private MovimentoObra consultarPorId(Long id) {
		
		MovimentoObra movimentoObra = null;
		try {
			
			movimentoObra = dao.consultarPorId(id);

			if (movimentoObra == null)
				validator.add(new ValidationMessage(getMessage("movimentoObra.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("movimentoObra.falha.recuperar", id));
			result.forwardTo("/obras/listar");
		}
		
		return movimentoObra;
	}
	
	private Obra buscarObra(Long id) {

		if (id == null)
			return null;
		
		Obra obra = null;
		
		try {
			
			obra = obraDao.consultarPorId(id);

			if (obra == null)
				validator.add(new ValidationMessage(getMessage("obra.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("movimentoObra.falha.recuperar", id));
			result.forwardTo("/obras/listar");
		}
		
		return obra;
	}
		
}
