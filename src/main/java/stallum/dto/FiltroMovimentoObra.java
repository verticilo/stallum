package stallum.dto;

import java.util.ArrayList;
import java.util.List;

import stallum.modelo.Obra;

public class FiltroMovimentoObra {

	private String palavraChave;
	private OrdenacaoMovimentoObra ordenacao;
	private Paginacao paginacao;
	private Obra obra;

	private List<OrdenacaoMovimentoObra> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoMovimentoObra getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoMovimentoObra ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	
	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public List<OrdenacaoMovimentoObra> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoMovimentoObra> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoMovimentoObra ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoMovimentoObra>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroMovimentoObra newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoMovimentoObra.DEFAULT);
	}
	
	public static FiltroMovimentoObra newInstance(int registrosPorPagina, OrdenacaoMovimentoObra ordenacaoDefault) {
		FiltroMovimentoObra instance = new FiltroMovimentoObra();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
