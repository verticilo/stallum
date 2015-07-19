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

import stallum.dto.FiltroFornecedor;
import stallum.dto.OrdenacaoFornecedor;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Fornecedor;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FornecedorDao extends AbstractDao {

	public static final String ALIAS = "clie";
	public static final String ALIAS_ENDERECO = "ende";

	public FornecedorDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}

	private Criteria getCriteriaListar() {
		return session.createCriteria(Fornecedor.class, ALIAS);
	}

	private Criteria getCriteriaConsultar() {
		return getCriteriaListar().createAlias(ALIAS + ".endereco", ALIAS_ENDERECO, JoinFragment.LEFT_OUTER_JOIN);
	}

	private ProjectionList getProjectionListar() {
		return Projections
				.projectionList()
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".nomeCurto").as("nomeCurto"))
				.add(Projections.property(ALIAS + ".razaoSocial").as("razaoSocial"))
				.add(Projections.property(ALIAS + ".ddd1").as("ddd1"))
				.add(Projections.property(ALIAS + ".telefone1").as("telefone1"))
				.add(Projections.property(ALIAS + ".email").as("email"))
				.add(Projections.property(ALIAS + ".ativo").as("ativo"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))

		;
	}

	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()

				.add(Projections.property(ALIAS + ".contato").as("contato"))
				.add(Projections.property(ALIAS + ".funcao").as("funcao"))
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

		return session
				.createQuery("select 1 from Fornecedor where email = :email")
				.setParameter("email", email).uniqueResult() != null;
	}

	/**
	 * Conulta a Fornecedor no banco pelo id, retornando seus dados completos.
	 * 
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Fornecedor consultarPorId(Long idFornecedor)
			throws HibernateException {

		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Fornecedor.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", idFornecedor));

		Fornecedor result = (Fornecedor) criteria.uniqueResult();

		return result;
	}

	/**
	 * Busca a Fornecedor pelo id, retornando apenas os dados basicos.
	 * 
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Fornecedor buscarPorId(Long id) throws HibernateException {

		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Fornecedor.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", id));

		Fornecedor result = (Fornecedor) criteria.uniqueResult();

		return result;

	}

	@SuppressWarnings("unchecked")
	public Collection<Fornecedor> filtrar(FiltroFornecedor filtro)
			throws HibernateException {

		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Fornecedor.class, "id"));

		prepararFiltro(criteria, filtro);

		prepararPaginacao(filtro, criteria);

		Collection<Fornecedor> result = (Collection<Fornecedor>) criteria.list();

		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long) criteria.uniqueResult()).intValue());
		}

		return result;
	}

	private void prepararPaginacao(FiltroFornecedor filtro, Criteria criteria) {

		if (filtro == null)
			return;

		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoFornecedor.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroFornecedor filtro) {

		if (filtro == null)
			return;

		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".razaoSocial", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".nomeCurto", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".contato", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".email", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".cnpj", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".telefone1", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}

		if (!Boolean.TRUE.equals(filtro.getRemovidos()))
			criteria.add(Restrictions.eq(ALIAS + ".ativo", Boolean.TRUE));
		else
			criteria.add(Restrictions.ne(ALIAS + ".ativo", Boolean.TRUE));

	}

}
