package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import stallum.enums.TipoMovCaixa;

@Entity
public class MovimentoCaixa extends AbstractModelo {
	private static final long serialVersionUID = -6595483879412019927L;

	@ManyToOne
	private ContaCorrente conta;
	private Date data;
	private String documento;
	private TipoMovCaixa tipoMovimento;
	private BigDecimal valor;

	private Long idRegistro;

	public ContaCorrente getConta() {
		return conta;
	}

	public void setConta(ContaCorrente conta) {
		this.conta = conta;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public TipoMovCaixa getTipoMovimento() {
		return tipoMovimento;
	}

	public void setTipoMovimento(TipoMovCaixa tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Long getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(Long idRegistro) {
		this.idRegistro = idRegistro;
	}

}