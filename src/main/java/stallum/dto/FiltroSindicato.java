package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroSindicato {

	private String palavraChave;
	private OrdenacaoSindicato ordenacao;
	private Paginacao paginacao;

	private List<OrdenacaoSindicato> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoSindicato getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoSindicato ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	
	public List<OrdenacaoSindicato> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoSindicato> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoSindicato ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoSindicato>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroSindicato newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoSindicato.DEFAULT);
	}
	
	public static FiltroSindicato newInstance(int registrosPorPagina, OrdenacaoSindicato ordenacaoDefault) {
		FiltroSindicato instance = new FiltroSindicato();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
