package stallum.dao;

import java.util.Collection;
import java.util.Date;

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

import stallum.dto.FiltroDadosGerais;
import stallum.dto.OrdenacaoDadosGerais;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.DadosGerais;
import stallum.modelo.Encargo;
import stallum.modelo.HorarioTrabalho;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DadosGeraisDao extends AbstractDao {

	public static final String ALIAS			= "dado";
	public static final String ALIAS_ENCARGOS	= "enca";
	public static final String ALIAS_EMPRESA	= "empr";
	public static final String ALIAS_SINDICATO	= "sind";
	public static final String ALIAS_HORARIOS	= "hora";
	
	public DadosGeraisDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(DadosGerais.class, ALIAS);
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS + ".encargos", ALIAS_ENCARGOS)
				.createAlias(ALIAS_ENCARGOS + ".empresa", ALIAS_EMPRESA)
				.createAlias(ALIAS_ENCARGOS + ".sindicato", ALIAS_SINDICATO)
				.createAlias(ALIAS + ".horarios", ALIAS_HORARIOS, JoinFragment.LEFT_OUTER_JOIN);
	}
		
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()
				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".data").as("data"))
				.add(Projections.property(ALIAS + ".descricao").as("descricao"))
				.add(Projections.property(ALIAS + ".criadoEm").as("criadoEm"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				
				.add(Projections.property(ALIAS + ".tetoSalFamilia").as("tetoSalFamilia"))
				.add(Projections.property(ALIAS + ".adicDependente").as("adicDependente"))
				.add(Projections.property(ALIAS + ".percAnuenio").as("percAnuenio"))
				.add(Projections.property(ALIAS + ".valeRefeicao").as("valeRefeicao"))
				.add(Projections.property(ALIAS + ".bonificacao").as("bonificacao"))
				.add(Projections.property(ALIAS + ".jornadaDiaMin").as("jornadaDiaMin"))

				.add(Projections.property(ALIAS_ENCARGOS + ".id").as("encargos.Encargo.id"))				
				.add(Projections.property(ALIAS_ENCARGOS + ".aliquota1").as("encargos.Encargo.aliquota1"))
				.add(Projections.property(ALIAS_ENCARGOS + ".aliquota2").as("encargos.Encargo.aliquota2"))
				.add(Projections.property(ALIAS_ENCARGOS + ".alteradoEm").as("encargos.Encargo.alteradoEm"))

				.add(Projections.property(ALIAS_SINDICATO + ".id").as("encargos.Encargo.sindicato.id"))
				.add(Projections.property(ALIAS_SINDICATO + ".nome").as("encargos.Encargo.sindicato.nome"))
				.add(Projections.property(ALIAS_SINDICATO + ".alteradoEm").as("encargos.Encargo.sindicato.alteradoEm"))
				
				.add(Projections.property(ALIAS_EMPRESA + ".id").as("encargos.Encargo.empresa.id"))
				.add(Projections.property(ALIAS_EMPRESA + ".razaoSocial").as("encargos.Encargo.empresa.razaoSocial"))
				.add(Projections.property(ALIAS_EMPRESA + ".nomeCurto").as("encargos.Encargo.empresa.nomeCurto"))
				.add(Projections.property(ALIAS_EMPRESA + ".alteradoEm").as("encargos.Encargo.empresa.alteradoEm"))
				
				.add(Projections.property(ALIAS_HORARIOS + ".id").as("horarios.HorarioTrabalho.id"))				
				.add(Projections.property(ALIAS_HORARIOS + ".diaSemana").as("horarios.HorarioTrabalho.diaSemana"))				
				.add(Projections.property(ALIAS_HORARIOS + ".horaEntrada").as("horarios.HorarioTrabalho.horaEntrada"))				
				.add(Projections.property(ALIAS_HORARIOS + ".horaSaida").as("horarios.HorarioTrabalho.horaSaida"))				
				.add(Projections.property(ALIAS_HORARIOS + ".tempoAlmoco").as("horarios.HorarioTrabalho.tempoAlmoco"))				
				.add(Projections.property(ALIAS_HORARIOS + ".alteradoEm").as("horarios.HorarioTrabalho.alteradoEm"))				
				
				;
	}
	
	private HibernateTransformer getTransformer() {
		HibernateTransformer transformer = new HibernateTransformer(DadosGerais.class, "id");
		transformer.addGroup("Encargo", Encargo.class, "encargos.Encargo.id");
		transformer.addGroup("HorarioTrabalho", HorarioTrabalho.class, "horarios.HorarioTrabalho.id");
		return transformer;
	}
	
	/**
	 * Conulta a DadosGerais no banco pelo id, retornando seus dados completos.
	 * @param idDadosGerais
	 * @return
	 * @throws HibernateException
	 */
	public DadosGerais consultarPorId(Long idDadosGerais) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idDadosGerais));
		
		DadosGerais result = (DadosGerais) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Conulta a DadosGerais no banco pela data, retornando seus dados completos.
	 * @param data
	 * @return
	 * @throws HibernateException
	 */
	@SuppressWarnings("unchecked")
	public DadosGerais consultarPorData(Date data) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(getTransformer());
		
		// Busca da data pra tras
		criteria.add(Restrictions.le(ALIAS + ".data", data));		
		criteria.addOrder(Order.desc(ALIAS + ".data"));
		
		Collection<DadosGerais> result = (Collection<DadosGerais>) criteria.list();
		
		// Se nao encontrar, busca da data pra frente
		if (result == null || result.size() == 0) {
			criteria = getCriteriaConsultar();
			criteria.setProjection(getProjectionConsultar());
			criteria.setResultTransformer(getTransformer());
			criteria.add(Restrictions.ge(ALIAS + ".data", data));		
			criteria.addOrder(Order.asc(ALIAS + ".data"));
			result = (Collection<DadosGerais>) criteria.list();
		}
		
		if (result == null || result.size() == 0)
			return null;
						
		return result.iterator().next();
	}
	
	/**
	 * Busca a DadosGerais pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public DadosGerais buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		DadosGerais result = (DadosGerais) criteria.uniqueResult();
		
		return result;
		
	}
		
	@SuppressWarnings("unchecked")
	public Collection<DadosGerais> filtrar(FiltroDadosGerais filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(getTransformer());
	
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<DadosGerais> result = (Collection<DadosGerais>) criteria.list();
		
		if (!CollectionUtils.isEmpty(result) && filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}	

	public DadosGerais buscarVigente() throws HibernateException {
		return consultarPorData(new Date());
	}

	private void prepararPaginacao(FiltroDadosGerais filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoDadosGerais.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroDadosGerais filtro) {

		if (filtro == null)
			return;
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".descricao", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
				
	}

}
