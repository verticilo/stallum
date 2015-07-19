package stallum.dto;

import java.util.ArrayList;
import java.util.List;

import stallum.modelo.Sindicato;

public class FiltroFuncao {

	private String palavraChave;
	private OrdenacaoFuncao ordenacao;
	private Paginacao paginacao;
	private Sindicato sindicato;

	private List<OrdenacaoFuncao> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoFuncao getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoFuncao ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	
	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
	}

	public List<OrdenacaoFuncao> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoFuncao> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoFuncao ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoFuncao>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroFuncao newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoFuncao.DEFAULT);
	}
	
	public static FiltroFuncao newInstance(int registrosPorPagina, OrdenacaoFuncao ordenacaoDefault) {
		FiltroFuncao instance = new FiltroFuncao();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
