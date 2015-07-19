package stallum.modelo;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Bonificacao extends AbstractModelo {
	private static final long serialVersionUID = 2091750901090637600L;

	private Date data;
	private String obs;

	@OneToMany(mappedBy = "bonificacao")
	private Collection<ValeRefeicao> vales;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Collection<ValeRefeicao> getVales() {
		return vales;
	}

	public void setVales(Collection<ValeRefeicao> vales) {
		this.vales = vales;
	}


}