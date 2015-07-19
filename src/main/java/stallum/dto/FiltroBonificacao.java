package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroBonificacao {

	private String palavraChave;
	private OrdenacaoBonificacao ordenacao;
	private Paginacao paginacao;

	private List<OrdenacaoBonificacao> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoBonificacao getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoBonificacao ordenacao) {
		this.ordenacao = ordenacao;
	}
	
	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public List<OrdenacaoBonificacao> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoBonificacao> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoBonificacao ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoBonificacao>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroBonificacao newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoBonificacao.DEFAULT);
	}
	
	public static FiltroBonificacao newInstance(int registrosPorPagina, OrdenacaoBonificacao ordenacaoDefault) {
		FiltroBonificacao instance = new FiltroBonificacao();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
