package stallum.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class CentroCusto extends AbstractModelo {
	private static final long serialVersionUID = 2265991299260276809L;

	private String nome;
	@ManyToOne
	private Sindicato sindicato;
	private String obs;
	
	private transient BigDecimal totalDespesas = BigDecimal.ZERO;
	private transient BigDecimal despesasMes = BigDecimal.ZERO;
	private transient BigDecimal totalApontamentos = BigDecimal.ZERO;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public BigDecimal getTotalDespesas() {
		return totalDespesas;
	}

	public void setTotalDespesas(BigDecimal totalDespesas) {
		this.totalDespesas = totalDespesas;
	}

	public BigDecimal getDespesasMes() {
		return despesasMes;
	}

	public void setDespesasMes(BigDecimal despesasMes) {
		this.despesasMes = despesasMes;
	}

	public BigDecimal getTotalApontamentos() {
		return totalApontamentos;
	}

	public void setTotalApontamentos(BigDecimal totalApontamentos) {
		this.totalApontamentos = totalApontamentos;
	}

	public String getNomeCurto() {
		if (nome.length() > 20)
			return nome.substring(0, 20) + "...";
		return nome;
	}
	
}
