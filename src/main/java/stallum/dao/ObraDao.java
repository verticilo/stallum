package stallum.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

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
import org.hibernate.sql.JoinFragment;

import stallum.dto.FiltroObra;
import stallum.dto.OrdenacaoObra;
import stallum.enums.StatusObra;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Obra;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ObraDao extends AbstractDao {

	public static final String ALIAS 			= "obra";
	public static final String ALIAS_CLIENTE 	= "clie";
	public static final String ALIAS_CCUSTO		= "ccus";
	public static final String ALIAS_CONTA		= "cger";
	public static final String ALIAS_EMPRESA 	= "empr";
	public static final String ALIAS_ENDERECO 	= "ende";
	public static final String ALIAS_SINDICATO 	= "sind";
	
	public ObraDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Obra.class, ALIAS)
				.createAlias(ALIAS + ".cliente", ALIAS_CLIENTE)
				.createAlias(ALIAS + ".sindicato", ALIAS_SINDICATO)
				.createAlias(ALIAS + ".empresa", ALIAS_EMPRESA)
				.createAlias(ALIAS + ".contaGerencial", ALIAS_CONTA, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS + ".centroCusto", ALIAS_CCUSTO, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS + ".endereco", ALIAS_ENDERECO)
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".nome").as("nome"))
				.add(Projections.property(ALIAS + ".valorInicial").as("valorInicial"))
				.add(Projections.property(ALIAS + ".saldoReceber").as("saldoReceber"))
				.add(Projections.property(ALIAS + ".totalReajustes").as("totalReajustes"))
				.add(Projections.property(ALIAS + ".status").as("status"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))

				.add(Projections.property(ALIAS_SINDICATO + ".id").as("sindicato.id"))
				.add(Projections.property(ALIAS_SINDICATO + ".nome").as("sindicato.nome"))
				.add(Projections.property(ALIAS_SINDICATO + ".alteradoEm").as("sindicato.alteradoEm"))
				
				.add(Projections.property(ALIAS_CLIENTE + ".razaoSocial").as("cliente.razaoSocial"))
				.add(Projections.property(ALIAS_CLIENTE + ".nomeCurto").as("cliente.nomeCurto"))
				
				.add(Projections.property(ALIAS_EMPRESA + ".id").as("empresa.id"))
				.add(Projections.property(ALIAS_EMPRESA + ".alteradoEm").as("empresa.alteradoEm"))				
				
				.add(Projections.property(ALIAS_CONTA + ".id").as("contaGerencial.id"))
				.add(Projections.property(ALIAS_CONTA + ".nome").as("contaGerencial.nome"))
				.add(Projections.property(ALIAS_CONTA + ".numero").as("contaGerencial.numero"))
				.add(Projections.property(ALIAS_CONTA + ".alteradoEm").as("contaGerencial.alteradoEm"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				
				.add(Projections.property(ALIAS + ".contrato").as("contrato"))
				.add(Projections.property(ALIAS + ".dataInicio").as("dataInicio"))
				.add(Projections.property(ALIAS + ".dataStatus").as("dataStatus"))
				.add(Projections.property(ALIAS + ".obs").as("obs"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_CCUSTO + ".id").as("centroCusto.id"))
				.add(Projections.property(ALIAS_CCUSTO + ".nome").as("centroCusto.nome"))
				.add(Projections.property(ALIAS_CCUSTO + ".alteradoEm").as("centroCusto.alteradoEm"))
				
				
				.add(Projections.property(ALIAS_CLIENTE + ".id").as("cliente.id"))
				.add(Projections.property(ALIAS_CLIENTE + ".alteradoEm").as("cliente.alteradoEm"))
				
				.add(Projections.property(ALIAS_EMPRESA + ".razaoSocial").as("empresa.razaoSocial"))
				.add(Projections.property(ALIAS_EMPRESA + ".nomeCurto").as("empresa.nomeCurto"))
				
				.add(Projections.property(ALIAS_ENDERECO + ".id").as("endereco.id"))
				.add(Projections.property(ALIAS_ENDERECO + ".logradouro").as("endereco.logradouro"))
				.add(Projections.property(ALIAS_ENDERECO + ".numero").as("endereco.numero"))
				.add(Projections.property(ALIAS_ENDERECO + ".complemento").as("endereco.complemento"))
				.add(Projections.property(ALIAS_ENDERECO + ".bairro").as("endereco.bairro"))
				.add(Projections.property(ALIAS_ENDERECO + ".cidade").as("endereco.cidade"))
				.add(Projections.property(ALIAS_ENDERECO + ".estado").as("endereco.estado"))
				.add(Projections.property(ALIAS_ENDERECO + ".cep").as("endereco.cep"))
				.add(Projections.property(ALIAS_ENDERECO + ".alteradoEm").as("endereco.alteradoEm"))
				
				;
	}
	
	private HibernateTransformer getTransformerListar() {
		HibernateTransformer transformer = new HibernateTransformer(Obra.class, "id");
		return transformer;
	}
	
	private HibernateTransformer getTransformerConsultar() {
		HibernateTransformer transformer = getTransformerListar();
		return transformer;
	}
			
	
	/**
	 * Conulta a Obra no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Obra consultarPorId(Long idObra) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformerConsultar());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idObra));
		
		Obra result = (Obra) criteria.uniqueResult();
		
		if (result == null)
			return null;
		
		String sql = "select sum(a.valor) from Aditivo a where a.obra_id = " + idObra;
		SQLQuery qry = session.createSQLQuery(sql);
		Object total = qry.uniqueResult();
		if (total != null)
			result.setTotalAditivos(new BigDecimal(total.toString()));
		
		sql = "select sum(a.valor) from Despesa a where a.obra_id = " + idObra;
		qry = session.createSQLQuery(sql);
		total = qry.uniqueResult();
		if (total != null)
			result.setTotalDespesas(new BigDecimal(total.toString()));	
		
		sql = "select sum(p.custosIndiretos) " +
				"from Ponto p inner join Apontamento a on a.id = p.apontamento_id where a.obra_id = " + idObra;
		qry = session.createSQLQuery(sql);
		total = qry.uniqueResult();
		if (total != null)
			result.setTotalCustosIndiretos(new BigDecimal(total.toString()));
		
		sql = "select sum(a.valor) from Medicao a where (a.cancelada is null or not a.cancelada) and a.obra_id = " + idObra;
		qry = session.createSQLQuery(sql);
		total = qry.uniqueResult();
		if (total != null)
			result.setTotalMedicoes(new BigDecimal(total.toString()));
		
		sql = "select sum(p.custoHoraNormal + p.custoHoraExtra + p.encargos + p.salarioFamilia + p.valeBonificacao) " +
			  "from Ponto p inner join Apontamento a on a.id = p.apontamento_id where a.obra_id = " + idObra;
		qry = session.createSQLQuery(sql);
		total = qry.uniqueResult();
		if (total != null)
			result.setTotalApontamentos(new BigDecimal(total.toString()));
		
		sql = "select pontos/dias from (select count(1) as \"pontos\", count(distinct a.data) as \"dias\" from Ponto p " +
			  "inner join Apontamento a on a.id = p.apontamento_id where a.obra_id = " + idObra + ") as apont " +
			  "where pontos > 0";
		qry = session.createSQLQuery(sql);
		total = qry.uniqueResult();
		if (total != null)
			result.setMediaFuncionarios(new BigDecimal(total.toString()));
		
		return result;
	}
	
	/**
	 * Busca a Obra pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Obra buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformerListar());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Obra result = (Obra) criteria.uniqueResult();
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Obra> listarAtivas(Date data) throws HibernateException {		
		
		String sql = 	" select id, nome, sindicato_id as \"sindicato.id\", alteradoem as \"alteradoEm\" from Obra o " +
						" where o.dataInicio <= :data " + 
						" and (not exists (select 1 from (select m.novoStatus from MovimentoObra m where m.obra_id = o.id " +
						"	and m.data <= :data order by m.data desc limit 1) as aux where novoStatus > 0)) ";

		SQLQuery qry = session.createSQLQuery(sql);
		qry.setDate("data", data);
		
		qry.setResultTransformer(new HibernateTransformer(Obra.class, "id"));
		
		Collection<Obra> lista = (Collection<Obra>) qry.list();	
		
		return lista;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Obra> filtrar(FiltroObra filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformerListar());			
		
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Obra> result = (Collection<Obra>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroObra filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoObra.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroObra filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getSindicato() != null)
			criteria.add(Restrictions.eq(ALIAS_SINDICATO + ".id", filtro.getSindicato().getId()));
		
		if (Boolean.TRUE.equals(filtro.getAtivas()))			
			criteria.add(Restrictions.eq(ALIAS + ".status", StatusObra.ATIVA));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".contrato", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_CLIENTE + ".nomeCurto", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_CLIENTE + ".razaoSocial", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_EMPRESA + ".nomeCurto", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_EMPRESA + ".razaoSocial", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_CCUSTO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

	public void atualizaSaldo(Obra obra) throws HibernateException {
		
		String hql = "update Obra o set o.saldoReceber = :saldoReceber, o.totalReajustes = :totalReajustes where o.id = :idObra";
		
		Query qry = session.createQuery(hql);
		qry.setBigDecimal("saldoReceber", obra.getSaldoReceber());
		qry.setBigDecimal("totalReajustes", obra.getTotalReajustes());
		qry.setLong("idObra", obra.getId());
		
		qry.executeUpdate();
		
	}
	
	public boolean isObraAtiva(Long idObra, Date data) throws HibernateException {		
		
		String sql = 	" select o.id from Obra o " +
						" where o.id = :idObra and o.dataInicio <= :data " + 
						" and not exists (select 1 from (select m.novoStatus from MovimentoObra m where m.obra_id = o.id " +
						"	and m.data <= :data order by m.data desc limit 1) as aux where novoStatus <> 0) ";

		SQLQuery qry = session.createSQLQuery(sql);
		qry.setLong("idObra", idObra);
		qry.setDate("data", data);
		
		boolean result = qry.uniqueResult() != null;
		
		return result;
		
	}

	public BigDecimal totalHorasApontadas(Long idObra) throws HibernateException {

		String sql = 	" select sum(p.horasNormais) as horas from Ponto p " +
						" inner join Apontamento a on a.id = p.apontamento_id ";
		
		if (idObra != null)
			sql += " where a.obra_id = :idObra ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		
		if (idObra != null)
			qry.setLong("idObra", idObra);
		
		BigDecimal result = (BigDecimal)qry.uniqueResult();
		
		return result;

	}
}
