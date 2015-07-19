package stallum.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class Sindicato extends AbstractModelo {
	private static final long serialVersionUID = 3616047030971911480L;

	private String nome;
	private Boolean salarioMinimo;
	private BigDecimal valorBeneficio;	
	private String dataDissidio;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Boolean getSalarioMinimo() {
		return salarioMinimo;
	}

	public void setSalarioMinimo(Boolean salarioMinimo) {
		this.salarioMinimo = salarioMinimo;
	}

	public BigDecimal getValorBeneficio() {
		return valorBeneficio;
	}

	public void setValorBeneficio(BigDecimal valorBeneficio) {
		this.valorBeneficio = valorBeneficio;
	}
		
	public String getDataDissidio() {
		return dataDissidio;
	}

	public void setDataDissidio(String dataDissidio) {
		this.dataDissidio = dataDissidio;
	}

	public String toString() {
		return nome;
	}

}
