package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroDadosGerais {

	private String palavraChave;
	private OrdenacaoDadosGerais ordenacao;
	private Paginacao paginacao;
	private Boolean removidos;

	private List<OrdenacaoDadosGerais> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoDadosGerais getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoDadosGerais ordenacao) {
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

	public List<OrdenacaoDadosGerais> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoDadosGerais> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoDadosGerais ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoDadosGerais>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroDadosGerais newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoDadosGerais.DEFAULT);
	}
	
	public static FiltroDadosGerais newInstance(int registrosPorPagina, OrdenacaoDadosGerais ordenacaoDefault) {
		FiltroDadosGerais instance = new FiltroDadosGerais();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
