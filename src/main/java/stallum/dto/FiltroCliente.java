package stallum.dto;

import java.util.ArrayList;
import java.util.List;

public class FiltroCliente {

	private String palavraChave;
	private OrdenacaoCliente ordenacao;
	private Paginacao paginacao;
	private Boolean removidos;

	private List<OrdenacaoCliente> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoCliente getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoCliente ordenacao) {
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

	public List<OrdenacaoCliente> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoCliente> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoCliente ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoCliente>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroCliente newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoCliente.DEFAULT);
	}
	
	public static FiltroCliente newInstance(int registrosPorPagina, OrdenacaoCliente ordenacaoDefault) {
		FiltroCliente instance = new FiltroCliente();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
