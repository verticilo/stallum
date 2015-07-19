package stallum.modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import stallum.enums.StatusObra;

@Entity
public class MovimentoObra extends AbstractModelo {
	private static final long serialVersionUID = -3799567306419130036L;

	@ManyToOne
	private Obra obra;
	private Date data;
	private StatusObra novoStatus;
	private String motivo;

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public StatusObra getNovoStatus() {
		return novoStatus;
	}

	public void setNovoStatus(StatusObra novoStatus) {
		this.novoStatus = novoStatus;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}