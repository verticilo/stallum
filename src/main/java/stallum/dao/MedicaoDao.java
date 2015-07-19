package stallum.dao;

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
import org.hibernate.sql.JoinFragment;

import stallum.dto.FiltroMedicao;
import stallum.dto.OrdenacaoMedicao;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Medicao;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class MedicaoDao extends AbstractDao {

	public static final String ALIAS 				= "medi";
	public static final String ALIAS_OBRA 			= "obra";
	public static final String ALIAS_EMPRESA_OBRA	= "epob";
	public static final String ALIAS_EMPRESA		= "empr";
	
	public MedicaoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Medicao.class, ALIAS)
				.createAlias(ALIAS + ".obra", ALIAS_OBRA)
				.createAlias(ALIAS + ".empresa", ALIAS_EMPRESA, JoinFragment.LEFT_OUTER_JOIN)
				.createAlias(ALIAS_OBRA + ".empresa", ALIAS_EMPRESA_OBRA)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".notaFiscal").as("notaFiscal"))
				.add(Projections.property(ALIAS + ".valor").as("valor"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				.add(Projections.property(ALIAS + ".cancelada").as("cancelada"))

				.add(Projections.property(ALIAS_OBRA + ".id").as("obra.id"))
				.add(Projections.property(ALIAS_OBRA + ".nome").as("obra.nome"))
				
				.add(Projections.property(ALIAS_EMPRESA + ".id").as("empresa.id"))
				.add(Projections.property(ALIAS_EMPRESA + ".nomeCurto").as("empresa.nomeCurto"))
				.add(Projections.property(ALIAS_EMPRESA + ".razaoSocial").as("empresa.razaoSocial"))
				
				.add(Projections.property(ALIAS_EMPRESA_OBRA + ".nomeCurto").as("obra.empresa.nomeCurto"))
				.add(Projections.property(ALIAS_EMPRESA_OBRA + ".razaoSocial").as("obra.empresa.razaoSocial"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()				
				.add(Projections.property(ALIAS + ".iss").as("iss"))
				.add(Projections.property(ALIAS + ".inss").as("inss"))
				.add(Projections.property(ALIAS + ".clss").as("clss"))
				.add(Projections.property(ALIAS + ".irrf").as("irrf"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_OBRA + ".alteradoEm").as("obra.alteradoEm"))
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(Medicao.class, "id");
		//transformer.addGroup("Dissidio", Dissidio.class, "dissidios.Dissidio.id");
		return transformer;
	}
			
	
	/**
	 * Conulta a Medicao no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Medicao consultarPorId(Long idMedicao) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", idMedicao));
		
		Medicao result = (Medicao) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Medicao pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Medicao buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Medicao result = (Medicao) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Medicao> filtrar(FiltroMedicao filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Medicao> result = (Collection<Medicao>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}
	
	public boolean existe(Medicao medicao) {
		
		String sql = 	" select 1 from Medicao m " +
						" inner join Obra o on o.id = m.obra_id " +
						" where m.notaFiscal = :notaFiscal " +
						" and m.data = :data and m.valor = :valor ";
		
		if (medicao.getObra() !=  null && medicao.getObra().getEmpresa() != null)
			sql += " and o.empresa_id = :idEmpresa ";
		
		if (medicao.getId() != null)
			sql += " and m.id <> :id";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setString("notaFiscal", medicao.getNotaFiscal());
		qry.setDate("data", medicao.getData());
		qry.setBigDecimal("valor", medicao.getValor());
		if (medicao.getObra() !=  null && medicao.getObra().getEmpresa() != null)
			qry.setLong("idEmpresa", medicao.getObra().getEmpresa().getId());
		if (medicao.getId() != null)
			qry.setLong("id", medicao.getId());
				
		return qry.list().size() > 0;
	}

	private void prepararPaginacao(FiltroMedicao filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoMedicao.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
				criteria.addOrder(Order.asc(filtro.getOrdenacao().getNomeCampo()));
			} else {
				criteria.addOrder(Order.desc(filtro.getOrdenacao().getNomeCampo()));
			}
			criteria.addOrder(Order.desc(OrdenacaoMedicao.DATA.getNomeCampo()));
		}
		
		if (filtro.getPaginacao() != null) {
			criteria.setFirstResult(filtro.getPaginacao().getPrimeiroRegistro());
			criteria.setMaxResults(filtro.getPaginacao().getRegistrosPorPagina());
		}
		
	}

	private void prepararFiltro(Criteria criteria, FiltroMedicao filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getObra() != null && filtro.getObra().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_OBRA + ".id", filtro.getObra().getId()));
		
		if (Boolean.TRUE.equals(filtro.getCanceladas()))
			criteria.add(Restrictions.eq(ALIAS + ".cancelada", Boolean.TRUE));
		
		if (filtro.getDataDe() != null)
			criteria.add(Restrictions.ge(ALIAS + ".data", filtro.getDataDe()));
		if (filtro.getDataAte() != null)
			criteria.add(Restrictions.le(ALIAS + ".data", filtro.getDataAte()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS_OBRA + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".notaFiscal", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
