package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.FuncaoDao;
import stallum.dao.SindicatoDao;
import stallum.dto.FiltroFuncao;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoFuncao;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.Dissidio;
import stallum.modelo.Funcao;
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
public class FuncoesController extends AbstractController {

	private Logger log = Logger.getLogger(FuncoesController.class);
	
	private final FuncaoDao dao;
	private final SindicatoDao sindicatoDao;

	public FuncoesController(FuncaoDao dao, SindicatoDao sindicatoDao, 
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.sindicatoDao = sindicatoDao;
	}
	
	@Get @Path({"/funcoes/nova", "/funcoes/{funcao.id}/editar"})
	public Funcao formFuncao(Funcao funcao) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (funcao == null)
				funcao = new Funcao();
			else if (funcao.getId() != null && funcao.getVersao() == null)
				funcao = consultarPorId(funcao.getId());
			
			result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formFuncao.falha.carregar"));
			result.redirectTo(this).formFuncao(funcao);
		}
		
		return funcao;
	}
	
	@Get @Post @Path("/funcoes/listar")
	public void listarFuncoes(FiltroFuncao filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroFuncao.newInstance(getRegistrosPagina());

			for (OrdenacaoFuncao ordem: OrdenacaoFuncao.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaFuncoes", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarFuncoes.falha.listar"));
			result.redirectTo("/");
		}	
	}
		
	@Put @Post @Path("/funcoes")
	public void salvar(final Funcao funcao, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(funcao.getNome()), "funcao.nome", "obrigatorio");
			that(preenchido(funcao.getSalario()), "funcao.salario", "obrigatorio");
			that(preenchido(funcao.getSindicato().getId()), "funcao.sindicato", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formFuncao(funcao);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			boolean novo = true;
			
			funcao.setSindicato(sindicatoDao.buscarPorId(funcao.getSindicato().getId()));
			
			if (funcao.getId() == null) {
				
				if (preenchido(funcao.getDataUltimoDissidio()) || preenchido(funcao.getPercUltimoDissidio())) {
					Dissidio dissidio = new Dissidio();
					dissidio.setData(funcao.getDataUltimoDissidio());
					dissidio.setPercReajuste(funcao.getPercUltimoDissidio());
					dissidio.setFuncao(funcao);
					funcao.addDissidio(dissidio);
					dao.incluir(dissidio);
				}
				
				dao.incluir(funcao);								
			} else {
				dao.alterar(funcao);
				novo = false;
			}
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formFuncao.sucesso"));
			
			if (novo)
				result.redirectTo("/funcoes/nova");
			else
				result.redirectTo("/funcoes/listar");				
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formFuncao.falha.salvar"));
			result.redirectTo(this).formFuncao(funcao);
		}
		
	}
	
	@Get @Path("/funcoes/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.OPERADOR);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Funcao funcao = consultarPorId(id);
			
			dao.excluir(funcao);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("funcao.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("funcao.falha.remover", id));
		}
		
		result.redirectTo("/funcoes/listar");
	}

	private Funcao consultarPorId(Long id) {
		
		Funcao funcao = null;
		try {
			
			funcao = dao.consultarPorId(id);

			if (funcao == null)
				validator.add(new ValidationMessage(getMessage("funcao.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("funcao.falha.recuperar", id));
			result.forwardTo("/funcoes/listar");
		}
		
		return funcao;
	}
	
}
