package stallum.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import org.hibernate.sql.JoinFragment;

import stallum.dto.FiltroCentroCusto;
import stallum.dto.OrdenacaoCentroCusto;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.CentroCusto;
import stallum.modelo.Sindicato;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class CentroCustoDao extends AbstractDao {

	public static final String ALIAS 			= "ccus";
	public static final String ALIAS_SINDICATO	= "sind";
	
	public CentroCustoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(CentroCusto.class, ALIAS)
				.createAlias(ALIAS + ".sindicato", ALIAS_SINDICATO, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".nome").as("nome"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_SINDICATO + ".id").as("sindicato.id"))
				.add(Projections.property(ALIAS_SINDICATO + ".nome").as("sindicato.nome"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()				
				.add(Projections.property(ALIAS + ".obs").as("obs"))	
				
				.add(Projections.property(ALIAS_SINDICATO + ".alteradoEm").as("sindicato.alteradoEm"))
				;
	}
	
	private HibernateTransformer getTransformerListar() {
		HibernateTransformer transformer = new HibernateTransformer(CentroCusto.class, "id");
		return transformer;
	}
	
	private HibernateTransformer getTransformerConsultar() {
		HibernateTransformer transformer = getTransformerListar();
		return transformer;
	}
			
	
	/**
	 * Conulta a CentroCusto no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public CentroCusto consultarPorId(Long idCentroCusto, Date dataRef) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformerConsultar());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idCentroCusto));
		
		CentroCusto result = (CentroCusto) criteria.uniqueResult();
		
		String sql = "select sum(a.valor) from Despesa a where a.centroCusto_id = " + idCentroCusto;
		SQLQuery qry  = session.createSQLQuery(sql);
		Object total = qry.uniqueResult();
		if (total != null)
			result.setTotalDespesas(new BigDecimal(total.toString()));	
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataRef);
		sql = "select sum(a.valor) from Despesa a where a.centroCusto_id = " + idCentroCusto + 
			  " and extract(month from a.data) = " + (cal.get(Calendar.MONTH)); // Despesas do mês anterior
		qry  = session.createSQLQuery(sql);
		total = qry.uniqueResult();
		if (total != null)
			result.setDespesasMes(new BigDecimal(total.toString()));	
		
		sql = "select sum(p.custoHoraNormal + p.custoHoraExtra + p.encargos + p.salarioFamilia + p.valeBonificacao) " +
			  "from Ponto p inner join Apontamento a on a.id = p.apontamento_id where a.centroCusto_id = " + idCentroCusto;
		qry = session.createSQLQuery(sql);
		total = qry.uniqueResult();
		if (total != null)
			result.setTotalApontamentos(new BigDecimal(total.toString()));
				
		return result;
	}
	
	/**
	 * Busca a CentroCusto pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public CentroCusto buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformerListar());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		CentroCusto result = (CentroCusto) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<CentroCusto> filtrar(FiltroCentroCusto filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformerListar());			
		
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<CentroCusto> result = (Collection<CentroCusto>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroCentroCusto filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoCentroCusto.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroCentroCusto filtro) {

		if (filtro == null)
			return;
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

	public BigDecimal custosIndiretosMesAnterior(Sindicato sindicato, Date data) throws HibernateException {
		
		// Desepesas lancadas para o centro de custo
		
		String sql = 	" select sum(d.valor) from Despesa d " +
						" inner join CentroCusto c on c.id = d.centroCusto_id " +
						" where (c.sindicato_id is null or c.sindicato_id = :sind) and d.data >= :dataIni and d.data <= :dataFim";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.roll(Calendar.MONTH, false);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date dataIni = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date dataFim = cal.getTime();
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setLong("sind", sindicato.getId());		
		qry.setDate("dataIni", dataIni);
		qry.setDate("dataFim", dataFim);
		
		BigDecimal result = (BigDecimal) qry.uniqueResult();
		
		if (result == null)
			result = BigDecimal.ZERO;
		
		// Desepesas indiretas dos apontamento dos centros de custo
		
		sql = 	" select sum(p.custoHoraNormal + p.custoHoraExtra + p.encargos + p.salarioFamilia + p.valeBonificacao) from Ponto p " +
				" inner join Apontamento a on a.id = p.apontamento_id " +
				" inner join CentroCusto c on c.id = a.centroCusto_id " +
				" where (c.sindicato_id is null or c.sindicato_id = :sind) and a.data >= :dataIni and a.data <= :dataFim";

		qry = session.createSQLQuery(sql);
		qry.setLong("sind", sindicato.getId());
		qry.setDate("dataIni", dataIni);
		qry.setDate("dataFim", dataFim);
		
		BigDecimal result2 = (BigDecimal) qry.uniqueResult();
		
		if (result2 == null)
			result2 = BigDecimal.ZERO;
		
		return result.add(result2);
		
	}
	
	public BigDecimal totalHorasApontadas(Long idCentroCusto) throws HibernateException {

		String sql = 	" select sum(p.horasNormais) as horas from Ponto p " +
						" inner join Apontamento a on a.id = p.apontamento_id " +
						" inner join Obra o on o.id = a.obra_id " +
						" left outer join CentroCusto c on c.id = o.centroCusto_id " +
						" where o.centroCusto_id = :idCentroCusto ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setLong("idCentroCusto", idCentroCusto);
		
		BigDecimal result = (BigDecimal)qry.uniqueResult();
		
		return result;

	}
	
}
