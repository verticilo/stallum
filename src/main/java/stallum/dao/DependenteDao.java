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

import stallum.dto.FiltroDependente;
import stallum.dto.OrdenacaoDependente;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Dependente;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DependenteDao extends AbstractDao {

	public static final String ALIAS				= "depe";
	public static final String ALIAS_FUNCIONARIO	= "func";
	
	public DependenteDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Dependente.class, ALIAS)
				.createAlias(ALIAS + ".funcionario", ALIAS_FUNCIONARIO);
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar();
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()
				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".nome").as("nome"))
				.add(Projections.property(ALIAS + ".dataNascimento").as("dataNascimento"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				.add(Projections.property(ALIAS_FUNCIONARIO + ".id").as("funcionario.id"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".nome").as("funcionario.nome"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".apelido").as("funcionario.apelido"))
				.add(Projections.property(ALIAS_FUNCIONARIO + ".alteradoEm").as("funcionario.alteradoEm"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				;
	}
		
	
	/**
	 * Conulta a Dependente no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Dependente consultarPorId(Long idDependente) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Dependente.class));
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idDependente));
		
		Dependente result = (Dependente) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Dependente pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Dependente buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Dependente.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Dependente result = (Dependente) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Dependente> filtrar(FiltroDependente filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Dependente.class, "id"));
	
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Dependente> result = (Collection<Dependente>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroDependente filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoDependente.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
				criteria.addOrder(Order.asc(filtro.getOrdenacao().getNomeCampo()));
			} else {
				criteria.addOrder(Order.desc(filtro.getOrdenacao().getNomeCampo()));
			}
			// Sempre ordenando por data de nascimento
			criteria.addOrder(Order.asc(OrdenacaoDependente.DATA_NASCIMENTO.getNomeCampo()));
		}
		
		if (filtro.getPaginacao() != null) {
			criteria.setFirstResult(filtro.getPaginacao().getPrimeiroRegistro());
			criteria.setMaxResults(filtro.getPaginacao().getRegistrosPorPagina());
		}
		
	}

	private void prepararFiltro(Criteria criteria, FiltroDependente filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getFuncionario() != null && filtro.getFuncionario().getId() != null)
			criteria.add(Restrictions.eq(ALIAS_FUNCIONARIO + ".id", filtro.getFuncionario().getId()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}
