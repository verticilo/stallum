package stallum.dao;

import java.util.Collection;
import java.util.Date;

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

import stallum.dto.FiltroDissidio;
import stallum.dto.OrdenacaoDissidio;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Dissidio;
import stallum.modelo.Sindicato;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DissidioDao extends AbstractDao {

	public static final String ALIAS 			= "diss";
	public static final String ALIAS_FUNCAO 	= "func";
	public static final String ALIAS_SINDICATO	= "sind";
	
	public DissidioDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Dissidio.class, ALIAS)
				.createAlias(ALIAS + ".funcao", ALIAS_FUNCAO)
				.createAlias(ALIAS_FUNCAO + ".sindicato", ALIAS_SINDICATO)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar();
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.min(ALIAS + ".id").as("id"))
				.add(Projections.groupProperty(ALIAS + ".data").as("data"))
				.add(Projections.groupProperty(ALIAS_SINDICATO + ".id").as("funcao.sindicato.id"))
				.add(Projections.groupProperty(ALIAS_SINDICATO + ".nome").as("funcao.sindicato.nome"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				.add(Projections.property(ALIAS + ".percReajuste").as("percReajuste"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_FUNCAO + ".id").as("funcao.id"))
				.add(Projections.property(ALIAS_FUNCAO + ".alteradoEm").as("funcao.alteradoEm"))
				
				.add(Projections.property(ALIAS_SINDICATO + ".id").as("funcao.sindicato.id"))
				.add(Projections.property(ALIAS_SINDICATO + ".nome").as("funcao.sindicato.nome"))
				.add(Projections.property(ALIAS_SINDICATO + ".valorBeneficio").as("beneficio"))
				.add(Projections.property(ALIAS_SINDICATO + ".alteradoEm").as("funcao.sindicato.alteradoEm"))
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(Dissidio.class, "id");
		return transformer;
	}
		
	/**
	 * Busca o Dissidio pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Dissidio
	 * @throws HibernateException
	 */
	public Dissidio buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Dissidio result = (Dissidio) criteria.uniqueResult();
		
		return result;
		
	}

	@SuppressWarnings("unchecked")
	public Collection<Dissidio> consultar(Sindicato sindicato, Date data) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".data", data));
		criteria.add(Restrictions.eq(ALIAS_SINDICATO + ".id", sindicato.getId()));
		
		Collection<Dissidio> result = (Collection<Dissidio>) criteria.list();
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public Collection<Dissidio> filtrar(FiltroDissidio filtro) throws HibernateException {
		
		Criteria criteria = null;
		
		if (filtro.getPaginacao() == null) {
			criteria = getCriteriaConsultar();
			criteria.setProjection(getProjectionConsultar());			
		} else {
			criteria = getCriteriaListar();
			criteria.setProjection(getProjectionListar());		
		}
		criteria.setResultTransformer(getTransformer());			
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Dissidio> result = (Collection<Dissidio>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroDissidio filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoDissidio.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroDissidio filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getSindicato() != null)
			criteria.add(Restrictions.eq(ALIAS_FUNCAO + ".sindicato", filtro.getSindicato()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS_FUNCAO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_FUNCAO + ".sindicao", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
