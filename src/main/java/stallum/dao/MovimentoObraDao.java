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

import stallum.dto.FiltroMovimentoObra;
import stallum.dto.OrdenacaoMovimentoObra;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.MovimentoObra;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class MovimentoObraDao extends AbstractDao {

	public static final String ALIAS		= "movi";
	public static final String ALIAS_OBRA	= "obra";
	
	public MovimentoObraDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(MovimentoObra.class, ALIAS)
				.createAlias(ALIAS + ".obra", ALIAS_OBRA)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()
				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				.add(Projections.property(ALIAS + ".novoStatus").as("novoStatus"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				.add(Projections.property(ALIAS + ".motivo").as("motivo"))
				
				.add(Projections.property(ALIAS_OBRA + ".id").as("obra.id"))
				.add(Projections.property(ALIAS_OBRA + ".nome").as("obra.nome"))
				.add(Projections.property(ALIAS_OBRA + ".alteradoEm").as("obra.alteradoEm"))
				
				;
	}
		
	
	/**
	 * Conulta a Movimento no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public MovimentoObra consultarPorId(Long idMovimento) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(MovimentoObra.class));
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idMovimento));
		
		MovimentoObra result = (MovimentoObra) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Movimento pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public MovimentoObra buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(MovimentoObra.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		MovimentoObra result = (MovimentoObra) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<MovimentoObra> filtrar(FiltroMovimentoObra filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(MovimentoObra.class, "id"));
	
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<MovimentoObra> result = (Collection<MovimentoObra>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroMovimentoObra filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoMovimentoObra.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
				criteria.addOrder(Order.asc(filtro.getOrdenacao().getNomeCampo()));
			} else {
				criteria.addOrder(Order.desc(filtro.getOrdenacao().getNomeCampo()));
			}
			// Sempre ordenando por data do movimento
			criteria.addOrder(Order.asc(OrdenacaoMovimentoObra.DATA.getNomeCampo()));
		}
		
		if (filtro.getPaginacao() != null) {
			criteria.setFirstResult(filtro.getPaginacao().getPrimeiroRegistro());
			criteria.setMaxResults(filtro.getPaginacao().getRegistrosPorPagina());
		}
		
	}

	private void prepararFiltro(Criteria criteria, FiltroMovimentoObra filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getObra() != null && filtro.getObra().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_OBRA + ".id", filtro.getObra().getId()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS_OBRA + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".motivo", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
