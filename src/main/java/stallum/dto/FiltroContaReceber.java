package stallum.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stallum.enums.StatusConta;
import stallum.modelo.ContaGerencial;
import stallum.modelo.Fornecedor;

public class FiltroContaReceber {

	private String palavraChave;
	private OrdenacaoContaReceber ordenacao;
	private Paginacao paginacao;
	private ContaGerencial conta;
	private Fornecedor fornecedor;
	private Date dataDe;
	private Date dataAte;
	private List<Long> ids;
	private StatusConta status;
	private Boolean semVencto;

	private List<OrdenacaoContaReceber> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoContaReceber getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoContaReceber ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public ContaGerencial getConta() {
		return conta;
	}

	public void setConta(ContaGerencial conta) {
		this.conta = conta;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public Boolean isSemVencto() {
		return semVencto;
	}

	public void setSemVencto(Boolean semVencto) {
		this.semVencto = semVencto;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	public StatusConta getStatus() {
		return status;
	}

	public void setStatus(StatusConta status) {
		this.status = status;
	}

	public List<OrdenacaoContaReceber> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoContaReceber> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoContaReceber ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoContaReceber>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroContaReceber newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoContaReceber.DEFAULT);
	}

	public static FiltroContaReceber newInstance(int registrosPorPagina,
			OrdenacaoContaReceber ordenacaoDefault) {
		FiltroContaReceber instance = new FiltroContaReceber();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
