package stallum.modelo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import stallum.enums.FormaPagto;
import stallum.enums.StatusConta;

@Entity
public class Pagamento extends AbstractModelo {
	private static final long serialVersionUID = 2618947162599175823L;
	
	private Date data;
	@ManyToOne
	private ContaCorrente contaCorrente;
	private FormaPagto formaPagto;
	private String numDocto;
	private BigDecimal totalContas;
	private BigDecimal totalAcrescimos;
	private BigDecimal totalPagto;
	private StatusConta status;
	private String obs;

	@OneToOne
	private MovimentoCaixa movCaixa;

	private transient Collection<ContaPagar> contasPagas;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public ContaCorrente getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrente contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public FormaPagto getFormaPagto() {
		return formaPagto;
	}

	public void setFormaPagto(FormaPagto formaPagto) {
		this.formaPagto = formaPagto;
	}

	public String getNumDocto() {
		return numDocto;
	}

	public void setNumDocto(String numDocto) {
		this.numDocto = numDocto;
	}

	public BigDecimal getTotalContas() {
		if (totalContas == null) {
			totalContas = BigDecimal.ZERO;
			for (ContaPagar cp : contasPagas)
				if (cp.getValor() != null)
					totalContas = totalContas.add(cp.getValor());
		}
		return totalContas;
	}

	public void setTotalContas(BigDecimal totalContas) {
		this.totalContas = totalContas;
	}

	public BigDecimal getTotalAcrescimos() {
		if (totalAcrescimos == null) {
			totalAcrescimos = BigDecimal.ZERO;
			for (ContaPagar cp : contasPagas)
				if (cp.getAcrescimos() != null)
					totalAcrescimos = totalAcrescimos.add(cp.getAcrescimos());
		}
		return totalAcrescimos;
	}

	public void setTotalAcrescimos(BigDecimal totalAcrescimos) {
		this.totalAcrescimos = totalAcrescimos;
	}

	public BigDecimal getTotalPagto() {
		if (totalPagto == null) {
			totalPagto = BigDecimal.ZERO;
			for (ContaPagar cp : contasPagas)
				if (cp.getValorPago() != null)
					totalPagto = totalPagto.add(cp.getValorPago());
				else
					totalPagto = totalPagto.add(cp.getValor());
		}
		return totalPagto;
	}

	public void setTotalPagto(BigDecimal totalPagto) {
		this.totalPagto = totalPagto;
	}

	public StatusConta getStatus() {
		return status;
	}

	public void setStatus(StatusConta status) {
		this.status = status;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Collection<ContaPagar> getContasPagas() {
		return contasPagas;
	}

	public void setContasPagas(Collection<ContaPagar> contasPagas) {
		this.contasPagas = contasPagas;
	}

	public MovimentoCaixa getMovCaixa() {
		return movCaixa;
	}

	public void setMovCaixa(MovimentoCaixa movCaixa) {
		this.movCaixa = movCaixa;
	}

}