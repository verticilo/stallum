package stallum.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.IntegerType;

import stallum.dto.FiltroFuncionario;
import stallum.dto.OrdenacaoFuncionario;
import stallum.enums.StatusFuncionario;
import stallum.infra.HibernateTransformer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Funcao;
import stallum.modelo.Funcionario;
import stallum.relatorio.FuncionarioRelatorio;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FuncionarioDao extends AbstractDao {

	public static final String ALIAS			= "func";
	public static final String ALIAS_EMPRESA 	= "empr";
	public static final String ALIAS_SINDICATO 	= "sind";
	public static final String ALIAS_FUNCAO 	= "fcao";
	public static final String ALIAS_ENDERECO 	= "ende";
	public static final String ALIAS_DEPENDENTE	= "depe";
	
	public FuncionarioDao(Session session, SessaoUsuario sessaoUsuario) {
		super(session, sessaoUsuario);
	}
	
	private Criteria getCriteriaListar() {
		return session.createCriteria(Funcionario.class, ALIAS)
				.createAlias(ALIAS + ".funcao", ALIAS_FUNCAO)
				.createAlias(ALIAS + ".empresa", ALIAS_EMPRESA)
				;
	}
	
	private Criteria getCriteriaConsultar() {
		return getCriteriaListar()
				.createAlias(ALIAS + ".sindicato", ALIAS_SINDICATO)
				.createAlias(ALIAS + ".endereco", ALIAS_ENDERECO, JoinFragment.LEFT_OUTER_JOIN)
				;
	}
	
	private ProjectionList getProjectionListar() {
		return Projections.projectionList()
				
				.add(Projections.property(ALIAS + ".id").as("id"))
				.add(Projections.property(ALIAS + ".nome").as("nome"))
				.add(Projections.property(ALIAS + ".apelido").as("apelido"))
				.add(Projections.property(ALIAS + ".email").as("email"))
				.add(Projections.property(ALIAS + ".status").as("status"))
				.add(Projections.property(ALIAS + ".alteradoEm").as("alteradoEm"))

				.add(Projections.property(ALIAS_FUNCAO + ".id").as("funcao.id"))
				.add(Projections.property(ALIAS_FUNCAO + ".nome").as("funcao.nome"))
				
				.add(Projections.property(ALIAS_EMPRESA + ".nomeCurto").as("empresa.nomeCurto"))
				.add(Projections.property(ALIAS_EMPRESA + ".razaoSocial").as("empresa.razaoSocial"))
				
				;
	}
	
	private ProjectionList getProjectionConsultar() {
		return getProjectionListar()
				
				.add(Projections.property(ALIAS + ".salarioBase").as("salarioBase"))
				.add(Projections.property(ALIAS + ".anuenioSalario").as("anuenioSalario"))
				.add(Projections.property(ALIAS + ".bonusSalario").as("bonusSalario"))
				.add(Projections.property(ALIAS + ".anuenioBonus").as("anuenioBonus"))
				.add(Projections.property(ALIAS + ".ctps").as("ctps"))
				.add(Projections.property(ALIAS + ".serieCtps").as("serieCtps"))
				.add(Projections.property(ALIAS + ".dataAdmissao").as("dataAdmissao"))
				.add(Projections.property(ALIAS + ".rg").as("rg"))
				.add(Projections.property(ALIAS + ".orgaoRg").as("orgaoRg"))
				.add(Projections.property(ALIAS + ".ufRg").as("ufRg"))
				.add(Projections.property(ALIAS + ".dataRg").as("dataRg"))
				.add(Projections.property(ALIAS + ".nomePai").as("nomePai"))
				.add(Projections.property(ALIAS + ".nomeMae").as("nomeMae"))
				.add(Projections.property(ALIAS + ".cpf").as("cpf"))
				.add(Projections.property(ALIAS + ".dataNascimento").as("dataNascimento"))
				.add(Projections.property(ALIAS + ".pisPasep").as("pisPasep"))
				.add(Projections.property(ALIAS + ".tsRh").as("tsRh"))
				.add(Projections.property(ALIAS + ".email").as("email"))
				.add(Projections.property(ALIAS + ".dddTelefone").as("dddTelefone"))
				.add(Projections.property(ALIAS + ".telefone").as("telefone"))
				.add(Projections.property(ALIAS + ".dddCelular").as("dddCelular"))
				.add(Projections.property(ALIAS + ".celular").as("celular"))
				.add(Projections.property(ALIAS + ".dataDemissao").as("dataDemissao"))
				.add(Projections.property(ALIAS + ".banco").as("banco"))
				.add(Projections.property(ALIAS + ".agencia").as("agencia"))
				.add(Projections.property(ALIAS + ".contaCorrente").as("contaCorrente"))
				.add(Projections.property(ALIAS + ".centroCusto").as("centroCusto"))
				.add(Projections.property(ALIAS + ".obs").as("obs"))
				
				.add(Projections.property(ALIAS_SINDICATO + ".id").as("sindicato.id"))
				.add(Projections.property(ALIAS_SINDICATO + ".nome").as("sindicato.nome"))
				.add(Projections.property(ALIAS_SINDICATO + ".alteradoEm").as("sindicato.alteradoEm"))
				
				.add(Projections.property(ALIAS_EMPRESA + ".id").as("empresa.id"))
				.add(Projections.property(ALIAS_EMPRESA + ".alteradoEm").as("empresa.alteradoEm"))
				
				.add(Projections.property(ALIAS_FUNCAO + ".alteradoEm").as("funcao.alteradoEm"))
				
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
		
		return session.createQuery("select 1 from Funcionario where email = :email")
				   	  .setParameter("email", email)
				   	  .uniqueResult() != null;
	}

	/**
	 * Conulta o Funcionario no banco pelo id, retornando seus dados completos.
	 * @param idFuncionario
	 * @return
	 * @throws HibernateException
	 */
	public Funcionario consultarPorId(Long idFuncionario) throws HibernateException {
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar());
		criteria.setResultTransformer(new HibernateTransformer(Funcionario.class));
		
		criteria.add(Restrictions.eq(ALIAS + ".id", idFuncionario));
		
		Funcionario result = (Funcionario) criteria.uniqueResult();
		
		return result;
	}
	
	/**
	 * Busca o Funcionario pelo id, retornando apenas os dados basicos.
	 * @param id
	 * @return Funcionario
	 * @throws HibernateException
	 */
	public Funcionario buscarPorId(Long id) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Funcionario.class));

		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Funcionario result = (Funcionario) criteria.uniqueResult();
		
		return result;		
	}
	
	/**
	 * Busca o Funcionario pelo e-mail, retornando apenas os dados basicos.
	 * @param email
	 * @return Funcionario
	 * @throws HibernateException
	 */
	public Funcionario buscarPorEmail(String email) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(Transformers.aliasToBean(Funcionario.class));

		criteria.add(Restrictions.eq(ALIAS + ".email", email));
		
		Funcionario result = (Funcionario) criteria.uniqueResult();
		
		return result;
	}

	public boolean existeCpf(Funcionario func) {

		String sql = "select count(1) from funcionario f where f.cpf = :cpf ";
		
		if (func.getId() != null)
			sql += " and f.id <> :id ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setString("cpf", func.getCpf());
		if (func.getId() != null)
			qry.setLong("id", func.getId());
		
		BigInteger res = (BigInteger)qry.uniqueResult();
		
		return res.compareTo(BigInteger.ZERO) > 0;
	}

	public boolean existeNome(String nome) {

		String sql = "select count(1) from funcionario f where upper(f.nome) = :nome ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setString("nome", nome.toUpperCase());
		
		BigInteger res = (BigInteger)qry.uniqueResult();
		
		return res.compareTo(BigInteger.ZERO) > 0;
	}
	
	public Funcionario consultarComCusto(Long id, Date dataRef) throws HibernateException {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataRef);
		
		String dia = cal.get(Calendar.DAY_OF_MONTH) + "";
		String mes = (cal.get(Calendar.MONTH) + 1) + "";
		if (dia.length() == 1)
			dia = "0" + dia;
		if (mes.length() == 1)
			mes = "0" + mes;
		String dtRef = dia + " " + mes + " " + cal.get(Calendar.YEAR);
		String dtNasc = dia + " " + mes + " " + (cal.get(Calendar.YEAR) - 14);
		
		String sqlBnd = "(select m.bonus from Movimento m where m.funcionario_id = this_.id and " + 
						"m.data <= to_date('" + dtRef + "', 'DD MM YYYY') and m.bonus is not null order by m.data desc limit 1) as \"bonusNaData\" ";
		
		String sqlBon = "(select 1 from ValeRefeicao v " +
					 	"inner join Bonificacao b on b.id = v.bonificacao_id " +
					 	"where v.funcionario_id = this_.id and " +
					 	"extract(month from b.data) = " + cal.get(Calendar.MONTH) + " limit 1) as \"bonificado\" ";
		
		String sqlDep = "(select count(1) from Dependente d " +
						"where d.funcionario_id = this_.id and d.dataNascimento > to_date('" + dtNasc + "', 'DD MM YYYY') ) as \"quantDependentes\" ";		
		
		Criteria criteria = getCriteriaConsultar();
		criteria.setProjection(getProjectionConsultar()
				.add(Projections.sqlProjection(sqlBnd, new String[] {"bonusNaData"}, new BigDecimalType[]{new BigDecimalType()}))
				.add(Projections.sqlProjection(sqlBon, new String[] {"bonificado"}, new BooleanType[]{new BooleanType()}))
				.add(Projections.sqlProjection(sqlDep, new String[] {"quantDependentes"}, new IntegerType[]{new IntegerType()}))
				);
		
		criteria.setResultTransformer(new HibernateTransformer(Funcionario.class));
		
		criteria.add(Restrictions.eq(ALIAS + ".id", id));
		
		Funcionario result = (Funcionario) criteria.uniqueResult();
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Funcionario> filtrar(FiltroFuncionario filtro) throws HibernateException {
		
		Criteria criteria = getCriteriaListar();
		criteria.setProjection(getProjectionListar());
		criteria.setResultTransformer(new HibernateTransformer(Funcionario.class, "id"));
	
		prepararFiltro(criteria, filtro);
		
		prepararPaginacao(filtro, criteria);
		
		Collection<Funcionario> result = (Collection<Funcionario>) criteria.list();
		
		if (filtro != null && filtro.getPaginacao() != null) {
			criteria = getCriteriaListar();
			criteria.setProjection(Projections.rowCount());
			prepararFiltro(criteria, filtro);
			filtro.getPaginacao().setTotalRegistros(((Long)criteria.uniqueResult()).intValue());
		}
		
		return result;
	}
	
	/**
	 * Lista os funcionarios ativos com data de admissao a partir da data informada.
	 * @param dataApontamento
	 * @return lista de funcionarios ativos
	 * @throws HibernateException
	 */
	@SuppressWarnings("unchecked")
	public Collection<Funcionario> listarAtivos(Date dataApontamento) throws HibernateException {
		
		String sql =	" select f.id, f.nome, f.apelido, f.alteradoEm as \"versao\", f.sindicato_id as \"sindicato.id\", " +
				"   (select m.novoStatus from Movimento m where m.funcionario_id = f.id " +
				"    and m.data <= :dataApont and m.novoStatus <= 2 order by m.data desc limit 1) as \"ordinalStatus\" " + // , " +
//				"   (select o.nome from Ponto p inner join Apontamento a on a.id = p.apontamento_id " +
//				"    inner join Obra o on o.id = a.obra_id where p.funcionario_id = f.id order by a.data desc limit 1) as \"ultimaObra\" " +
				"from Funcionario f " +
//				"inner join Sindicato s on s.id = f.sindicato_id " +
				"  where f.dataAdmissao <= :dataApont " +
				"  and (not exists (select 1 from (select m.novoStatus from Movimento m " +
				"  where m.funcionario_id = f.id and m.data <= :dataApont order by m.data desc limit 1) as aux where novoStatus > 2)) " +
				"order by upper(f.nome) ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setDate("dataApont", dataApontamento);
		qry.setResultTransformer(new HibernateTransformer(Funcionario.class));
		
		Collection<Funcionario> result = (Collection<Funcionario>) qry.list();
		
		return result;
	}
	
	public void atualizaSalario(Funcao funcao) throws HibernateException {

		String sql = "update Funcionario f set f.salarioBase = :salario where f.funcao.id = :idFuncao and f.salarioBase < :salario";
		
		Query qry = session.createQuery(sql);
		qry.setBigDecimal("salario", funcao.getSalario());
		qry.setLong("idFuncao", funcao.getId());
		
		qry.executeUpdate();
		
	}

	private void prepararPaginacao(FiltroFuncionario filtro, Criteria criteria) {
		
		if (filtro == null)
			return;
		
		if (filtro.getOrdenacao() != null) {
			if (OrdenacaoFuncionario.ASCENDENTE == filtro.getOrdenacao().getOrdem()) {
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

	private void prepararFiltro(Criteria criteria, FiltroFuncionario filtro) {

		if (filtro == null)
			return;
		
		if (!Util.isVazioOuNulo(filtro.getPalavraChave())) {
			Disjunction disj2 = Restrictions.disjunction();
			disj2.add(Restrictions.ilike(ALIAS + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".email", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".cpf", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".ctps", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS + ".telefone", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_EMPRESA + ".nomeCurto", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_EMPRESA + ".razaoSocial", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			disj2.add(Restrictions.ilike(ALIAS_FUNCAO + ".nome", filtro.getPalavraChave(), MatchMode.ANYWHERE));
			criteria.add(disj2);
		}
		
		if (Boolean.FALSE.equals(filtro.getRemovidos())) {
			criteria.add(Restrictions.ne(ALIAS + ".status", StatusFuncionario.DEMITIDO));
			criteria.add(Restrictions.ne(ALIAS + ".status", StatusFuncionario.REMOVIDO));
		} else if (Boolean.TRUE.equals(filtro.getRemovidos())) {
			Disjunction disj = Restrictions.disjunction();
			disj.add(Restrictions.eq(ALIAS + ".status", StatusFuncionario.DEMITIDO));
			if (sessaoUsuario.isGerente())
				disj.add(Restrictions.eq(ALIAS + ".status", StatusFuncionario.REMOVIDO));
			criteria.add(disj);
		}
		
		if (filtro.getFuncionarios() != null && filtro.getFuncionarios().size() > 0) {
			Collection<Long> ids = new ArrayList<Long>();
			for (Funcionario funcionario: filtro.getFuncionarios())
				ids.add(funcionario.getId());
			criteria.add(Restrictions.in(ALIAS + ".id", ids));
		}
		
		if (Boolean.TRUE.equals(filtro.getCentroCusto()))
			criteria.add(Restrictions.isNotNull(ALIAS + ".centroCusto"));
			
		
	}

	public StatusFuncionario buscaStatusFuncionario(Long id, Date data) {
		
		String sql = " select m.novoStatus from Movimento m where m.funcionario_id = :id "
				   + " and m.data <= :data order by m.data desc limit 1 ";
		
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setLong("id", id);
		qry.setDate("data", data);
		
		Integer res = (Integer) qry.uniqueResult();
		if (res == null)
			res = 0;
		
		return StatusFuncionario.values()[res];
		
	}

	@SuppressWarnings("unchecked")
	public Collection<Funcionario> listarAtivos(FuncionarioRelatorio relatorio) {

		String sql =	" select f.id, f.nome, f.apelido, f.alteradoEm as \"versao\", f.sindicato_id as \"sindicato.id\", " +
				"   (select m.novoStatus from Movimento m where m.funcionario_id = f.id " +
				"    and m.data <= :dataApont and m.novoStatus <= 2 order by m.data desc limit 1) as \"ordinalStatus\", " +
				"   (select o.nome from Ponto p inner join Apontamento a on a.id = p.apontamento_id " +
				"    inner join Obra o on o.id = a.obra_id where p.funcionario_id = f.id order by a.data desc limit 1) as \"ultimaObra\" " +
				"from Funcionario f " +
				"  where f.sindicato_id = :sindicatoId and f.dataAdmissao <= :dataApont " +
				"  and (not exists (select 1 from (select m.novoStatus from Movimento m " +
				"  where m.funcionario_id = f.id and m.data <= :dataApont order by m.data desc limit 1) as aux where novoStatus > 2)) " +
				"order by upper(f.nome) offset :offset limit :limit ";
		
		String sql2 =	" select count(1) from Funcionario f " +
				"  where f.sindicato_id = :sindicatoId and f.dataAdmissao <= :dataApont " +
				"  and (not exists (select 1 from (select m.novoStatus from Movimento m " +
				"  where m.funcionario_id = f.id and m.data <= :dataApont order by m.data desc limit 1) as aux where novoStatus > 2)) ";
						
		SQLQuery qry = session.createSQLQuery(sql);
		qry.setDate("dataApont", relatorio.getDataDe());
		qry.setLong("sindicatoId", relatorio.getSindicato().getId());
		qry.setInteger("offset", relatorio.getPaginacao().getPrimeiroRegistro());
		qry.setInteger("limit", relatorio.getPaginacao().getRegistrosPorPagina());
		qry.setResultTransformer(new HibernateTransformer(Funcionario.class));
		
		SQLQuery qry2 = session.createSQLQuery(sql2);
		qry2.setDate("dataApont", relatorio.getDataDe());
		qry2.setLong("sindicatoId", relatorio.getSindicato().getId());

		relatorio.getPaginacao().setTotalRegistros(((BigInteger)qry2.uniqueResult()).intValue());
		
		Collection<Funcionario> result = (Collection<Funcionario>) qry.list();
		
		return result;
	}

}
