package stallum.modelo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DadosGerais extends AbstractModelo {
	private static final long serialVersionUID = 6377871384862829972L;

	public static final int ENCARGO1 = 1;
	public static final int ENCARGO2 = 2;
	
	@Temporal(TemporalType.DATE)
	private Date data;
	private String descricao;
	private BigDecimal tetoSalFamilia;
	private BigDecimal adicDependente;
	private BigDecimal percAnuenio;
	private BigDecimal valeRefeicao;
	private BigDecimal bonificacao;
	private Integer jornadaDiaMin;
	@OneToMany(mappedBy="dadosGerais")
	private Collection<Encargo> encargos;
	@OneToMany(mappedBy="dadosGerais")
	private Collection<HorarioTrabalho> horarios;
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public BigDecimal getTetoSalFamilia() {
		return tetoSalFamilia;
	}

	public void setTetoSalFamilia(BigDecimal tetoSalFamilia) {
		this.tetoSalFamilia = tetoSalFamilia;
	}

	public BigDecimal getPercAnuenio() {
		return percAnuenio;
	}

	public void setPercAnuenio(BigDecimal percAnuenio) {
		this.percAnuenio = percAnuenio;
	}

	public BigDecimal getAdicDependente() {
		return adicDependente;
	}

	public BigDecimal getValeRefeicao() {
		return valeRefeicao;
	}

	public void setValeRefeicao(BigDecimal valeRefeicao) {
		this.valeRefeicao = valeRefeicao;
	}

	public BigDecimal getBonificacao() {
		return bonificacao;
	}

	public void setBonificacao(BigDecimal bonificacao) {
		this.bonificacao = bonificacao;
	}

	public void setAdicDependente(BigDecimal adicDependente) {
		this.adicDependente = adicDependente;
	}

	public Integer getJornadaDiaMin() {
		return jornadaDiaMin;
	}

	public void setJornadaDiaMin(Integer jornadaDiaMin) {
		this.jornadaDiaMin = jornadaDiaMin;
	}

	public Collection<Encargo> getEncargos() {
		return encargos;
	}
	
	public void setEncargos(Collection<Encargo> encargos) {
		this.encargos = encargos;
	}
	
	public Collection<HorarioTrabalho> getHorarios() {
		return horarios;
	}
	
	public void setHorarios(Collection<HorarioTrabalho> horarios) {
		this.horarios = horarios;
	}

	public BigDecimal getCargaHoraria(Date dataApontamento) {
		
		if (getHorarios() != null) {
		
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataApontamento);
			int diaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1;
			
			for (HorarioTrabalho horario: getHorarios()) {
				if (horario.getDiaSemana().ordinal() == diaSemana)
					return horario.getCargaHoraria();
			}
		}
		
		return BigDecimal.ZERO;		
	}

	public BigDecimal getEncargo(Funcionario funcionario, int tipoEncargo) {
		return this.getEncargo(funcionario.getEmpresa(), funcionario.getSindicato(), tipoEncargo);
	}
	
	public BigDecimal getEncargo(Empresa empresa, Sindicato sindicato, int tipoEncargo) {
		
		if (getEncargos() == null)
			return BigDecimal.ZERO;
		
		Encargo encargo = new Encargo();
		encargo.setEmpresa(empresa);
		encargo.setSindicato(sindicato);
		List<Encargo> encs = (List<Encargo>)getEncargos();
		
		int idx = encs.indexOf(encargo);
		if (idx == -1)
			return BigDecimal.ZERO;
		
		if (tipoEncargo == ENCARGO1)
			return encs.get(idx).getAliquota1();
		else
			return encs.get(idx).getAliquota2();
	}

	public BigDecimal getCompensacao(Date data) {
		return getCargaHoraria(data).subtract(BigDecimal.valueOf(getJornadaDiaMin() / 60.0));
	}

	public Date getHoraEntrada(Date dataApont) {
		
		if (getHorarios() != null) {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataApont);
			int diaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1;
			
			for (HorarioTrabalho horario: getHorarios()) {
				if (horario.getDiaSemana().ordinal() == diaSemana)
					return horario.getHoraEntrada();
			}
		}
		
		return null;		
	}
	
	public Date getHoraSaida(Date dataApont) {
		
		if (getHorarios() != null) {
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataApont);
			int diaSemana = cal.get(Calendar.DAY_OF_WEEK) - 1;
			
			for (HorarioTrabalho horario: getHorarios()) {
				if (horario.getDiaSemana().ordinal() == diaSemana)
					return horario.getHoraSaida();
			}
		}
		
		return null;		
	}

	public BigDecimal getJornadaDiaHora() {
		return BigDecimal.valueOf(getJornadaDiaMin() / 60.0);
	}
	
}
