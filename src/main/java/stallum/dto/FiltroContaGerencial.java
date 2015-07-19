package stallum.dto;

import java.util.ArrayList;
import java.util.List;

import stallum.enums.ClasseContaGerencial;

public class FiltroContaGerencial {

	private String palavraChave;
	private ClasseContaGerencial classe;
	private Boolean totalizadoras;
	private OrdenacaoContaGerencial ordenacao;
	private Paginacao paginacao;

	private List<OrdenacaoContaGerencial> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public ClasseContaGerencial getClasse() {
		return classe;
	}

	public void setClasse(ClasseContaGerencial classe) {
		this.classe = classe;
	}

	public Boolean getTotalizadoras() {
		return totalizadoras;
	}

	public void setTotalizadoras(Boolean totalizadoras) {
		this.totalizadoras = totalizadoras;
	}

	public OrdenacaoContaGerencial getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoContaGerencial ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public List<OrdenacaoContaGerencial> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoContaGerencial> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoContaGerencial ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoContaGerencial>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroContaGerencial newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoContaGerencial.DEFAULT);
	}

	public static FiltroContaGerencial newInstance(int registrosPorPagina,
			OrdenacaoContaGerencial ordenacaoDefault) {
		FiltroContaGerencial instance = new FiltroContaGerencial();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
