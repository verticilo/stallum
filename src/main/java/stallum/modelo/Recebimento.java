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
public class Recebimento extends AbstractModelo {
	private static final long serialVersionUID = -3473098226524948413L;

	private Date data;
	@ManyToOne
	private ContaCorrente contaCorrente;
	private FormaPagto formaPagto;
	private String numDocto;
	private BigDecimal totalContas;
	private BigDecimal totalAcrescimos;
	private BigDecimal totalRecto;
	private StatusConta status;
	private String obs;

	@OneToOne
	private MovimentoCaixa movCaixa;

	private transient Collection<ContaReceber> contasRecebidas;

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
			for (ContaReceber cr : contasRecebidas)
				if (cr.getValor() != null)
					totalContas = totalContas.add(cr.getValor());
		}
		return totalContas;
	}

	public void setTotalContas(BigDecimal totalContas) {
		this.totalContas = totalContas;
	}

	public BigDecimal getTotalAcrescimos() {
		if (totalAcrescimos == null) {
			totalAcrescimos = BigDecimal.ZERO;
			for (ContaReceber cr : contasRecebidas)
				if (cr.getAcrescimos() != null)
					totalAcrescimos = totalAcrescimos.add(cr.getAcrescimos());
		}
		return totalAcrescimos;
	}

	public void setTotalAcrescimos(BigDecimal totalAcrescimos) {
		this.totalAcrescimos = totalAcrescimos;
	}

	public BigDecimal getTotalRecto() {
		if (totalRecto == null) {
			totalRecto = BigDecimal.ZERO;
			for (ContaReceber cr : contasRecebidas)
				if (cr.getValorRecebido() != null)
					totalRecto = totalRecto.add(cr.getValorRecebido());
				else
					totalRecto = totalRecto.add(cr.getValor());
		}
		return totalRecto;
	}

	public void setTotalRecto(BigDecimal totalRecto) {
		this.totalRecto = totalRecto;
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

	public Collection<ContaReceber> getContasRecebidas() {
		return contasRecebidas;
	}

	public void setContasRecebidas(Collection<ContaReceber> contasRecebidas) {
		this.contasRecebidas = contasRecebidas;
	}

	public MovimentoCaixa getMovCaixa() {
		return movCaixa;
	}

	public void setMovCaixa(MovimentoCaixa movCaixa) {
		this.movCaixa = movCaixa;
	}

}