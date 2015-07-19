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

import stallum.dto.FiltroContaGerencial;
import stallum.dto.OrdenacaoUsuario;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.ContaGerencial;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ContaGerencialDao extends AbstractDao {

	public static final String ALIAS = "cont";
	
	public ContaGerencialDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(ContaGerencial.class, ALIAS)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				;
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".numero").as("numero"))
				.add(Projections.property(ALIAS + ".nome").as("nome"))
				.add(Projections.property(ALIAS + ".totalizadora").as("totalizadora"))
				.add(Projections.property(ALIAS + ".classe").as("classe"))
				.add(Projections.property(ALIAS + ".saldo").as("saldo"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()				
				
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(ContaGerencial.class, "id");
		return transformer;
	}
	
	
	
	/**
	 * Conulta a ContaGerencial no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public ContaGerencial consultarPorNumero(String numero) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		criteria.add(Restrictions.eq(ALIAS + ".numero", numero));
		
		ContaGerencial result = (ContaGerencial) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Conulta a ContaGerencial no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public ContaGerencial consultarPorId(Long idContaGerencial) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idContaGerencial));
		
		ContaGerencial result = (ContaGerencial) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca a ContaGerencial pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public ContaGerencial buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		ContaGerencial result = (ContaGerencial) criteria.uniqueResult();
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public Collection<ContaGerencial> filtrar(FiltroContaGerencial filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
				
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<ContaGerencial> result = (Collection<ContaGerencial>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<ContaGerencial> listarPorPrefixo(String prefixo) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.ilike(ALIAS + ".numero", prefixo, MatchMode.START));
		
		Collection<ContaGerencial> result = (Collection<ContaGerencial>) criteria.list();
		
		return result;
	}

	private void prepararPaginacao(FiltroContaGerencial filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoUsuario.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroContaGerencial filtro) {

		if (filtro == null)
			return;
		
		if (filtro.getClasse() != null)
			criteria.add(Restrictions.eq(ALIAS + ".classe", filtro.getClasse()));
		
		if (filtro.getTotalizadoras() != null)
			criteria.add(Restrictions.eq(ALIAS + ".totalizadora", filtro.getTotalizadoras()));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".numero", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
	}

}