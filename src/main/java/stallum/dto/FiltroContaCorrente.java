package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroContaCorrente {

	private String palavraChave;
	private OrdenacaoContaCorrente ordenacao;
	private Paginacao paginacao;

	private List<OrdenacaoContaCorrente> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoContaCorrente getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoContaCorrente ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public List<OrdenacaoContaCorrente> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoContaCorrente> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoContaCorrente ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoContaCorrente>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroContaCorrente newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoContaCorrente.DEFAULT);
	}

	public static FiltroContaCorrente newInstance(int registrosPorPagina,
			OrdenacaoContaCorrente ordenacaoDefault) {
		FiltroContaCorrente instance = new FiltroContaCorrente();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
