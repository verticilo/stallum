package stallum.dao;

import java.math.BigInteger;
import java.util.Calendar;
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
import org.hibernate.transform.Transformers;

import stallum.dto.FiltroUsuario;
import stallum.dto.OrdenacaoUsuario;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Bonificacao;
import stallum.modelo.Usuario;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class UsuarioDao extends AbstractDao {

	public static final String ALIAS			= "usua";
	public static final String ALIAS_ENDERECO 	= "ende";
	
	public UsuarioDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Usuario.class, ALIAS);
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS + ".endereco", ALIAS_ENDERECO, JoinFragment.LEFT_OUTER_JOIN);
	}
	
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()
				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".nome").as("nome"))
				.add(Projections.property(ALIAS + ".apelido").as("apelido"))
				.add(Projections.property(ALIAS + ".email").as("email"))
				.add(Projections.property(ALIAS + ".bloqueado").as("bloqueado"))
				.add(Projections.property(ALIAS + ".perfil").as("perfil"))
				.add(Projections.property(ALIAS + ".confirmacaoEmail").as("confirmacaoEmail"))
				.add(Projections.property(ALIAS + ".ultimoAcesso").as("ultimoAcesso"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				
				.add(Projections.property(ALIAS + ".imagem").as("imagem"))
				.add(Projections.property(ALIAS + ".cpf").as("cpf"))
				.add(Projections.property(ALIAS + ".rg").as("rg"))
				.add(Projections.property(ALIAS + ".dddTelefone").as("dddTelefone"))
				.add(Projections.property(ALIAS + ".telefone").as("telefone"))
				.add(Projections.property(ALIAS + ".dddCelular").as("dddCelular"))
				.add(Projections.property(ALIAS + ".celular").as("celular"))
				.add(Projections.property(ALIAS + ".dataNascimento").as("dataNascimento"))
				.add(Projections.property(ALIAS + ".newsletter").as("newsletter"))
				.add(Projections.property(ALIAS + ".lembrar").as("lembrar"))
				
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
	
	public Usuario autenticarUsuario(String login, String senha) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Usuario.class));
		
		criteria.add(Restrictions.eq("email", login))
				.add(Restrictions.eq("senha", senha));
		
		Usuario result = (Usuario) criteria.uniqueResult();
		
		// Se o usuario for autenticado com sucesso, atualiza a data do ultimo acesse
		if (result != null) {
			session.createQuery("update Usuario set ultimoAcesso = :dataAtual where id = :id")
				   .setParameter("dataAtual", new Date())
				   .setParameter("id", result.getId())
				   .executeUpdate();
		}
		
		return result;
	}

	public Usuario autenticarUsuario(Long id, String senha) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Usuario.class));
		
		criteria.add(Restrictions.eq("id", id))
				.add(Restrictions.eq("senha", senha));
		
		Usuario result = (Usuario) criteria.uniqueResult();
		
		// Se o usuario for autenticado com sucesso, atualiza a data do ultimo acesse
		if (result != null) {
			session.createQuery("update Usuario set ultimoAcesso = :dataAtual where id = :id")
			.setParameter("dataAtual", new Date())
			.setParameter("id", result.getId())
			.executeUpdate();
		}
		
		return result;
	}
	
	public boolean existeEmail(String email) throws HibernateException {
		
		return session.createQuery("select 1 from Usuario where email = :email")
				   	  .setParameter("email", email)
				   	  .uniqueResult() != null;
	}

	/**
	 * Conulta o Usuario no banco pelo id, retornando seus dados completos.
	 * @param idUsuario
	 * @return
	 * @throws HibernateException
	 */
	public Usuario consultarPorId(Long idUsuario) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Usuario.class));
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idUsuario));
		
		Usuario result = (Usuario) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca o Usuario pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Usuario buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Usuario.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Usuario result = (Usuario) criteria.uniqueResult();
		
		return result;		
	}
	
	/**
	 * Busca o Usuario pelo e-mail, retornando apenas os dados basicos.
	 * @param email
	 * @return Usuario
	 * @throws HibernateException
	 */
	public Usuario buscarPorEmail(String email) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(Transformers.aliasToBean(Usuario.class));

		criteria.add(Restrictions.eq(ALIAS + ".email", email));
		
		Usuario result = (Usuario) criteria.uniqueResult();
		
		return result;
	}
	
	public void alterarUsuario(Usuario usuario) throws HibernateException {
		
		StringBuilder hql = new StringBuilder("update Usuario set ");
		
		hql.append(" nome = :nome, ");
		hql.append(" email = :email, ");
		hql.append(" apelido = :apelido, ");
		hql.append(" dataNascimento = :dataNascimento, ");
		hql.append(" cpf = :cpf, ");
		hql.append(" rg = :rg, ");		
		hql.append(" dddTelefone = :dddTelefone, ");
		hql.append(" telefone = :telefone, ");
		hql.append(" dddCelular = :dddCelular, ");
		hql.append(" celular = :celular, ");
		hql.append(" bloqueado = :bloqueado ");
		
		if (!Util.isVazioOuNulo(usuario.getPerfil()))
			hql.append(", perfil = :perfil ");
		if (usuario.getNewsletter() != null)
			hql.append(", newsletter = :newsletter ");
		if (usuario.getConfirmacaoEmail() != null)
			hql.append(", confirmacaoEmail = :confirmacaoEmail ");
		if (!Util.isVazioOuNulo(usuario.getSenha()))
			hql.append(", senha = :senha ");

		hql.append(" where id = :id ");
		
		Query qry = getSession().createQuery(hql.toString());
		
		qry.setString("nome", usuario.getNome());
		qry.setString("email", usuario.getEmail());
		qry.setString("apelido", usuario.getApelido());
		qry.setDate("dataNascimento", usuario.getDataNascimento());
		qry.setString("cpf", usuario.getCpf());
		qry.setString("rg", usuario.getRg());
		qry.setString("dddTelefone", usuario.getDddTelefone());
		qry.setString("telefone", usuario.getTelefone());
		qry.setString("dddCelular", usuario.getDddCelular());
		qry.setString("celular", usuario.getCelular());
		qry.setBoolean("bloqueado", Boolean.TRUE.equals(usuario.getBloqueado()));
		
		if (!Util.isVazioOuNulo(usuario.getPerfil()))
			qry.setInteger("perfil", usuario.getPerfil().ordinal());
		if (usuario.getNewsletter() != null)
			qry.setBoolean("newsletter", Boolean.TRUE.equals(usuario.getNewsletter()));
		if (usuario.getConfirmacaoEmail() != null)
			qry.setLong("confirmacaoEmail", usuario.getConfirmacaoEmail());
		if (!Util.isVazioOuNulo(usuario.getSenha()))
			qry.setString("senha", usuario.getSenha());
		
		qry.setLong("id", usuario.getId());
		
		qry.executeUpdate();
	}

	public boolean trocarSenha(Usuario usuario) throws HibernateException {
		
		String hql = "update Usuario set senha = :senha where id = :id";
		
		Query qry = getSession().createQuery(hql);
		qry.setString("senha", usuario.getSenha());
		qry.setLong("id", usuario.getId());
		
		return qry.executeUpdate() == 1;
	}

	public boolean bloquearUsuario(Long idUsuario) throws HibernateException {
		
		String hql = "update Usuario set bloqueado = :bloqueado where id = :id";
		
		Query qry = getSession().createQuery(hql);
		qry.setBoolean("bloqueado", Boolean.TRUE);
		qry.setLong("id", idUsuario);
		
		return qry.executeUpdate() == 1;
	}

	public boolean confirmarEmail(Usuario usuario) throws HibernateException {
		
		String hql = "update Usuario set bloqueado = :bloqueado, confirmacaoEmail = :confirmacaoEmail, perfil = :perfil where id = :id";
		
		Query qry = getSession().createQuery(hql);
		qry.setBoolean("bloqueado", Boolean.FALSE);
		qry.setLong("confirmacaoEmail", 1L);
		qry.setInteger("perfil", usuario.getPerfil().ordinal());
		qry.setLong("id", usuario.getId());
		
		return qry.executeUpdate() == 1;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Usuario> filtrar(FiltroUsuario filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Usuario.class, "id"));
	
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Usuario> result = (Collection<Usuario>) criteria.list();
		
		if (filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}

	private void prepararPaginacao(FiltroUsuario filtro, Criteria criteria) {
		
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

	private void prepararFiltro(Criteria criteria, FiltroUsuario filtro) {

		if (filtro == null)
			return;
		
		criteria.add(Restrictions.ne(ALIAS + ".bloqueado", Boolean.TRUE));
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".email", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".cpf", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".telefone", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
		if (filtro.getPerfilMin() != null)
			criteria.add(Restrictions.ge(ALIAS + ".perfil", filtro.getPerfilMin()));
		
	}

	@SuppressWarnings("unchecked")
	public Collection<Usuario> listarEmailMarketing()  throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Usuario.class, "id"));
	
		criteria.add(Restrictions.eq(ALIAS + ".newsletter", Boolean.TRUE));
		
		Collection<Usuario> result = (Collection<Usuario>) criteria.list();
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean checarBonificacao() throws HibernateException {

		String sql = " select id from Bonificacao b where b.data >= :dataIni and b.data <= :dataFim"; 
		Calendar cal = Calendar.getInstance();
		SQLQuery qry = session.createSQLQuery(sql);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DAY_OF_MONTH));
		qry.setDate("dataIni", Util.diaSemHora(cal.getTime()));
		cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
		qry.setDate("dataFim", Util.diaCompleto(cal.getTime()));
		
		qry.setResultTransformer(new HibernateTransformer(Bonificacao.class, "id"));
		
		Collection<Bonificacao> lista = (Collection<Bonificacao>) qry.list();	
		
		return !CollectionUtils.isEmpty(lista);
		
	}
	
	public boolean checarDissidio() throws HibernateException {
		
		String sql = 	" select count(1) from sindicato where not exists " +
						" (select s.id from Dissidio d " +
						" inner join Funcao f on f.id = d.funcao_id " +
						" inner join Sindicato s on s.id = f.sindicato_id " +
						" where d.data >= to_date(replace(s.dataDissidio,'/','-') || '-' || to_char(extract(year from now()), '9999'), 'DD-MM-YYYY')" + 
						" and to_date(replace(s.dataDissidio,'/','-') || '-' || to_char(extract(year from now()), '9999'), 'DD-MM-YYYY') < now())";
		
		SQLQuery qry = session.createSQLQuery(sql);
		
		BigInteger result = (BigInteger)qry.uniqueResult();
		
		return result.compareTo(BigInteger.ZERO) == 0;
		
	}

}
