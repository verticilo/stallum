package stallum.dao;

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

import stallum.dto.DiaApontamento;
import stallum.dto.FiltroApontamento;
import stallum.dto.OrdenacaoApontamento;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Apontamento;
import stallum.modelo.CentroCusto;
import stallum.modelo.Funcionario;
import stallum.modelo.Obra;
import stallum.modelo.Ponto;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ApontamentoDao extends AbstractDao {

	public static final String ALIAS 				= "apon";
	public static final String ALIAS_OBRA			= "obra";
	public static final String ALIAS_CCUSTO			= "ccus";
	public static final String ALIAS_SINDICATO		= "sind";
	public static final String ALIAS_PONTO			= "pont";
	public static final String ALIAS_FUNCIONARIO	= "func";
	public static final String ALIAS_EMPRESA		= "empr";
	
	public ApontamentoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Apontamento.class, ALIAS)
				.createAlias(ALIAS + ".obra", ALIAS_OBRA, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS + ".centroCusto", ALIAS_CCUSTO, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS + ".pontos", ALIAS_PONTO, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS_CCUSTO + ".sindicato", ALIAS_SINDICATO, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS_PONTO + ".funcionario", ALIAS_FUNCIONARIO, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS_FUNCIONARIO + ".empresa", ALIAS_EMPRESA, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				.add(Projections.property(ALIAS + ".feriado").as("feriado"))
				.add(Projections.property(ALIAS_OBRA + ".id").as("obra.id"))
				.add(Projections.property(ALIAS_OBRA + ".nome").as("obra.nome"))
				.add(Projections.property(ALIAS_CCUSTO + ".id").as("centroCusto.id"))
				.add(Projections.property(ALIAS_CCUSTO + ".nome").as("centroCusto.nome"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()			
				.add(Projections.property(ALIAS + ".obs").as("obs"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_OBRA + ".sindicato").as("obra.sindicato"))
				.add(Projections.property(ALIAS_OBRA + ".alteradoEm").as("obra.alteradoEm"))
				
				.add(Projections.property(ALIAS_PONTO + ".id").as("pontos.Ponto.id"))
				.add(Projections.property(ALIAS_PONTO + ".presente").as("pontos.Ponto.presente"))
				.add(Projections.property(ALIAS_PONTO + ".horaEntrada").as("pontos.Ponto.horaEntrada"))
				.add(Projections.property(ALIAS_PONTO + ".horaSaida").as("pontos.Ponto.horaSaida"))
				.add(Projections.property(ALIAS_PONTO + ".horaExtra").as("pontos.Ponto.horaExtra"))
				.add(Projections.property(ALIAS_PONTO + ".percHoraExtra").as("pontos.Ponto.percHoraExtra"))
				.add(Projections.property(ALIAS_PONTO + ".motivoFalta").as("pontos.Ponto.motivoFalta"))
				.add(Projections.property(ALIAS_PONTO + ".motivoAbono").as("pontos.Ponto.motivoAbono"))
				.add(Projections.property(ALIAS_PONTO + ".custoHoraNormal").as("pontos.Ponto.custoHoraNormal"))
				.add(Projections.property(ALIAS_PONTO + ".custoHoraExtra").as("pontos.Ponto.custoHoraExtra"))
				.add(Projections.property(ALIAS_PONTO + ".encargos").as("pontos.Ponto.encargos"))
				.add(Projections.property(ALIAS_PONTO + ".salarioFamilia").as("pontos.Ponto.salarioFamilia"))
				.add(Projections.property(ALIAS_PONTO + ".valeBonificacao").as("pontos.Ponto.valeBonificacao"))
				.add(Projections.property(ALIAS_PONTO + ".custosIndiretos").as("pontos.Ponto.custosIndiretos"))
				.add(Projections.property(ALIAS_PONTO + ".alteradoEm").as("pontos.Ponto.alteradoEm"))				
				
				.add(Projections.property(ALIAS_FUNCIONARIO + ".id").as("pontos.Ponto.funcionario.id"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".nome").as("pontos.Ponto.funcionario.nome"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".apelido").as("pontos.Ponto.funcionario.apelido"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".dataAdmissao").as("pontos.Ponto.funcionario.dataAdmissao"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".dataDemissao").as("pontos.Ponto.funcionario.dataDemissao"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".status").as("pontos.Ponto.funcionario.status"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".alteradoEm").as("pontos.Ponto.funcionario.alteradoEm"))
				
				.add(Projections.property(ALIAS_CCUSTO + ".alteradoEm").as("centroCusto.alteradoEm"))

				.add(Projections.property(ALIAS_SINDICATO + ".id").as("centroCusto.sindicato.id"))
				.add(Projections.property(ALIAS_SINDICATO + ".nome").as("centroCusto.sindicato.nome"))
				.add(Projections.property(ALIAS_SINDICATO + ".alteradoEm").as("centroCusto.sindicato.alteradoEm"))
				
				;
	}
	
	private HibernateTransformer getTransformerListar() {
		HibernateTransformer transformer = new HibernateTransformer(Apontamento.class, "id");
		return transformer;
	}
	
	private HibernateTransformer getTransformerConsultar() {
		HibernateTransformer transformer = getTransformerListar();
		transformer.addGroup("Ponto", Ponto.class, "pontos.Ponto.id");
		return transformer;
	}
		
	/**
	 * Busca o Apontamento pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Apontamento
	 * @throws HibernateException
	 */
	public Apontamento buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformerListar());

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Apontamento result = (Apontamento) criteria.uniqueResult();
		
		return result;
		
	}

	public Apontamento consultarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformerConsultar());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		criteria.addOrder(Order.asc(ALIAS_FUNCIONARIO + ".nome"));
		
		Apontamento result = (Apontamento) criteria.uniqueResult();
		
		return result;
	}
	
	public Apontamento conultarPorData(Apontamento apontamento) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformerConsultar());
				
		criteria.add(Restrictions.eq(ALIAS + ".data", apontamento.getData()));
		criteria.add(Restrictions.eq(ALIAS_PONTO + ".presente", Boolean.TRUE));
		if (apontamento.getObra().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_OBRA + ".id", apontamento.getObra().getId()));
		else
			criteria.add(Restrictions.eq(ALIAS_CCUSTO + ".id", apontamento.getCentroCusto().getId()));
		
		criteria.addOrder(Order.asc(ALIAS_FUNCIONARIO + ".nome"));
		
		Apontamento result = (Apontamento) criteria.uniqueResult();
		
		return result;
	}
	
	public Collection<Ponto> listarPontos(Date data) throws HibernateException {
		return listarPontos(null, data, null);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Ponto> listarPontos(Funcionario funcionario, Date dataDe, Date dataAte) throws HibernateException {		
				
		Criteria criteria = session.createCriteria(Ponto.class, ALIAS_PONTO)
				.createAlias(ALIAS_PONTO + ".funcionario", ALIAS_FUNCIONARIO, JoinFragment.RIGHT_OUTER_JOIN)
				.createAlias(ALIAS_PONTO + ".apontamento", ALIAS, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS + ".obra", ALIAS_OBRA, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS + ".centroCusto", ALIAS_CCUSTO, JoinFragment.LEFT_OUTER_JOIN)
				;
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property(ALIAS_PONTO + ".id").as("id"))
				.add(Projections.property(ALIAS_PONTO + ".presente").as("presente"))
				.add(Projections.property(ALIAS_PONTO + ".horaEntrada").as("horaEntrada"))
				.add(Projections.property(ALIAS_PONTO + ".horaSaida").as("horaSaida"))
				.add(Projections.property(ALIAS_PONTO + ".motivoFalta").as("motivoFalta"))
				.add(Projections.property(ALIAS_PONTO + ".motivoAbono").as("motivoAbono"))
				.add(Projections.property(ALIAS_PONTO + ".alteradoEm").as("alteradoEm"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".id").as("funcionario.id"))				
				.add(Projections.property(ALIAS_FUNCIONARIO + ".nome").as("funcionario.nome"))				
				.add(Projections.property(ALIAS_FUNCIONARIO + ".apelido").as("funcionario.apelido"))				
				.add(Projections.property(ALIAS_FUNCIONARIO + ".alteradoEm").as("funcionario.alteradoEm"))				
				.add(Projections.property(ALIAS + ".id").as("apontamento.id"))				
				.add(Projections.property(ALIAS + ".data").as("apontamento.data"))				
				.add(Projections.property(ALIAS + ".alteradoEm").as("apontamento.alteradoEm"))				
				.add(Projections.property(ALIAS_OBRA + ".id").as("apontamento.obra.id"))				
				.add(Projections.property(ALIAS_OBRA + ".nome").as("apontamento.obra.nome"))				
				.add(Projections.property(ALIAS_CCUSTO + ".id").as("apontamento.centroCusto.id"))				
				.add(Projections.property(ALIAS_CCUSTO + ".nome").as("apontamento.centroCusto.nome"))				
				);
		
		criteria.setResultTransformer(new HibernateTransformer(Ponto.class, "id"));
		
		if (funcionario != null)
			criteria.add(Restrictions.eq(ALIAS_FUNCIONARIO + ".id", funcionario.getId()));
		if ((dataDe != null) && (dataAte == null))
			criteria.add(Restrictions.eq(ALIAS + ".data", dataDe));
		else if (dataDe != null)
			criteria.add(Restrictions.ge(ALIAS + ".data", dataDe));
		if (dataAte != null)
			criteria.add(Restrictions.le(ALIAS + ".data", dataAte));
				
		criteria.addOrder(Order.asc(ALIAS_FUNCIONARIO + ".nome"));
		
		Collection<Ponto> result = (Collection<Ponto>) criteria.list();
		
		return result;
			
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Obra> listarObrasNaoLancadas(Date data) throws HibernateException {		
		
		String sql = 	" select id, nome, sindicato_id as \"sindicato.id\", alteradoem as \"alteradoEm\" from Obra o " +
						" where o.dataInicio <= :data " + 
						" and not exists (select 1 from Apontamento a where a.data = :data and a.obra_id = o.id) " +
						" and not exists (select 1 from (select m.novoStatus from MovimentoObra m where m.obra_id = o.id " +
						"	and m.data <= :data order by m.data desc limit 1) as aux where novoStatus <> 0) ";

		SQLQuery qry = session.createSQLQuery(sql);
		qry.setDate("data", data);
		
		qry.setResultTransformer(new HibernateTransformer(Obra.class, "id"));
		
		Collection<Obra> lista = (Collection<Obra>) qry.list();	
		
		return lista;
		
	}
	
	@SuppressWarnings("unchecked")
	public Collection<CentroCusto> listarCentrosCustoNaoLancados(Date data) throws HibernateException {		
		
		String sql = 	" select id, nome from CentroCusto c where not exists " +
						" (select 1 from Apontamento a where a.data = :data and a.centroCusto_id = c.id) ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setDate("data", data);
		
		qry.setResultTransformer(new HibernateTransformer(CentroCusto.class, "id"));
		
		Collection<CentroCusto> lista = (Collection<CentroCusto>) qry.list();	
		
		return lista;
		
	}

	@SuppressWarnings("unchecked")
	public Collection<Apontamento> filtrar(FiltroApontamento filtro) throws HibernateException {
		
		Criteria criteria = null;		
		criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());		
		criteria.setResultTransformer(getTransformerListar());			
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Apontamento> result = (Collection<Apontamento>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Apontamento> filtrarCompleto(FiltroApontamento filtro) throws HibernateException {
		
		Criteria criteria = null;		
		criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());		
		criteria.setResultTransformer(getTransformerConsultar());			
		
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Apontamento> result = (Collection<Apontamento>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Apontamento> filtrarComPontos(FiltroApontamento filtro) throws HibernateException {
		
		Criteria criteria = null;		
		criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());		
		criteria.setResultTransformer(getTransformerConsultar());			
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		criteria.addOrder(Order.asc(ALIAS_FUNCIONARIO + ".id"));
		criteria.addOrder(Order.asc(OrdenacaoApontamento.DATA.getNomeCampo()));
		
		Collection<Apontamento> result = (Collection<Apontamento>) criteria.list();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Apontamento> filtrarComPontosPorData(FiltroApontamento filtro) throws HibernateException {
		
		Criteria criteria = null;		
		criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());		
		criteria.setResultTransformer(getTransformerConsultar());			
		
		prepararFiltro(criteria, filtro);
		
//		prepararPaginacao(filtro, criteria);
		
		criteria.addOrder(Order.asc(OrdenacaoApontamento.DATA.getNomeCampo()));
		criteria.addOrder(Order.asc(ALIAS_OBRA + ".sindicato"));
		
		Collection<Apontamento> result = (Collection<Apontamento>) criteria.list();
		
		return result;
	}
	
	public Apontamento getUltimoApontamentoObra(Long idObra, Date data) {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());		
		criteria.setResultTransformer(getTransformerListar());	
		criteria.addOrder(Order.desc(ALIAS + ".data"));
		criteria.setMaxResults(1);
		
		criteria.add(Restrictions.eq(ALIAS_OBRA + ".id", idObra));
		criteria.add(Restrictions.le(ALIAS + ".data", data));
		criteria.add(Restrictions.or(Restrictions.isNull(ALIAS + ".feriado"),
									 Restrictions.eq(ALIAS + ".feriado", Boolean.FALSE)));
		
		Apontamento result = (Apontamento)criteria.uniqueResult();

		if (result == null)
			return null;
		
		Criteria criteria2 = getCriteriaConsultar();
		criteria2.setProjection(getProjectionConsultar());		
		criteria2.setResultTransformer(getTransformerConsultar());			
		criteria2.addOrder(Order.asc(ALIAS_FUNCIONARIO + ".apelido"));
		criteria2.addOrder(Order.asc(ALIAS_FUNCIONARIO + ".nome"));
		
		criteria2.add(Restrictions.eq(ALIAS + ".id", result.getId()));
		
		result = (Apontamento)criteria2.uniqueResult();
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Ponto> listarPontosCentroCusto(Long idCentroCusto) {
		
		String sql = 	"select f.id as \"funcionario.id\", " +
						"		f.nome as \"funcionario.nome\", " +
						"		sum(p.horasNormais) as \"horasNormais\", " +
						"		sum(p.horaExtra) as \"horaExtra\", " +
						"		sum(p.custoHoraNormal) as \"custoHoraNormal\", " +
						"		sum(p.custoHoraExtra) as \"custoHoraExtra\", " +
						"		sum(p.encargos) as \"encargos\", " +
						"		sum(p.salarioFamilia) as \"salarioFamilia\", " +
						"		sum(p.valeBonificacao) as \"valeBonificacao\", " +
						" (select count(1) from Ponto p1 inner join Apontamento a on a.id = p1.apontamento_id where p1.funcionario_id = f.id and p1.motivoFalta is null) as \"diasTrab\", " + 
				        " (select count(1) from Ponto p1 inner join Apontamento a on a.id = p1.apontamento_id where p1.funcionario_id = f.id and p1.motivoFalta <> 0) as \"abonos\", " +
				        " (select count(1) from Ponto p1 inner join Apontamento a on a.id = p1.apontamento_id where p1.funcionario_id = f.id and p1.motivoFalta = 0) as \"faltas\" " +
						"from Ponto p " +
						"inner join Funcionario f on f.id = p.funcionario_id " +
						"inner join Apontamento a on a.id = p.apontamento_id " +
						"where a.centroCusto_id = " + idCentroCusto +
						" group by f.id, f.nome order by f.nome ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		
		qry.setResultTransformer(new HibernateTransformer(Ponto.class, "funcionario.id"));
		
		Collection<Ponto> result = (Collection<Ponto>)qry.list(); 
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Ponto> listarPontosObra(Long idObra) {
		
		String sql = 	"select f.id as \"funcionario.id\", " +
				"		f.nome as \"funcionario.nome\", " +
				"		sum(p.horasNormais) as \"horasNormais\", " +
				"		sum(p.horaExtra) as \"horaExtra\", " +
				"		sum(p.custoHoraNormal) as \"custoHoraNormal\", " +
				"		sum(p.custoHoraExtra) as \"custoHoraExtra\", " +
				"		sum(p.encargos) as \"encargos\", " +
				"		sum(p.salarioFamilia) as \"salarioFamilia\", " +
				"		sum(p.valeBonificacao) as \"valeBonificacao\", " +
				" (select count(1) from Ponto p1 inner join Apontamento a on a.id = p1.apontamento_id where p1.funcionario_id = f.id and p1.motivoFalta is null) as \"diasTrab\", " + 
				" (select count(1) from Ponto p1 inner join Apontamento a on a.id = p1.apontamento_id where p1.funcionario_id = f.id and p1.motivoFalta <> 0) as \"abonos\", " +
				" (select count(1) from Ponto p1 inner join Apontamento a on a.id = p1.apontamento_id where p1.funcionario_id = f.id and p1.motivoFalta = 0) as \"faltas\" " +
				"from Ponto p " +
				"inner join Funcionario f on f.id = p.funcionario_id " +
				"inner join Apontamento a on a.id = p.apontamento_id " +
				"where a.obra_id = " + idObra +
				" group by f.id, f.nome order by f.nome ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		
		qry.setResultTransformer(new HibernateTransformer(Ponto.class, "funcionario.id"));
		
		Collection<Ponto> result = (Collection<Ponto>)qry.list(); 
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<DiaApontamento> buscarCalendarioObra(Calendar cal) {
		
		String sql = 	" select a.data as \"data\", " +
						" count(1) as \"numPontos\", " +
						" (select count(1) from Funcionario f " +
						"  where f.dataAdmissao <= a.data " +
						"  and (not exists (select 1 from (select m.novoStatus from Movimento m " +
						"  where m.funcionario_id = f.id and m.data <= a.data order by m.data desc limit 1) as aux where novoStatus > 2)) " +
						" ) as \"numFuncionarios\" " +   
						" from Ponto p " +
						" inner join Apontamento a on a.id = p.apontamento_id " +
						" where extract(month from a.data) = " + (cal.get(Calendar.MONTH) + 1) +
						" group by a.data";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setResultTransformer(new HibernateTransformer(DiaApontamento.class, "data"));
		
		Collection<DiaApontamento> result = (Collection<DiaApontamento>) qry.list();
		
		return result;
		
	}
		
	private void prepararPaginacao(FiltroApontamento filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoApontamento.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroApontamento filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getFuncionario() != null)
			criteria.add(Restrictions.eq(ALIAS_FUNCIONARIO + ".id", filtro.getFuncionario().getId()));
		
		if (filtro.getEmpresa() != null)
			criteria.add(Restrictions.eq(ALIAS_EMPRESA + ".id", filtro.getEmpresa().getId()));
		
		if (filtro.getStatusFuncionario() != null)
			criteria.add(Restrictions.eq(ALIAS_FUNCIONARIO + ".status", filtro.getStatusFuncionario()));
		
		if (filtro.getObra() != null)
			criteria.add(Restrictions.eq(ALIAS_OBRA + ".id", filtro.getObra().getId()));		
		else if (filtro.getCentroCusto() != null)
			criteria.add(Restrictions.eq(ALIAS_CCUSTO + ".id", filtro.getCentroCusto().getId()));
		
		if (filtro.getMotivoFalta() != null)
			criteria.add(Restrictions.eq(ALIAS_PONTO + ".motivoFalta", filtro.getMotivoFalta()));
		
		if (filtro.isOcorrencias()) {
			Disjunction disj = Restrictions.disjunction();
			disj.add(Restrictions.isNotNull(ALIAS_PONTO + ".horaExtra"));
			disj.add(Restrictions.isNotNull(ALIAS_PONTO + ".motivoFalta"));
			criteria.add(disj);
		}			
		
		if (filtro.getData() != null && filtro.getDataFim() == null) {
			criteria.add(Restrictions.eq(ALIAS + ".data", filtro.getData()));
		} else {
			if (filtro.getData() != null)
				criteria.add(Restrictions.ge(ALIAS + ".data", filtro.getData()));
			if (filtro.getDataFim() != null)
				criteria.add(Restrictions.le(ALIAS + ".data", filtro.getDataFim()));
		}
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj = Restrictions.disjunction();
			disj.add(Restrictions.ilike(ALIAS_OBRA + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj.add(Restrictions.ilike(ALIAS_CCUSTO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj.add(Restrictions.ilike(ALIAS_FUNCIONARIO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj.add(Restrictions.ilike(ALIAS_FUNCIONARIO + ".apelido", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj);
		}
		
	}

	public void excluirPontosAssociados(Apontamento apontamento) throws HibernateException {
		
		String sql = " delete from Ponto where apontamento_id = :idApont and funcionario_id is null ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setLong("idApont", apontamento.getId());
		
		qry.executeUpdate();
		
	}

	public void removeApontamentoSemPonto() {
		String sql = "delete from Apontamento a where not exists (select 1 from Ponto p where p.apontamento_id = a.id) and a.feriado is not true";
		SQLQuery qry = session.createSQLQuery(sql);
		qry.executeUpdate();
	}

}
