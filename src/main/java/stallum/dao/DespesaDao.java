package stallum.dao;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;

import stallum.dto.FiltroDespesa;
import stallum.dto.OrdenacaoDespesa;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Despesa;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DespesaDao extends AbstractDao {

	public static final String ALIAS 		= "desp";
	public static final String ALIAS_OBRA 	= "obra";
	public static final String ALIAS_CCUSTO	= "ccus";
	
	public DespesaDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Despesa.class, ALIAS)
				.createAlias(ALIAS + ".obra", ALIAS_OBRA, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS + ".centroCusto", ALIAS_CCUSTO, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".descricao").as("descricao"))
				.add(Projections.property(ALIAS + ".valor").as("valor"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				
				.add(Projections.property(ALIAS_OBRA + ".id").as("obra.id"))
				.add(Projections.property(ALIAS_OBRA + ".nome").as("obra.nome"))
				
				.add(Projections.property(ALIAS_CCUSTO + ".id").as("centroCusto.id"))
				.add(Projections.property(ALIAS_CCUSTO + ".nome").as("centroCusto.nome"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()				
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))				
				.add(Projections.property(ALIAS_OBRA + ".alteradoEm").as("obra.alteradoEm"))
				.add(Projections.property(ALIAS_CCUSTO + ".alteradoEm").as("centroCusto.alteradoEm"))
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(Despesa.class, "id");
		//transformer.addGroup("Dissidio", Dissidio.class, "dissidios.Dissidio.id");
		return transformer;
	}
			
	
	/**
	 * Conulta a Despesa no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Despesa consultarPorId(Long idDespesa) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idDespesa));
		
		Despesa result = (Despesa) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Despesa pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Despesa buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Despesa result = (Despesa) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Despesa> filtrar(FiltroDespesa filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Despesa> result = (Collection<Despesa>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroDespesa filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoDespesa.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
				criteria.addOrder(Order.asc(filtro.getOrdenacao().getNomeCampo()));
			} else {
				criteria.addOrder(Order.desc(filtro.getOrdenacao().getNomeCampo()));
			}
		}
		
		if (filtro.getPaginacao() != null) {
			criteria.setFirstResult(filtro.getPaginacao().getPrimeiroRegistro());
			criteria.setMaxResults(filtro.getPaginacao().getRegistrosPorPagina());
		}
		
	}

	private void prepararFiltro(Criteria criteria, FiltroDespesa filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getObra() != null && filtro.getObra().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_OBRA + ".id", filtro.getObra().getId()));
		
		if (filtro.getCentroCusto() != null && filtro.getCentroCusto().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_CCUSTO + ".id", filtro.getCentroCusto().getId()));
		
		if (filtro.getDataDe() != null)
			criteria.add(Restrictions.ge(ALIAS + ".data", filtro.getDataDe()));
		if (filtro.getDataAte() != null)
			criteria.add(Restrictions.le(ALIAS + ".data", filtro.getDataAte()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".descricao", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_OBRA + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_CCUSTO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
