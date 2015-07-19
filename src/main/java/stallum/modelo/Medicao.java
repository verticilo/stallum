package stallum.modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Medicao extends AbstractModelo {
	private static final long serialVersionUID = -6721259149096880687L;

	@ManyToOne
	private Obra obra;
	@ManyToOne
	private Empresa empresa;
	private String notaFiscal;
	private BigDecimal valor;
	private Date data;
	private BigDecimal iss;
	private BigDecimal inss;
	private BigDecimal clss;
	private BigDecimal irrf;
	private Boolean cancelada;
	
	// Usado para forcar duplicidade da NF
	private transient boolean repetir;

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getIss() {
		return iss;
	}

	public void setIss(BigDecimal iss) {
		this.iss = iss;
	}

	public BigDecimal getInss() {
		return inss;
	}

	public void setInss(BigDecimal inss) {
		this.inss = inss;
	}

	public BigDecimal getClss() {
		return clss;
	}

	public void setClss(BigDecimal clss) {
		this.clss = clss;
	}
	
	public BigDecimal getIrrf() {
		return irrf;
	}

	public void setIrrf(BigDecimal irrf) {
		this.irrf = irrf;
	}

	public Boolean getCancelada() {
		return cancelada;
	}

	public void setCancelada(Boolean cancelada) {
		this.cancelada = cancelada;
	}

	public boolean isRepetir() {
		return repetir;
	}

	public void setRepetir(boolean repetir) {
		this.repetir = repetir;
	}

	public BigDecimal getValorIss() {
		if (getValor() == null || getIss() == null)
			return BigDecimal.ZERO;
		return getValor().multiply(getIss().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
	}	

	public BigDecimal getValorInss() {
		if (getValor() == null || getInss() == null)
			return BigDecimal.ZERO;
		return getValor().multiply(getInss().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getValorClss() {
		if (getValor() == null || getClss() == null)
			return BigDecimal.ZERO;
		return getValor().multiply(getClss().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getValorIrrf() {
		if (getValor() == null || getIrrf() == null)
			return BigDecimal.ZERO;
		return getValor().multiply(getIrrf().divide(BigDecimal.valueOf(100))).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getValorLiquido() {
		if (getValor() == null)
			return BigDecimal.ZERO;
		return getValor().subtract(getValorIss()).subtract(getValorInss()).subtract(getValorClss()).subtract(getValorIrrf());
	}
	
	public String getNomeEmpresa() {
		if (empresa != null)
			return empresa.getNomeCurto();
		return obra.getEmpresa().getNomeCurto();
	}
	
}