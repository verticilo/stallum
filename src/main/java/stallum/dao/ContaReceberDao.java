package stallum.dao;

import java.math.BigInteger;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import stallum.dto.FiltroContaReceber;
import stallum.dto.OrdenacaoContaReceber;
import stallum.enums.StatusConta;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.ContaReceber;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ContaReceberDao extends AbstractDao {

	public static final String ALIAS 			= "ctrc";
	public static final String ALIAS_CLIENTE	= "clie";
	public static final String ALIAS_CONTA_GER 	= "cger";
	public static final String ALIAS_ORIGEM 	= "orig";
	
	public ContaReceberDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(ContaReceber.class, ALIAS)
				.createAlias(ALIAS + ".cliente", ALIAS_CLIENTE)
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
				.add(Projections.property(ALIAS + ".valorRecebido").as("valorRecebido"))
				.add(Projections.property(ALIAS + ".status").as("status"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))

				.add(Projections.property(ALIAS_CLIENTE + ".id").as("cliente.id"))
				.add(Projections.property(ALIAS_CLIENTE + ".nomeCurto").as("cliente.nomeCurto"))
				.add(Projections.property(ALIAS_CLIENTE + ".alteradoEm").as("cliente.alteradoEm"))
				
				.add(Projections.property(ALIAS_ORIGEM + ".id").as("origem.id"))
				.add(Projections.property(ALIAS_ORIGEM + ".nome").as("origem.nome"))
				.add(Projections.property(ALIAS_ORIGEM + ".alteradoEm").as("origem.alteradoEm"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()				
				.add(Projections.property(ALIAS + ".tipo").as("tipo"))
				.add(Projections.property(ALIAS + ".numero").as("numero"))
				.add(Projections.property(ALIAS + ".dataRecto").as("dataRecto"))
				.add(Projections.property(ALIAS + ".obs").as("obs"))
				
				.add(Projections.property(ALIAS_CONTA_GER + ".id").as("conta.id"))
				.add(Projections.property(ALIAS_CONTA_GER + ".nome").as("conta.nome"))
				.add(Projections.property(ALIAS_CONTA_GER + ".alteradoEm").as("conta.alteradoEm"))
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(ContaReceber.class, "id");
		return transformer;
	}
			
	
	/**
	 * Conulta a ContaReceber no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public ContaReceber consultarPorId(Long idContaReceber) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idContaReceber));
		
		ContaReceber result = (ContaReceber) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a ContaReceber pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public ContaReceber buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		ContaReceber result = (ContaReceber) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<ContaReceber> filtrar(FiltroContaReceber filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<ContaReceber> result = (Collection<ContaReceber>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroContaReceber filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoContaReceber.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroContaReceber filtro) {

		if (filtro == null)
			return;

		if (filtro.getIds() != null && filtro.getIds().size() > 0)
			criteria.add(Restrictions.in(ALIAS + ".id", filtro.getIds()));
		
		if (filtro.getStatus() != null)
			criteria.add(Restrictions.eq(ALIAS + ".status", filtro.getStatus()));
		
		if (filtro.getConta() != null)
			criteria.add(Restrictions.eq(ALIAS_CONTA_GER + ".nome", filtro.getConta()));
		
		if (Boolean.TRUE.equals(filtro.isSemVencto()))
			criteria.add(Restrictions.isNull(ALIAS + ".vencto"));
		else if (Boolean.FALSE.equals(filtro.isSemVencto()))
			criteria.add(Restrictions.isNotNull(ALIAS + ".vencto"));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS_CLIENTE + ".nomeCurto", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_CLIENTE + ".razaoSocial", filtro.getPalavraChave(), MatchMode.ANYWHERE));
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

	public void pagar(ContaReceber cp) {

		String hql =  " update ContaReceber p set p.recebimento = :recto, p.status = :status, "
					+ " p.dataRecto = :dataRecto, p.acrescimos = :acrescimos, p.valorRecebido = :valRecebido "
					+ " where p.id = :idConta";

		Query qry = session.createQuery(hql);
		qry.setParameter("recto", cp.getRecebimento());
		qry.setParameter("status", StatusConta.LIQUIDADA);
		qry.setDate("dataRecto", cp.getRecebimento().getData());
		qry.setBigDecimal("acrescimos", cp.getAcrescimos());
		qry.setBigDecimal("valRecebido", cp.getValorRecebido());
		qry.setLong("idConta", cp.getId());
		
		qry.executeUpdate();
		
	}

	public boolean checarVencimentos() throws HibernateException {
			
		String sql = "select count(1) from contaReceber where vencto is null";
		
		SQLQuery qry = session.createSQLQuery(sql);
		
		BigInteger result = (BigInteger)qry.uniqueResult();
		
		return result.compareTo(BigInteger.ZERO) == 0;
		
	}

}
