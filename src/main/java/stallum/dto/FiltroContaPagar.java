package stallum.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stallum.enums.StatusConta;
import stallum.modelo.ContaGerencial;
import stallum.modelo.Fornecedor;

public class FiltroContaPagar {

	private String palavraChave;
	private OrdenacaoContaPagar ordenacao;
	private Paginacao paginacao;
	private ContaGerencial conta;
	private Fornecedor fornecedor;
	private Date dataDe;
	private Date dataAte;
	private List<Long> ids;
	private StatusConta status;

	private List<OrdenacaoContaPagar> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoContaPagar getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoContaPagar ordenacao) {
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

	public List<OrdenacaoContaPagar> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoContaPagar> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoContaPagar ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoContaPagar>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroContaPagar newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoContaPagar.DEFAULT);
	}

	public static FiltroContaPagar newInstance(int registrosPorPagina,
			OrdenacaoContaPagar ordenacaoDefault) {
		FiltroContaPagar instance = new FiltroContaPagar();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
