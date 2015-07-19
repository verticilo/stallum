package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Despesa extends AbstractModelo {
	private static final long serialVersionUID = -5241747495533661883L;

	@ManyToOne
	private Obra obra;
	@ManyToOne
	private CentroCusto centroCusto;
	private String descricao;
	private BigDecimal valor;
	private Date data;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public String getOrigem() {
		if (obra == null)
			return centroCusto.getNomeCurto();
		return obra.getNomeCurto();
	}
	
}