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

import stallum.dto.FiltroBonificacao;
import stallum.dto.OrdenacaoBonificacao;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Bonificacao;
import stallum.modelo.ValeRefeicao;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class BonificacaoDao extends AbstractDao {

	public static final String ALIAS 				= "boni";
	public static final String ALIAS_VALE			= "vale";
	public static final String ALIAS_FUNCIONARIO	= "func";
	
	public BonificacaoDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Bonificacao.class, ALIAS)
				.createAlias(ALIAS + ".vales", ALIAS_VALE, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS_VALE + ".funcionario", ALIAS_FUNCIONARIO, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()			
				.add(Projections.property(ALIAS + ".obs").as("obs"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_VALE + ".id").as("vales.ValeRefeicao.id"))
				.add(Projections.property(ALIAS_VALE + ".recebe").as("vales.ValeRefeicao.recebe"))
				.add(Projections.property(ALIAS_VALE + ".valor").as("vales.ValeRefeicao.valor"))
				.add(Projections.property(ALIAS_VALE + ".obs").as("vales.ValeRefeicao.obs"))
				.add(Projections.property(ALIAS_VALE + ".alteradoEm").as("vales.ValeRefeicao.alteradoEm"))
				
				.add(Projections.property(ALIAS_FUNCIONARIO + ".id").as("vales.ValeRefeicao.funcionario.id"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".nome").as("vales.ValeRefeicao.funcionario.nome"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".apelido").as("vales.ValeRefeicao.funcionario.apelido"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".alteradoEm").as("vales.ValeRefeicao.funcionario.alteradoEm"))
				;
	}
	
	private HibernateTransformer getTransformerListar() {
		HibernateTransformer transformer = new HibernateTransformer(Bonificacao.class, "id");
		return transformer;
	}
	
	private HibernateTransformer getTransformerConsultar() {
		HibernateTransformer transformer = getTransformerListar();
		transformer.addGroup("ValeRefeicao", ValeRefeicao.class, "vales.ValeRefeicao.id");
		return transformer;
	}
		
	/**
	 * Busca o Bonificacao pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Bonificacao
	 * @throws HibernateException
	 */
	public Bonificacao buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformerListar());

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Bonificacao result = (Bonificacao) criteria.uniqueResult();
		
		return result;
		
	}

	public Bonificacao consultarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformerConsultar());
				
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Bonificacao result = (Bonificacao) criteria.uniqueResult();
		
		return result;
	}
	

	public Bonificacao conultarPorData(Bonificacao bonificacao) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformerConsultar());
				
		criteria.add(Restrictions.eq(ALIAS + ".data", bonificacao.getData()));
		criteria.add(Restrictions.eq(ALIAS_VALE + ".recebe", Boolean.TRUE));
		
		Bonificacao result = (Bonificacao) criteria.uniqueResult();
		
		return result;
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Bonificacao> filtrar(FiltroBonificacao filtro) throws HibernateException {
		
		Criteria criteria = null;		
		criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());		
		criteria.setResultTransformer(getTransformerListar());			
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Bonificacao> result = (Collection<Bonificacao>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroBonificacao filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoBonificacao.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroBonificacao filtro) {

		if (filtro == null)
			return;
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS_FUNCIONARIO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_FUNCIONARIO + ".apelido", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
