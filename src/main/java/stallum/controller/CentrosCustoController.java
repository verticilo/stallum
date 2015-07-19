package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.CentroCustoDao;
import stallum.dao.SindicatoDao;
import stallum.dto.FiltroCentroCusto;
import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoCentroCusto;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.CentroCusto;
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
public class CentrosCustoController extends AbstractController {

	private Logger log = Logger.getLogger(CentrosCustoController.class);
	
	private final CentroCustoDao dao;
	private final SindicatoDao sindicatoDao;

	public CentrosCustoController(CentroCustoDao dao, SindicatoDao sindicatoDao, 
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.sindicatoDao = sindicatoDao;
	}
	
	@Get @Path({"/centrosCusto/novo", "/centrosCusto/{centroCusto.id}/editar"})
	public CentroCusto formCentroCusto(CentroCusto centroCusto) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {

			if (centroCusto == null)
				centroCusto = new CentroCusto();
			else if (centroCusto.getId() != null && centroCusto.getVersao() == null) {
				centroCusto = buscarPorId(centroCusto.getId());
			}
			
			result.include("sindicatos", sindicatoDao.filtrar(FiltroSindicato.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formCentroCusto.falha.carregar"));
			result.redirectTo("/centrosCusto/listar");
		}
		
		return centroCusto;
	}
	
	@Get @Post @Path("/centrosCusto/listar")
	public void listarCentrosCusto(FiltroCentroCusto filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroCentroCusto.newInstance(getRegistrosPagina());

			for (OrdenacaoCentroCusto ordem: OrdenacaoCentroCusto.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("lista", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarCentrosCusto.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Put @Post @Path("/centrosCusto")
	public void salvar(final CentroCusto centroCusto, String acao) {
		
		validator.checking(new Validations() {{
			that(preenchido(centroCusto.getNome()), "centroCusto.nome", "obrigatorio");
		}});
		validator.onErrorRedirectTo(this).formCentroCusto(centroCusto);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			boolean novo = true;
			
			if (centroCusto.getSindicato().getId() != null)
				centroCusto.setSindicato(sindicatoDao.buscarPorId(centroCusto.getSindicato().getId()));
			
			if (centroCusto.getId() == null) {
				dao.incluir(centroCusto);								
			} else {
				dao.alterar(centroCusto);
				novo = false;
			}
						
			tx.commit();
			
			sessao.addMensagem(getMessage("formCentroCusto.sucesso"));
			
			if (novo)
				result.redirectTo("/centrosCusto/novo");
			else
				result.redirectTo("/centrosCusto/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formCentroCusto.falha.salvar"));
			result.redirectTo(this).formCentroCusto(centroCusto);
		}
		
	}
		
	private CentroCusto buscarPorId(Long id) {
		
		CentroCusto centroCusto = null;
		try {
			
			centroCusto = dao.buscarPorId(id);

			if (centroCusto == null)
				validator.add(new ValidationMessage(getMessage("centroCusto.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("centroCusto.falha.recuperar", id));
			result.forwardTo("/centrosCusto/listar");
		}
		
		return centroCusto;
	}
	
}
