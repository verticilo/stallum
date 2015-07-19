package stallum.dao;

import java.math.BigInteger;
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

import stallum.dto.FiltroMovimento;
import stallum.dto.OrdenacaoMovimento;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Movimento;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class MovimentoDao extends AbstractDao {

	public static final String ALIAS				= "movi";
	public static final String ALIAS_FUNCIONARIO	= "func";
	public static final String ALIAS_FUNCAO			= "fcao";
	
	public MovimentoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Movimento.class, ALIAS)
				.createAlias(ALIAS + ".funcionario", ALIAS_FUNCIONARIO)
				.createAlias(ALIAS + ".novaFuncao", ALIAS_FUNCAO, JoinFragment.LEFT_OUTER_JOIN)
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
				.add(Projections.property(ALIAS + ".bonus").as("bonus"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_FUNCAO + ".id").as("novaFuncao.id"))
				.add(Projections.property(ALIAS_FUNCAO + ".nome").as("novaFuncao.nome"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				.add(Projections.property(ALIAS + ".motivo").as("motivo"))
				
				.add(Projections.property(ALIAS_FUNCIONARIO + ".id").as("funcionario.id"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".nome").as("funcionario.nome"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".apelido").as("funcionario.apelido"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".alteradoEm").as("funcionario.alteradoEm"))
				
				;
	}
		
	
	/**
	 * Conulta a Movimento no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Movimento consultarPorId(Long idMovimento) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Movimento.class));
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idMovimento));
		
		Movimento result = (Movimento) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Movimento pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Movimento buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Movimento.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Movimento result = (Movimento) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Movimento> filtrar(FiltroMovimento filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Movimento.class, "id"));
	
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Movimento> result = (Collection<Movimento>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroMovimento filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoMovimento.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
				criteria.addOrder(Order.asc(filtro.getOrdenacao().getNomeCampo()));
			} else {
				criteria.addOrder(Order.desc(filtro.getOrdenacao().getNomeCampo()));
			}
			// Sempre ordenando por data do movimento
			criteria.addOrder(Order.asc(OrdenacaoMovimento.DATA.getNomeCampo()));
		}
		
		if (filtro.getPaginacao() != null) {
			criteria.setFirstResult(filtro.getPaginacao().getPrimeiroRegistro());
			criteria.setMaxResults(filtro.getPaginacao().getRegistrosPorPagina());
		}
		
	}

	private void prepararFiltro(Criteria criteria, FiltroMovimento filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getFuncionario() != null && filtro.getFuncionario().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_FUNCIONARIO + ".id", filtro.getFuncionario().getId()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".motivo", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_FUNCIONARIO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_FUNCAO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

	/**
	 * Verifica se ja existe ponto para o funcionario a partir da data informada.
	 * @param data
	 * @param idFuncionario
	 * @return
	 * @throws HibernateException
	 */
	public boolean existePonto(Date data, Long idFuncionario) throws HibernateException {

		String sql = 	" select count(1) from Ponto p inner join Apontamento a on a.id = p.apontamento_id " + 
						" where a.data >= :data and p.funcionario_id = :idFunc ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setDate("data", data);
		qry.setLong("idFunc", idFuncionario);
		
		BigInteger result = (BigInteger) qry.uniqueResult();
		
		return result != null && result.compareTo(BigInteger.ZERO) > 0;
	
	}

	
	public void removePonto(Long idFuncionario, Date data, Date dataFim) {
		
		String sql = " delete from Ponto where id in (select p.id as id from Ponto p inner join Apontamento a on a.id = p.apontamento_id " + 
					 " where p.funcionario_id = :idFunc and a.data >= :data and a.data <= :dataFim) ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setDate("data", data);
		qry.setDate("dataFim", dataFim);
		qry.setLong("idFunc", idFuncionario);
		
		qry.executeUpdate();

	}

}
