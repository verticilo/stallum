package stallum.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Funcao extends AbstractModelo {
	private static final long serialVersionUID = 5327683334466726700L;
	
	private String nome;
	private BigDecimal salario;
	@ManyToOne
	private Sindicato sindicato;
	
	@OneToMany(mappedBy="funcao", cascade=CascadeType.REMOVE)
	private Collection<Dissidio> dissidios;
	
	private transient Date dataUltimoDissidio;
	private transient BigDecimal percUltimoDissidio;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public BigDecimal getSalario() {
		return salario;
	}
	
	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public Collection<Dissidio> getDissidios() {
		return dissidios;
	}

	public void setDissidios(Collection<Dissidio> dissidios) {
		this.dissidios = dissidios;
	}
		
	public void setDataUltimoDissidio(Date dataUltimoDissidio) {
		this.dataUltimoDissidio = dataUltimoDissidio;
	}

	public Date getDataUltimoDissidio() {
		if (dataUltimoDissidio != null)
			return dataUltimoDissidio;
		if (dissidios != null && !dissidios.isEmpty())
			return ((Dissidio)dissidios.toArray()[dissidios.size() - 1]).getData();
		return null;
	}
	
	public void setPercUltimoDissidio(BigDecimal percUltimoDissidio) {
		this.percUltimoDissidio = percUltimoDissidio;
	}
	
	public BigDecimal getPercUltimoDissidio() {
		if (percUltimoDissidio != null)
			return percUltimoDissidio;
		if (dissidios != null && !dissidios.isEmpty())
			return ((Dissidio)dissidios.toArray()[dissidios.size() - 1]).getPercReajuste();
		return null;
	}

	public void addDissidio(Dissidio dissidio) {
		if (dissidios == null)
			dissidios = new ArrayList<Dissidio>();
		dissidios.add(dissidio);
	}
	
	@Override
	public String toString() {
		return getNome() + " - " + getSindicato().getNome();
	}
		
}
