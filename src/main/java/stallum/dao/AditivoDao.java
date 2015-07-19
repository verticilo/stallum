package stallum.dao;

import java.math.BigInteger;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import stallum.dto.FiltroAditivo;
import stallum.dto.OrdenacaoAditivo;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Aditivo;
import stallum.modelo.Obra;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AditivoDao extends AbstractDao {

	public static final String ALIAS 		= "adit";
	public static final String ALIAS_OBRA 	= "obra";
	
	public AditivoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Aditivo.class, ALIAS)
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
				.add(Projections.property(ALIAS + ".numero").as("numero"))
				.add(Projections.property(ALIAS + ".descricao").as("descricao"))
				.add(Projections.property(ALIAS + ".valor").as("valor"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_OBRA + ".id").as("obra.id"))
				.add(Projections.property(ALIAS_OBRA + ".nome").as("obra.nome"))
				.add(Projections.property(ALIAS_OBRA + ".alteradoEm").as("obra.alteradoEm"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()				
				
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(Aditivo.class, "id");
		//transformer.addGroup("Dissidio", Dissidio.class, "dissidios.Dissidio.id");
		return transformer;
	}
			
	
	/**
	 * Conulta a Aditivo no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Aditivo consultarPorId(Long idAditivo) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idAditivo));
		
		Aditivo result = (Aditivo) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Aditivo pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Aditivo buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Aditivo result = (Aditivo) criteria.uniqueResult();
		
		return result;
		
	}
	
	public String proximoNumero(Obra obra) {
		
		String hql = "select count(1) from Aditivo where obra_id = :idObra";
		
		SQLQuery qry = session.createSQLQuery(hql);
		qry.setLong("idObra", obra.getId());
		
		Integer num = ((BigInteger)qry.uniqueResult()).intValue();
		
		if (num != null) {
			num++;
			return "000".substring(0, 3 - num.toString().length()) + num.toString();
		}
		
		return "001";
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Aditivo> filtrar(FiltroAditivo filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Aditivo> result = (Collection<Aditivo>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroAditivo filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoAditivo.DATA.equals(filtro.getOrdenacao())) {
				criteria.addOrder(Order.asc(OrdenacaoAditivo.DATA.getNomeCampo()));				
				criteria.addOrder(Order.asc(OrdenacaoAditivo.OBRA.getNomeCampo()));				
			} else if (OrdenacaoAditivo.NUMERO.equals(filtro.getOrdenacao())) {
				criteria.addOrder(Order.asc(OrdenacaoAditivo.OBRA.getNomeCampo()));				
			} else if (OrdenacaoAditivo.OBRA.equals(filtro.getOrdenacao())) {
				criteria.addOrder(Order.asc(OrdenacaoAditivo.OBRA.getNomeCampo()));				
				criteria.addOrder(Order.asc(OrdenacaoAditivo.DATA.getNomeCampo()));				
			} else {
				if (OrdenacaoAditivo.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
					criteria.addOrder(Order.asc(filtro.getOrdenacao().getNomeCampo()));
				} else {
					criteria.addOrder(Order.desc(filtro.getOrdenacao().getNomeCampo()));
				}			
			}
			criteria.addOrder(Order.asc(OrdenacaoAditivo.NUMERO.getNomeCampo()));				
		}
		
		if (filtro.getPaginacao() != null) {
			criteria.setFirstResult(filtro.getPaginacao().getPrimeiroRegistro());
			criteria.setMaxResults(filtro.getPaginacao().getRegistrosPorPagina());
		}
		
	}

	private void prepararFiltro(Criteria criteria, FiltroAditivo filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getObra() != null && filtro.getObra().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_OBRA + ".id", filtro.getObra().getId()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS_OBRA + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".numero", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".descricao", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
		if (filtro.getDataDe() != null)
			criteria.add(Restrictions.ge(ALIAS + ".data", filtro.getDataDe()));
		if (filtro.getDataAte() != null)
			criteria.add(Restrictions.le(ALIAS + ".data", filtro.getDataAte()));
			
		
	}

}
