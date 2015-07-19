package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import stallum.enums.MotivoFalta;
import stallum.infra.Util;

@Entity
public class Ponto extends AbstractModelo implements Comparable<Ponto> {
	private static final long serialVersionUID = -5145969567461008900L;

	@ManyToOne
	private Apontamento apontamento;
	@ManyToOne
	private Funcionario funcionario;
	@Temporal(TemporalType.TIME)
	private Date horaEntrada;
	@Temporal(TemporalType.TIME)
	private Date horaSaida;
	private BigDecimal horaExtra;
	private BigDecimal percHoraExtra;
	private MotivoFalta motivoFalta;
	private String motivoAbono;
	private Boolean presente;

	private BigDecimal horasNormais = BigDecimal.ZERO;
	private BigDecimal custoHoraNormal = BigDecimal.ZERO;
	private BigDecimal custoHoraExtra = BigDecimal.ZERO;
	private BigDecimal encargos = BigDecimal.ZERO;
	private BigDecimal salarioFamilia = BigDecimal.ZERO;
	private BigDecimal valeBonificacao = BigDecimal.ZERO;
	private BigDecimal custosIndiretos = BigDecimal.ZERO;

	@Transient
	private Integer diasTrab;
	@Transient
	private Integer abonos;
	@Transient
	private Integer faltas;
	@Transient
	private Integer ferias;
	@Transient
	private BigDecimal descontos = BigDecimal.ZERO;
	@Transient
	private BigDecimal compensacao = BigDecimal.ZERO;
	@Transient
	private String strHoraExtra;

	public Apontamento getApontamento() {
		return apontamento;
	}

	public void setApontamento(Apontamento apontamento) {
		this.apontamento = apontamento;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Date getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(Date horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public Date getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(Date horaSaida) {
		this.horaSaida = horaSaida;
	}

	public BigDecimal getHoraExtra() {
		return horaExtra;
	}

	public void setHoraExtra(BigDecimal horaExtra) {
		this.horaExtra = horaExtra;
	}

	public BigDecimal getPercHoraExtra() {
		return percHoraExtra;
	}

	public void setPercHoraExtra(BigDecimal percHoraExtra) {
		this.percHoraExtra = percHoraExtra;
	}

	public MotivoFalta getMotivoFalta() {
		return motivoFalta;
	}

	public void setMotivoFalta(MotivoFalta motivoFalta) {
		this.motivoFalta = motivoFalta;
	}

	public String getMotivoAbono() {
		if (motivoAbono != null)
			return motivoAbono;
		if (getFuncionario().getUltimaObra() != null)
			return getFuncionario().getUltimaObra();
		return null;
	}

	public void setMotivoAbono(String motivoAbono) {
		this.motivoAbono = motivoAbono;
	}

	public Boolean getPresente() {
		return presente;
	}

	public void setPresente(Boolean presente) {
		this.presente = presente;
	}

	public BigDecimal getHorasNormais() {
		return horasNormais;
	}

	public void setHorasNormais(BigDecimal horasNormais) {
		this.horasNormais = horasNormais;
	}

	public BigDecimal getCustoHoraNormal() {
		return custoHoraNormal;
	}

	public void setCustoHoraNormal(BigDecimal custoHoraNormal) {
		this.custoHoraNormal = custoHoraNormal;
	}

	public BigDecimal getCustoHoraExtra() {
		return custoHoraExtra;
	}

	public void setCustoHoraExtra(BigDecimal custoHoraExtra) {
		this.custoHoraExtra = custoHoraExtra;
	}

	public BigDecimal getEncargos() {
		return encargos;
	}

	public void setEncargos(BigDecimal encargos) {
		this.encargos = encargos;
	}

	public BigDecimal getSalarioFamilia() {
		return salarioFamilia;
	}

	public void setSalarioFamilia(BigDecimal salarioFamilia) {
		this.salarioFamilia = salarioFamilia;
	}

	public BigDecimal getValeBonificacao() {
		return valeBonificacao;
	}

	public void setValeBonificacao(BigDecimal valeBonificacao) {
		this.valeBonificacao = valeBonificacao;
	}

	public BigDecimal getCustosIndiretos() {
		return custosIndiretos;
	}

	public void setCustosIndiretos(BigDecimal custosIndiretos) {
		this.custosIndiretos = custosIndiretos;
	}

	public Integer getDiasTrab() {
		return diasTrab;
	}

	public void setDiasTrab(Integer diasTrab) {
		this.diasTrab = diasTrab;
	}

	public Integer getAbonos() {
		return abonos;
	}

	public void setAbonos(Integer abonos) {
		this.abonos = abonos;
	}

	public Integer getFaltas() {
		return faltas;
	}

	public void setFaltas(Integer faltas) {
		this.faltas = faltas;
	}

	public BigDecimal getDescontos() {
		return descontos;
	}

	public void setDescontos(BigDecimal descontos) {
		this.descontos = descontos;
	}

	public BigDecimal getCompensacao() {
		return compensacao;
	}

	public void setCompensacao(BigDecimal compensacao) {
		this.compensacao = compensacao;
	}

	public Integer getFerias() {
		return ferias;
	}

	public void setFerias(Integer ferias) {
		this.ferias = ferias;
	}

	public String getStrHoraExtra() {
		if (horaExtra == null)
			return strHoraExtra;
		return Util.decimalToHora(horaExtra);
	}

	public void setStrHoraExtra(String strHoraExtra) {
		this.strHoraExtra = strHoraExtra;
		this.horaExtra = Util.stringToHora(strHoraExtra);
	}

	public BigDecimal getExtras() {
		return getSalarioFamilia().add(getValeBonificacao());
	}
	
	public String getObs() {
		if (apontamento != null) {
			if (apontamento.getObra() != null)
				return apontamento.getObra().getNomeCurto();
			else
				return apontamento.getCentroCusto().getNomeCurto();
		}				
		return null;
	}

	public BigDecimal getCustoTotal() {
		return getCustoHoraNormal().add(getCustoHoraExtra()).add(getEncargos())
				.add(getExtras());
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj == null)
			return false;
		Ponto other = (Ponto) obj;
		return getFuncionario().equals(other.getFuncionario());
	}

	@Override
	public int compareTo(Ponto o) {
		return this.funcionario.getNome().toLowerCase().trim()
				.compareTo(o.funcionario.getNome().toLowerCase().trim());
	}

}
