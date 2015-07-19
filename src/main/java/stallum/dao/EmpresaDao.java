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

import stallum.dto.FiltroEmpresa;
import stallum.dto.OrdenacaoEmpresa;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Empresa;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class EmpresaDao extends AbstractDao {

	public static final String ALIAS			= "empr";
	public static final String ALIAS_ENDERECO	= "ende";
	
	public EmpresaDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Empresa.class, ALIAS);
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS + ".endereco", ALIAS_ENDERECO, JoinFragment.LEFT_OUTER_JOIN);
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()
				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".nomeCurto").as("nomeCurto"))
				.add(Projections.property(ALIAS + ".razaoSocial").as("razaoSocial"))
				.add(Projections.property(ALIAS + ".ddd1").as("ddd1"))
				.add(Projections.property(ALIAS + ".telefone1").as("telefone1"))
				.add(Projections.property(ALIAS + ".email").as("email"))
				.add(Projections.property(ALIAS + ".ativa").as("ativa"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				
				.add(Projections.property(ALIAS + ".cnpj").as("cnpj"))
				.add(Projections.property(ALIAS + ".inscricao").as("inscricao"))
				.add(Projections.property(ALIAS + ".ddd2").as("ddd2"))
				.add(Projections.property(ALIAS + ".telefone2").as("telefone2"))
				
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
			
	public boolean existeEmail(String email) throws HibernateException {
		
		return session.createQuery("select 1 from Empresa where email = :email")
				   	  .setParameter("email", email)
				   	  .uniqueResult() != null;
	}

	/**
	 * Conulta a Empresa no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Empresa consultarPorId(Long idEmpresa) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Empresa.class));
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idEmpresa));
		
		Empresa result = (Empresa) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a Empresa pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Empresa buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Empresa.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Empresa result = (Empresa) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<Empresa> filtrar(FiltroEmpresa filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Empresa.class, "id"));
	
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Empresa> result = (Collection<Empresa>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroEmpresa filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoEmpresa.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroEmpresa filtro) {

		if (filtro == null)
			return;
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".razaoSocial", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".nomeCurto", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".email", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".cnpj", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".telefone1", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
		// Se nao for administrador, so exibe registros ativos.
		if (!sessaoUsuario.isAdministrador())
			criteria.add(Restrictions.eq(ALIAS + ".ativa", Boolean.TRUE));
		
	}

}
