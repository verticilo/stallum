package stallum.controller;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.CentroCustoDao;
import stallum.dao.DespesaDao;
import stallum.dao.ObraDao;
import stallum.dto.FiltroCentroCusto;
import stallum.dto.FiltroDespesa;
import stallum.dto.FiltroObra;
import stallum.dto.OrdenacaoDespesa;
import stallum.dto.Paginacao;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoUsuario;
import stallum.modelo.CentroCusto;
import stallum.modelo.Despesa;
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
public class DespesasController extends AbstractController {

	private Logger log = Logger.getLogger(DespesasController.class);
	
	private final DespesaDao dao;
	private final ObraDao obraDao;
	private final CentroCustoDao centroCustoDao;

	public DespesasController(DespesaDao dao, ObraDao obraDao, CentroCustoDao centroCustoDao,
			Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.obraDao = obraDao;
		this.centroCustoDao = centroCustoDao;
	}
	
	@Get @Path({"/despesas/nova", "/despesas/{despesa.id}/editar"})
	public Despesa formDespesa(Despesa despesa) {

		validaSessao(PerfilUsuario.OPERADOR);
			
		try {
			
			if (despesa == null)
				despesa = new Despesa();
			else if (despesa.getId() != null && despesa.getVersao() == null)
				despesa = consultarPorId(despesa.getId());
			
			result.include("obras", obraDao.filtrar(FiltroObra.newInstance(-1)));
			result.include("centrosCusto", centroCustoDao.filtrar(FiltroCentroCusto.newInstance(-1)));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDespesa.falha.carregar"));
			result.redirectTo("/despesas/listar");
		}
		
		return despesa;
	}
	
	@Get @Post @Path({"/despesas/listar", "/despesas/obra/{idObra}/listar"})
	public void listarDespesas(FiltroDespesa filtro, Long idObra) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
					
			if (filtro == null || filtro.getPaginacao() == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroDespesa.newInstance(getRegistrosPagina());

			for (OrdenacaoDespesa ordem: OrdenacaoDespesa.values())
				filtro.addOrdencacao(ordem);

			if (filtro.getObra() != null) {
				filtro.setObra(buscarObra(filtro.getObra().getId()));
			} else if (idObra != null) {
				Obra obra = new Obra();
				obra.setId(idObra);
				filtro.setObra(obra);
			}
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaDespesas", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarDespesas.falha.listar"));
			result.redirectTo("/");
		}	
	}
	
	@Get @Path("/despesas/{id}/remover")
	public void remover(Long id) {
				
		validaSessao(PerfilUsuario.OPERADOR);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			Despesa despesa = consultarPorId(id);
			
			dao.excluir(despesa);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("despesa.sucesso.remover"));
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("despesa.falha.remover", id));
		}
		
		result.redirectTo("/despesas/listar");
	}

	
	@Put @Post @Path("/despesas")
	public void salvar(final Despesa despesa, String acao) {
		
		validator.checking(new Validations() {{
			that((preenchido(despesa.getObra()) && preenchido(despesa.getObra().getId())) ||
				 (preenchido(despesa.getCentroCusto()) && preenchido(despesa.getCentroCusto().getId())), "despesa.obraCCusto", "obrigatorio");
			that(preenchido(despesa.getDescricao()), "despesa.descricao", "obrigatoria");
			that(preenchido(despesa.getValor()), "despesa.valor", "obrigatorio");
			that(preenchido(despesa.getData()), "despesa.data", "obrigatoria");
		}});
		validator.onErrorRedirectTo(this).formDespesa(despesa);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {
			
			if (despesa.getObra() != null)
				despesa.setObra(buscarObra(despesa.getObra().getId()));
			
			if (despesa.getCentroCusto() != null)
				despesa.setCentroCusto(buscarCentroCusto(despesa.getCentroCusto().getId()));
			
			if (despesa.getId() == null)
				dao.incluir(despesa);								
			else
				dao.alterar(despesa);
						
			tx.commit();
			
			sessao.addMensagem(getMessage("formDespesa.sucesso"));
			result.redirectTo("/despesas/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formDespesa.falha.salvar"));
			result.redirectTo(this).formDespesa(despesa);
		}
		
	}
		
	private Despesa consultarPorId(Long id) {
		
		Despesa despesa = null;
		try {
			
			despesa = dao.consultarPorId(id);

			if (despesa == null)
				validator.add(new ValidationMessage(getMessage("despesa.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("despesa.falha.recuperar", id));
			result.forwardTo("/despesas/listar");
		}
		
		return despesa;
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
			sessao.addErro(getMessage("despesa.falha.recuperar", id));
			result.forwardTo("/despesas/listar");
		}
		
		return obra;
	}
	
	private CentroCusto buscarCentroCusto(Long id) {
		
		if (id == null)
			return null;
		
		CentroCusto centroCusto = null;
		
		try {
			
			centroCusto = centroCustoDao.buscarPorId(id);
			
			if (centroCusto == null)
				validator.add(new ValidationMessage(getMessage("centroCusto.validacao.naoExiste", id), "semCategoria"));
			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("despesa.falha.recuperar", id));
			result.forwardTo("/despesas/listar");
		}
		
		return centroCusto;
	}
	
}
