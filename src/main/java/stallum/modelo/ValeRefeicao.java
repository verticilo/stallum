package stallum.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ValeRefeicao extends AbstractModelo {
	private static final long serialVersionUID = -5268024749638950479L;
	
	@ManyToOne
	private Bonificacao bonificacao;
	@ManyToOne
	private Funcionario funcionario;
	private Boolean recebe;
	private BigDecimal valor;	
	private String obs;

	public Bonificacao getBonificacao() {
		return bonificacao;
	}

	public void setBonificacao(Bonificacao bonificacao) {
		this.bonificacao = bonificacao;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Boolean getRecebe() {
		return recebe;
	}

	public void setRecebe(Boolean recebe) {
		this.recebe = recebe;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj == null)
			return false;
		ValeRefeicao other = (ValeRefeicao) obj;
		return getFuncionario().equals(other.getFuncionario());
	}

}
