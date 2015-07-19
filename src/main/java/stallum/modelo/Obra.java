package stallum.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import stallum.enums.StatusObra;

@Entity
public class Obra extends AbstractModelo {
	private static final long serialVersionUID = 9064519591574620376L;

	private String nome;
	@ManyToOne
	private Empresa empresa;
	@ManyToOne
	private Cliente cliente;
	private String contrato;
	private Date dataInicio;
	@ManyToOne
	private Sindicato sindicato;
	@ManyToOne
	private CentroCusto centroCusto;
	private BigDecimal valorInicial = BigDecimal.ZERO;
	private BigDecimal saldoReceber = BigDecimal.ZERO;
	private BigDecimal totalReajustes = BigDecimal.ZERO;
	@OneToOne
	private Endereco endereco;
	private String obs;
	private StatusObra status;
	private Date dataStatus;
	@ManyToOne
	private ContaGerencial contaGerencial;

	private transient BigDecimal totalAditivos = BigDecimal.ZERO;
	private transient BigDecimal totalMedicoes = BigDecimal.ZERO;
	private transient BigDecimal totalDespesas = BigDecimal.ZERO;
	private transient BigDecimal totalCustosIndiretos = BigDecimal.ZERO;
	private transient BigDecimal totalApontamentos = BigDecimal.ZERO;
	private transient BigDecimal mediaFuncionarios = BigDecimal.ZERO;
		
	public String getNomeCurto() {
		if (nome.length() > 20)
			return nome.toUpperCase().substring(0, 20);
		return nome;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getContrato() {
		return contrato;
	}

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}
	
	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}
	
	public BigDecimal getSaldoReceber() {
		if (getValorInicial() == null)
			return saldoReceber;
		return getValorInicial().add(getTotalAditivos()).subtract(getTotalMedicoes());
	}

	public void setSaldoReceber(BigDecimal saldoReceber) {
		this.saldoReceber = saldoReceber;
	}

	public BigDecimal getTotalReajustes() {
		return totalReajustes;
	}

	public void setTotalReajustes(BigDecimal totalReajustes) {
		this.totalReajustes = totalReajustes;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public StatusObra getStatus() {
		return status;
	}

	public void setStatus(StatusObra status) {
		this.status = status;
	}

	public Date getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(Date dataStatus) {
		this.dataStatus = dataStatus;
	}

	public ContaGerencial getContaGerencial() {
		return contaGerencial;
	}

	public void setContaGerencial(ContaGerencial contaGerencial) {
		this.contaGerencial = contaGerencial;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public BigDecimal getTotalObra() {
		if (getValorInicial() == null)
			return null;
		return getValorInicial().add(getTotalAditivos());
	}
	
	public BigDecimal getTotalAditivos() {
		return totalAditivos;
	}

	public void setTotalAditivos(BigDecimal totalAditivos) {
		this.totalAditivos = totalAditivos;
	}

	public BigDecimal getTotalMedicoes() {
		return totalMedicoes;
	}

	public void setTotalMedicoes(BigDecimal totalMedicoes) {
		this.totalMedicoes = totalMedicoes;
	}

	public BigDecimal getTotalDespesas() {
		return totalDespesas;
	}

	public void setTotalDespesas(BigDecimal totalDespesas) {
		this.totalDespesas = totalDespesas;
	}

	public BigDecimal getTotalCustosIndiretos() {
		return totalCustosIndiretos;
	}

	public void setTotalCustosIndiretos(BigDecimal totalCustosIndiretos) {
		this.totalCustosIndiretos = totalCustosIndiretos;
	}

	public BigDecimal getTotalApontamentos() {
		return totalApontamentos;
	}

	public void setTotalApontamentos(BigDecimal totalApontamentos) {
		this.totalApontamentos = totalApontamentos;
	}

	public BigDecimal getMediaFuncionarios() {
		return mediaFuncionarios;
	}

	public void setMediaFuncionarios(BigDecimal mediaFuncionarios) {
		this.mediaFuncionarios = mediaFuncionarios;
	}

	public BigDecimal getResultFuncionario() {
		if (getMediaFuncionarios().equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		return getTotalMedicoes().divide(getMediaFuncionarios(), 2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getResultadoAtual() {
		return getTotalMedicoes().subtract(getCustoTotal());
	}
	
	public BigDecimal getPercResultado() {
		if (getTotalObra() == null || getTotalObra().compareTo(BigDecimal.ZERO) == 0)
			return BigDecimal.ZERO;
		return getResultadoAtual().multiply(BigDecimal.valueOf(100)).divide(getTotalObra(), 2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getPercSaldo() {
		if (getTotalObra() == null || getTotalObra().compareTo(BigDecimal.ZERO) == 0)
			return BigDecimal.ZERO;
		return getSaldoReceber().multiply(BigDecimal.valueOf(100)).divide(getTotalObra(), 2, RoundingMode.HALF_EVEN);
	}

	public BigDecimal getCustoTotal() {
		return getTotalApontamentos().add(getTotalDespesas().add(getTotalCustosIndiretos()));
	}

}