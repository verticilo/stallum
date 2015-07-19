package stallum.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class ContaCorrente extends AbstractModelo {
	private static final long serialVersionUID = 7613895702113391027L;

	private String numBanco;
	private String nomeBanco;
	private String numAgencia;
	private String numConta;
	private BigDecimal saldo;

	public String getNumBanco() {
		return numBanco;
	}

	public void setNumBanco(String numBanco) {
		this.numBanco = numBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getNumAgencia() {
		return numAgencia;
	}

	public void setNumAgencia(String numAgencia) {
		this.numAgencia = numAgencia;
	}

	public String getNumConta() {
		return numConta;
	}

	public void setNumConta(String numConta) {
		this.numConta = numConta;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public void debita(BigDecimal valor) {
		if (saldo == null)
			saldo = BigDecimal.ZERO;
		saldo = saldo.subtract(valor);
	}
	
	public void credita(BigDecimal valor) {
		if (saldo == null)
			saldo = BigDecimal.ZERO;
		saldo = saldo.add(valor);
	}

	@Override
	public String toString() {
		return nomeBanco + "/" + numConta;
	}
	
}
