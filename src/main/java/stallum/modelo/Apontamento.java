package stallum.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import stallum.infra.Util;

@Entity
public class Apontamento extends AbstractModelo {
	private static final long serialVersionUID = -1486874111967900628L;

	private Date data;
	@ManyToOne
	private Obra obra;
	@ManyToOne
	private CentroCusto centroCusto;
	private Boolean feriado;
	private String obs;

	transient private Sindicato sindicato;
	transient private BigDecimal custoIndiretoHH;
	
	@OneToMany(mappedBy = "apontamento")
	private Collection<Ponto> pontos = new ArrayList<Ponto>();
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	public Boolean getFeriado() {
		return feriado;
	}

	public void setFeriado(Boolean feriado) {
		this.feriado = feriado;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Sindicato getSindicato() {
		if (sindicato != null)
			return sindicato;
		if (getObra() != null && getObra().getSindicato() != null)
			return getObra().getSindicato();
		if (getCentroCusto() != null && getCentroCusto().getSindicato() != null)
			return getCentroCusto().getSindicato();
		return null;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public BigDecimal getCustoIndiretoHH() {
		return custoIndiretoHH;
	}

	public void setCustoIndiretoHH(BigDecimal custoIndiretoHH) {
		this.custoIndiretoHH = custoIndiretoHH;
	}

	public Collection<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(Collection<Ponto> pontos) {
		this.pontos = pontos;
	}

	public String getNomeCurto() {
		if (obra != null)
			return obra.getNomeCurto();
		return centroCusto.getNomeCurto();
	}
	
	public void setStrData(String strData) {
		setData(Util.stringToData(strData));
	}
	
	public String getStrData() {
		return Util.dataToString(data).replaceAll("/", "-");
	}
	
}