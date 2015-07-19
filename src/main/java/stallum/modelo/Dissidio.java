package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Dissidio extends AbstractModelo {
	private static final long serialVersionUID = 8939278226705381203L;
	
	@ManyToOne
	private Funcao funcao;
	private Date data;
	private BigDecimal percReajuste;
	private BigDecimal salario;
	
	private transient BigDecimal beneficio;
	
	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getPercReajuste() {
		return percReajuste;
	}

	public void setPercReajuste(BigDecimal percReajuste) {
		this.percReajuste = percReajuste;
	}

	public BigDecimal getSalario() {
		return salario;
	}

	public void setSalario(BigDecimal salario) {
		this.salario = salario;
	}
	
	public BigDecimal getBeneficio() {
		return beneficio;
	}

	public void setBeneficio(BigDecimal beneficio) {
		this.beneficio = beneficio;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj == null)
			return false;
		Dissidio other = (Dissidio) obj;
		return getFuncao().equals(other.getFuncao()) 
				&& getData().equals(other.getData());		
	}
	
}
