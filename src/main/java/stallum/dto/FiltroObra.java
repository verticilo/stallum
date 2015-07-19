package stallum.dto;

import java.util.ArrayList;
import java.util.List;

import stallum.modelo.Sindicato;

public class FiltroObra {

	private String palavraChave;
	private OrdenacaoObra ordenacao;
	private Paginacao paginacao;
	private Boolean removidos;
	private Boolean ativas;
	private Sindicato sindicato;

	private List<OrdenacaoObra> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoObra getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoObra ordenacao) {
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
	
	public Boolean getAtivas() {
		return ativas;
	}

	public void setAtivas(Boolean ativas) {
		this.ativas = ativas;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public List<OrdenacaoObra> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoObra> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoObra ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoObra>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroObra newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoObra.DEFAULT);
	}
	
	public static FiltroObra newInstance(int registrosPorPagina, OrdenacaoObra ordenacaoDefault) {
		FiltroObra instance = new FiltroObra();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
