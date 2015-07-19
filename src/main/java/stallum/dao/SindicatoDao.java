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

import stallum.dto.FiltroSindicato;
import stallum.dto.OrdenacaoSindicato;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Sindicato;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class SindicatoDao extends AbstractDao {

	public static final String ALIAS = "sind";
	
	public SindicatoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Sindicato.class, ALIAS);
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar();
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.groupProperty(ALIAS + ".id").as("id"))
				.add(Projections.groupProperty(ALIAS + ".nome").as("nome"))
				.add(Projections.groupProperty(ALIAS + ".dataDissidio").as("dataDissidio"))
				.add(Projections.groupProperty(ALIAS + ".salarioMinimo").as("salarioMinimo"))
				.add(Projections.groupProperty(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				.add(Projections.groupProperty(ALIAS + ".valorBeneficio").as("valorBeneficio"))
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(Sindicato.class, "id");
		return transformer;
	}
			
	
	/**
	 * Conulta a Sindicato no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Sindicato consultarPorId(Long idSindicato) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idSindicato));
		
		Sindicato result = (Sindicato) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Sindicato pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Sindicato buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Sindicato result = (Sindicato) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Sindicato> filtrar(FiltroSindicato filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Sindicato> result = (Collection<Sindicato>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroSindicato filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoSindicato.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroSindicato filtro) {

		if (filtro == null)
			return;
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
