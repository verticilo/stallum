package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import stallum.enums.StatusConta;
import stallum.enums.TipoDocumento;

@Entity
public class ContaReceber extends AbstractModelo {
	private static final long serialVersionUID = -6612334678334233181L;

	@ManyToOne
	private ContaGerencial conta;
	private Date data;
	@ManyToOne
	private Cliente cliente;
	private TipoDocumento tipo;
	private String numero;
	@ManyToOne
	private ContaGerencial origem;
	private BigDecimal valor;
	private Date vencto;
	private Date dataRecto;
	private BigDecimal acrescimos;
	private BigDecimal valorRecebido;
	private StatusConta status;
	private String obs;

	@ManyToOne
	private Recebimento recebimento;

	private transient boolean marcada;

	public ContaGerencial getConta() {
		return conta;
	}

	public void setConta(ContaGerencial conta) {
		this.conta = conta;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoDocumento getTipo() {
		return tipo;
	}

	public void setTipo(TipoDocumento tipo) {
		this.tipo = tipo;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public ContaGerencial getOrigem() {
		return origem;
	}

	public void setOrigem(ContaGerencial origem) {
		this.origem = origem;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getVencto() {
		return vencto;
	}

	public void setVencto(Date vencto) {
		this.vencto = vencto;
	}

	public Date getDataRecto() {
		return dataRecto;
	}

	public void setDataRecto(Date dataRecto) {
		this.dataRecto = dataRecto;
	}

	public BigDecimal getAcrescimos() {
		return acrescimos;
	}

	public void setAcrescimos(BigDecimal acrescimos) {
		this.acrescimos = acrescimos;
	}

	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public StatusConta getStatus() {
		return status;
	}

	public void setStatus(StatusConta status) {
		this.status = status;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Recebimento getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}

	public boolean isMarcada() {
		return marcada;
	}

	public void setMarcada(boolean marcada) {
		this.marcada = marcada;
	}

}