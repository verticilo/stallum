package stallum.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import stallum.enums.Estado;
import stallum.enums.StatusFuncionario;
import stallum.infra.Util;

@Entity
public class Funcionario extends AbstractModelo {
	private static final long serialVersionUID = -3710362916869875057L;

	private String nome;
	private String apelido;
	@ManyToOne
	private Empresa empresa;
	private BigDecimal salarioBase;
	private BigDecimal anuenioSalario;
	private BigDecimal bonusSalario;
	private BigDecimal anuenioBonus;
	@ManyToOne
	private Funcao funcao;
	private String ctps;
	private String serieCtps;
	@Temporal(TemporalType.DATE)
	private Date dataAdmissao;
	private String rg;
	private String orgaoRg;
	private Estado ufRg;
	@Temporal(TemporalType.DATE)
	private Date dataRg;
	private String nomePai;
	private String nomeMae;
	private String cpf;
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	private String pisPasep;
	private String tsRh;
	@ManyToOne
	private Sindicato sindicato;
	private String email;
	private String dddTelefone;
	private String telefone;
	private String celular;
	private String dddCelular;
	private String obs;
	private StatusFuncionario status;
	@Temporal(TemporalType.DATE)
	private Date dataDemissao;
	private String imagem;
	private String banco;
	private String agencia;
	private String contaCorrente;
	@ManyToOne
	private CentroCusto centroCusto;

	@OneToOne
	private Endereco endereco;
	
	@OneToMany(mappedBy="funcionario")
	private Collection<Dependente> dependentes;
	@OneToMany(mappedBy="funcionario")
	private Collection<Movimento> movimentos;
	
	@Transient
	private BigDecimal bonusNaData;
	@Transient
	private boolean bonificado;
	@Transient
	private int quantDependentes;
	@Transient
	private BigDecimal encargo1;
	@Transient
	private BigDecimal encargo2;
	@Transient
	private BigDecimal salarioFamilia;
	@Transient
	private Integer ordinalStatus;
	@Transient
	private String ultimaObra;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public BigDecimal getSalarioBase() {
		return salarioBase;
	}

	public void setSalarioBase(BigDecimal salarioBase) {
		this.salarioBase = salarioBase;
	}

	public BigDecimal getAnuenioSalario() {
		return anuenioSalario;
	}

	public void setAnuenioSalario(BigDecimal anuenioSalario) {
		this.anuenioSalario = anuenioSalario;
	}

	public BigDecimal getBonusSalario() {
		if (bonusNaData != null)
			return bonusNaData;
		return bonusSalario;
	}

	public void setBonusSalario(BigDecimal bonusSalario) {
		this.bonusSalario = bonusSalario;
	}

	public BigDecimal getAnuenioBonus() {
		return anuenioBonus;
	}

	public void setAnuenioBonus(BigDecimal anuenioBonus) {
		this.anuenioBonus = anuenioBonus;
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
	}

	public String getSerieCtps() {
		return serieCtps;
	}

