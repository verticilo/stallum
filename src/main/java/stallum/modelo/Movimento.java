package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import stallum.enums.StatusFuncionario;

@Entity
public class Movimento extends AbstractModelo {
	private static final long serialVersionUID = 2404945091244575670L;

	@ManyToOne
	private Funcionario funcionario;
	private Date data;
	@ManyToOne
	private Funcao novaFuncao;
	private StatusFuncionario novoStatus;
	private String motivo;
	private BigDecimal bonus;

	@Transient
	private Date dataFim;

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Funcao getNovaFuncao() {
		return novaFuncao;
	}

	public void setNovaFuncao(Funcao novaFuncao) {
		this.novaFuncao = novaFuncao;
	}

	public StatusFuncionario getNovoStatus() {
		return novoStatus;
	}

	public void setNovoStatus(StatusFuncionario novoStatus) {
		this.novoStatus = novoStatus;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

}