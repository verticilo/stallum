package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroEmpresa {

	private String palavraChave;
	private OrdenacaoEmpresa ordenacao;
	private Paginacao paginacao;

	private List<OrdenacaoEmpresa> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoEmpresa getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoEmpresa ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	
	public List<OrdenacaoEmpresa> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoEmpresa> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoEmpresa ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoEmpresa>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroEmpresa newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoEmpresa.DEFAULT);
	}
	
	public static FiltroEmpresa newInstance(int registrosPorPagina, OrdenacaoEmpresa ordenacaoDefault) {
		FiltroEmpresa instance = new FiltroEmpresa();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
