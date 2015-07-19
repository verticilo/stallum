package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroCentroCusto {

	private String palavraChave;
	private OrdenacaoCentroCusto ordenacao;
	private Paginacao paginacao;
	private Boolean removidos;

	private List<OrdenacaoCentroCusto> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoCentroCusto getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoCentroCusto ordenacao) {
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
	
	public List<OrdenacaoCentroCusto> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoCentroCusto> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoCentroCusto ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoCentroCusto>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroCentroCusto newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoCentroCusto.DEFAULT);
	}
	
	public static FiltroCentroCusto newInstance(int registrosPorPagina, OrdenacaoCentroCusto ordenacaoDefault) {
		FiltroCentroCusto instance = new FiltroCentroCusto();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
