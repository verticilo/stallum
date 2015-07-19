package stallum.modelo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import stallum.enums.StatusConta;
import stallum.enums.TipoDocumento;

@Entity
public class ContaPagar extends AbstractModelo {
	private static final long serialVersionUID = -533200417393393492L;

	@ManyToOne
	private ContaGerencial conta;
	private Date data = new Date();
	@ManyToOne
	private Fornecedor fornecedor;
	private TipoDocumento tipo;
	private String numero;
	@ManyToOne
	private ContaGerencial origem;
	private BigDecimal valor;
	private Date vencto;
	private Date dataPagto;
	private BigDecimal acrescimos;
	private BigDecimal valorPago;
	private StatusConta status;
	private String obs;

	@ManyToOne
	private Pagamento pagamento;

	private transient boolean marcada;
	private transient Integer parcs;
	private transient Integer interv;

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

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
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

	public Date getDataPagto() {
		return dataPagto;
	}

	public void setDataPagto(Date dataPagto) {
		this.dataPagto = dataPagto;
	}

	public BigDecimal getAcrescimos() {
		return acrescimos;
	}

	public void setAcrescimos(BigDecimal acrescimos) {
		this.acrescimos = acrescimos;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
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

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public boolean isMarcada() {
		return marcada;
	}

	public void setMarcada(boolean marcada) {
		this.marcada = marcada;
	}

	public Integer getParcs() {
		return parcs;
	}

	public void setParcs(Integer parcs) {
		this.parcs = parcs;
	}

	public Integer getInterv() {
		return interv;
	}

	public void setInterv(Integer interv) {
		this.interv = interv;
	}
	

}