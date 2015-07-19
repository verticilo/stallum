package stallum.dao;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import stallum.dto.FiltroContaPagar;
import stallum.dto.OrdenacaoContaPagar;
import stallum.enums.StatusConta;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.ContaPagar;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ContaPagarDao extends AbstractDao {

	public static final String ALIAS 			= "ctpg";
	public static final String ALIAS_FORNECEDOR = "forn";
	public static final String ALIAS_CONTA_GER 	= "cger";
	public static final String ALIAS_ORIGEM 	= "orig";
	
	public ContaPagarDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(ContaPagar.class, ALIAS)
				.createAlias(ALIAS + ".fornecedor", ALIAS_FORNECEDOR)
				.createAlias(ALIAS + ".origem", ALIAS_ORIGEM)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS + ".conta", ALIAS_CONTA_GER)
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				.add(Projections.property(ALIAS + ".conta").as("conta"))
				.add(Projections.property(ALIAS + ".valor").as("valor"))
				.add(Projections.property(ALIAS + ".vencto").as("vencto"))
				.add(Projections.property(ALIAS + ".acrescimos").as("acrescimos"))
				.add(Projections.property(ALIAS + ".valorPago").as("valorPago"))
				.add(Projections.property(ALIAS + ".status").as("status"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))

				.add(Projections.property(ALIAS_FORNECEDOR + ".id").as("fornecedor.id"))
				.add(Projections.property(ALIAS_FORNECEDOR + ".nomeCurto").as("fornecedor.nomeCurto"))
				.add(Projections.property(ALIAS_FORNECEDOR + ".alteradoEm").as("fornecedor.alteradoEm"))
				
				.add(Projections.property(ALIAS_ORIGEM + ".id").as("origem.id"))
				.add(Projections.property(ALIAS_ORIGEM + ".nome").as("origem.nome"))
				.add(Projections.property(ALIAS_ORIGEM + ".alteradoEm").as("origem.alteradoEm"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()				
				.add(Projections.property(ALIAS + ".origem").as("origem"))
				.add(Projections.property(ALIAS + ".tipo").as("tipo"))
				.add(Projections.property(ALIAS + ".numero").as("numero"))
				.add(Projections.property(ALIAS + ".dataPagto").as("dataPagto"))
				.add(Projections.property(ALIAS + ".obs").as("obs"))
				.add(Projections.property(ALIAS_CONTA_GER + ".id").as("conta.id"))
				.add(Projections.property(ALIAS_CONTA_GER + ".nome").as("conta.nome"))
				.add(Projections.property(ALIAS_CONTA_GER + ".alteradoEm").as("conta.alteradoEm"))
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(ContaPagar.class, "id");
		return transformer;
	}
			
	
	/**
	 * Conulta a ContaPagar no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public ContaPagar consultarPorId(Long idContaPagar) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idContaPagar));
		
		ContaPagar result = (ContaPagar) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a ContaPagar pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public ContaPagar buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		ContaPagar result = (ContaPagar) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<ContaPagar> filtrar(FiltroContaPagar filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<ContaPagar> result = (Collection<ContaPagar>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroContaPagar filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoContaPagar.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
				criteria.addOrder(Order.asc(filtro.getOrdenacao().getNomeCampo()));
			} else {
				criteria.addOrder(Order.desc(filtro.getOrdenacao().getNomeCampo()));
			}
		}
		criteria.addOrder(Order.asc(ALIAS + ".vencto"));
		
		if (filtro.getPaginacao() != null) {
			criteria.setFirstResult(filtro.getPaginacao().getPrimeiroRegistro());
			criteria.setMaxResults(filtro.getPaginacao().getRegistrosPorPagina());
		}
		
	}

	private void prepararFiltro(Criteria criteria, FiltroContaPagar filtro) {

		if (filtro == null)
			return;

		if (filtro.getIds() != null && filtro.getIds().size() > 0)
			criteria.add(Restrictions.in(ALIAS + ".id", filtro.getIds()));
		
		if (filtro.getStatus() != null)
			criteria.add(Restrictions.eq(ALIAS + ".status", filtro.getStatus()));
		
		if (filtro.getConta() != null)
			criteria.add(Restrictions.eq(ALIAS_CONTA_GER + ".nome", filtro.getConta()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS_FORNECEDOR + ".nomeCurto", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_FORNECEDOR + ".razaoSocial", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_CONTA_GER + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_ORIGEM + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".numero", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".obs", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
		if (filtro.getDataDe() != null)
			criteria.add(Restrictions.ge(ALIAS + ".data", filtro.getDataDe()));
		if (filtro.getDataAte() != null)
			criteria.add(Restrictions.le(ALIAS + ".data", filtro.getDataAte()));
		
	}

	public void pagar(ContaPagar cp) {

		String hql =  " update ContaPagar p set p.pagamento = :pagto, p.status = :status, "
					+ " p.dataPagto = :dataPagto, p.acrescimos = :acrescimos, p.valorPago = :valPago "
					+ " where p.id = :id";

		Query qry = session.createQuery(hql);
		qry.setParameter("pagto", cp.getPagamento());
		qry.setParameter("status", StatusConta.LIQUIDADA);
		qry.setDate("dataPagto", cp.getPagamento().getData());
		qry.setBigDecimal("acrescimos", cp.getAcrescimos());
		qry.setBigDecimal("valPago", cp.getValorPago());
		qry.setLong("id", cp.getId());
		
		qry.executeUpdate();
		
	}

}