	public void setSerieCtps(String serieCtps) {
		this.serieCtps = serieCtps;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public void setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getOrgaoRg() {
		return orgaoRg;
	}

	public void setOrgaoRg(String orgaoRg) {
		this.orgaoRg = orgaoRg;
	}

	public Estado getUfRg() {
		return ufRg;
	}

	public void setUfRg(Estado ufRg) {
		this.ufRg = ufRg;
	}

	public Date getDataRg() {
		return dataRg;
	}

	public void setDataRg(Date dataRg) {
		this.dataRg = dataRg;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getPisPasep() {
		return pisPasep;
	}

	public void setPisPasep(String pisPasep) {
		this.pisPasep = pisPasep;
	}

	public String getTsRh() {
		return tsRh;
	}

	public void setTsRh(String tsRh) {
		this.tsRh = tsRh;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getDddTelefone() {
		return dddTelefone;
	}

	public void setDddTelefone(String dddTelefone) {
		this.dddTelefone = dddTelefone;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getDddCelular() {
		return dddCelular;
	}

	public void setDddCelular(String dddCelular) {
		this.dddCelular = dddCelular;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public StatusFuncionario getStatus() {
		if (status == null && ordinalStatus != null)
			return StatusFuncionario.values()[ordinalStatus];
		return status;
	}

	public void setStatus(StatusFuncionario status) {
		this.status = status;
	}

	public Date getDataDemissao() {
		return dataDemissao;
	}

	public void setDataDemissao(Date dataDemissao) {
		this.dataDemissao = dataDemissao;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public Collection<Dependente> getDependentes() {
		return dependentes;
	}

	public void setDependentes(Collection<Dependente> dependentes) {
		this.dependentes = dependentes;
	}
	
	public Collection<Movimento> getMovimentos() {
		return movimentos;
	}

	public void setMovimentos(Collection<Movimento> movimentos) {
		this.movimentos = movimentos;
	}
	
	public BigDecimal getBonusNaData() {
		return bonusNaData;
	}

	public void setBonusNaData(BigDecimal bonusNaData) {
		this.bonusNaData = bonusNaData;
	}

	public boolean isBonificado() {
		return bonificado;
	}

	public void setBonificado(boolean bonificado) {
		this.bonificado = bonificado;
	}

	public int getQuantDependentes() {
		return quantDependentes;
	}

	public void setQuantDependentes(int quantDependentes) {
		this.quantDependentes = quantDependentes;
	}
	
	public BigDecimal getEncargo1() {
		if (encargo1 == null)
			return BigDecimal.ZERO;
		return encargo1;
	}

	public void setEncargo1(BigDecimal encargo1) {
		this.encargo1 = encargo1;
	}

	public BigDecimal getEncargo2() {
		if (encargo2 == null)
			return BigDecimal.ZERO;
		return encargo2;
	}

	public void setEncargo2(BigDecimal encargo2) {
		this.encargo2 = encargo2;
	}

	public BigDecimal getSalarioFamilia() {
		return salarioFamilia;
	}

	public void setSalarioFamilia(BigDecimal salarioFamilia) {
		this.salarioFamilia = salarioFamilia;
	}
	
	public Integer getOrdinalStatus() {
		return ordinalStatus;
	}

	public void setOrdinalStatus(Integer ordinalStatus) {
		this.ordinalStatus = ordinalStatus;
	}

	public String getUltimaObra() {
//		if (ultimaObra != null)
//			return "DE: " + ultimaObra;
		return ultimaObra;
	}

	public void setUltimaObra(String ultimaObra) {
		this.ultimaObra = ultimaObra;
	}

	public BigDecimal getSalarioHora() {
		BigDecimal salFamilia = BigDecimal.ZERO;
		if (getSalarioFamilia() != null)
			salFamilia = getSalarioFamilia().divide(BigDecimal.valueOf(220.0), 2, RoundingMode.HALF_EVEN);			
		return getTotalSalario().add(getTotalBonus()).add(salFamilia);
	}
	
	public BigDecimal getTotalSalario() {
		if (getSalarioBase() == null)
			return BigDecimal.ZERO;
		BigDecimal valAnuenio = BigDecimal.ZERO;		
		if (getAnuenioSalario() != null)
			valAnuenio = getSalarioBase().multiply(getAnuenioSalario().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal valEncargo = getSalarioBase().add(valAnuenio).multiply(getEncargo1().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
		return getSalarioBase().add(valAnuenio).add(valEncargo);
	}
	
	public BigDecimal getTotalBonus() {
		if (getSalarioBase() == null)
			return BigDecimal.ZERO;
		
		BigDecimal aliqBonus = BigDecimal.ZERO;
		if (getBonusSalario() != null)
			aliqBonus = getBonusSalario();
		if (getAnuenioBonus() != null)
			aliqBonus = aliqBonus.add(getAnuenioBonus());		
		BigDecimal valBonus = getSalarioBase().multiply(aliqBonus.divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
		
		BigDecimal valEncargo = valBonus.multiply(getEncargo2().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);

		return valBonus.add(valEncargo);
	}
	
	public BigDecimal getSalarioMes() {
		if (getSalarioBase() == null)
			return BigDecimal.ZERO;
		return getSalarioBase().multiply(BigDecimal.valueOf(220)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getCustoMes() {
		return getSalarioHora().multiply(BigDecimal.valueOf(220)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public String getNomeCurto() {
		if (!Util.isVazioOuNulo(apelido))
			return apelido.toUpperCase();
		
		if (nome != null && nome.length() > 25)
			return nome.substring(0, 25).toUpperCase();
		
		return nome.toUpperCase();
	}

}
