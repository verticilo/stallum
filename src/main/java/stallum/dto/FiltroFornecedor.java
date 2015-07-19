package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroFornecedor {

	private String palavraChave;
	private OrdenacaoFornecedor ordenacao;
	private Paginacao paginacao;
	private Boolean removidos;

	private List<OrdenacaoFornecedor> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoFornecedor getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoFornecedor ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public Boolean getRemovidos() {
		return removidos;
	}

	public void setRemovidos(Boolean removidos) {
		this.removidos = removidos;
	}

	public List<OrdenacaoFornecedor> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoFornecedor> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoFornecedor ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoFornecedor>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroFornecedor newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoFornecedor.DEFAULT);
	}
	
	public static FiltroFornecedor newInstance(int registrosPorPagina, OrdenacaoFornecedor ordenacaoDefault) {
		FiltroFornecedor instance = new FiltroFornecedor();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
