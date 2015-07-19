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

import stallum.dto.FiltroFuncao;
import stallum.dto.OrdenacaoFuncao;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Dissidio;
import stallum.modelo.Funcao;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FuncaoDao extends AbstractDao {

	public static final String ALIAS 			= "func";
	public static final String ALIAS_SINDICATO	= "sind";
	public static final String ALIAS_DISSIDIO 	= "diss";
	
	public FuncaoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Funcao.class, ALIAS)
				.createAlias(ALIAS + ".sindicato", ALIAS_SINDICATO)
				.createAlias(ALIAS + ".dissidios", ALIAS_DISSIDIO, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar();
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.groupProperty(ALIAS + ".id").as("id"))
				.add(Projections.groupProperty(ALIAS + ".nome").as("nome"))
				.add(Projections.groupProperty(ALIAS + ".salario").as("salario"))
				.add(Projections.groupProperty(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.groupProperty(ALIAS_SINDICATO + ".id").as("sindicato.id"))
				.add(Projections.groupProperty(ALIAS_SINDICATO + ".nome").as("sindicato.nome"))
				.add(Projections.groupProperty(ALIAS_SINDICATO + ".alteradoEm").as("sindicato.alteradoEm"))

				.add(Projections.max(ALIAS_DISSIDIO + ".data").as("dissidios.Dissidio.data"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				.add(Projections.groupProperty(ALIAS_DISSIDIO + ".id").as("dissidios.Dissidio.id"))
				.add(Projections.groupProperty(ALIAS_DISSIDIO + ".percReajuste").as("dissidios.Dissidio.percReajuste"))
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(Funcao.class, "id");
		transformer.addGroup("Dissidio", Dissidio.class, "dissidios.Dissidio.id");
		return transformer;
	}
			
	
	/**
	 * Conulta a Funcao no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Funcao consultarPorId(Long idFuncao) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idFuncao));
		
		Funcao result = (Funcao) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Funcao pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Funcao buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Funcao result = (Funcao) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Funcao> filtrar(FiltroFuncao filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Funcao> result = (Collection<Funcao>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroFuncao filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoFuncao.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroFuncao filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getSindicato() != null)
			criteria.add(Restrictions.eq(ALIAS_SINDICATO + ".id", filtro.getSindicato().getId()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
