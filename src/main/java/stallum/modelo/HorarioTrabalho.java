package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import stallum.enums.DiaSemana;
import stallum.infra.Util;

@Entity
public class HorarioTrabalho extends AbstractModelo {
	private static final long serialVersionUID = -5694236187882994502L;

	@ManyToOne
	private DadosGerais dadosGerais;
	
	private DiaSemana diaSemana;
	@Temporal(TemporalType.TIME)
	private Date horaEntrada;
	@Temporal(TemporalType.TIME)
	private Date horaSaida;
	@Temporal(TemporalType.TIME)
	private Date tempoAlmoco;
	
	public DadosGerais getDadosGerais() {
		return dadosGerais;
	}
	
	public void setDadosGerais(DadosGerais dadosGerais) {
		this.dadosGerais = dadosGerais;
	}
	
	public DiaSemana getDiaSemana() {
		return diaSemana;
	}
	
	public void setDiaSemana(DiaSemana diaSemana) {
		this.diaSemana = diaSemana;
	}
	
	public Date getHoraEntrada() {
		return horaEntrada;
	}
	
	public void setHoraEntrada(Date horaEntrada) {
		this.horaEntrada = horaEntrada;
	}
	
	public Date getHoraSaida() {
		return horaSaida;
	}
	
	public void setHoraSaida(Date horaSaida) {
		this.horaSaida = horaSaida;
	}
	
	public Date getTempoAlmoco() {
		return tempoAlmoco;
	}

	public void setTempoAlmoco(Date tempoAlmoco) {
		this.tempoAlmoco = tempoAlmoco;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj))
			return true;
		if (obj == null)
			return false;
		HorarioTrabalho other = (HorarioTrabalho) obj;
		return getDiaSemana().equals(other.getDiaSemana());	
	}

	public BigDecimal getCargaHoraria() {
		BigDecimal dif = Util.diferencaHoras(horaEntrada, horaSaida);
		return dif.subtract(Util.horasEmDecimal(tempoAlmoco));
	}
	
}