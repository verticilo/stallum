package stallum.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Encargo extends AbstractModelo {
	private static final long serialVersionUID = -7308230840866022098L;

	@ManyToOne
	private DadosGerais dadosGerais;
	@ManyToOne
	private Empresa empresa;
	@ManyToOne
	private Sindicato sindicato;
	private BigDecimal aliquota1;
	private BigDecimal aliquota2;
	
	public DadosGerais getDadosGerais() {
		return dadosGerais;
	}
	
	public void setDadosGerais(DadosGerais dadosGerais) {
		this.dadosGerais = dadosGerais;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}
	
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public Sindicato getSindicato() {
		return sindicato;
	}
	
	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}
	
	public BigDecimal getAliquota1() {
		return aliquota1;
	}
	
	public void setAliquota1(BigDecimal aliquota1) {
		this.aliquota1 = aliquota1;
	}
	
	public BigDecimal getAliquota2() {
		return aliquota2;
	}
	
	public void setAliquota2(BigDecimal aliquota2) {
		this.aliquota2 = aliquota2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj == null)
			return false;
		Encargo other = (Encargo) obj;
		return getEmpresa().equals(other.getEmpresa()) 
				&& getSindicato().equals(other.getSindicato());		
	}

}