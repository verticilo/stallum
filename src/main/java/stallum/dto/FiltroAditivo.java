package stallum.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stallum.modelo.Obra;

public class FiltroAditivo {

	private String palavraChave;
	private OrdenacaoAditivo ordenacao;
	private Paginacao paginacao;
	private Obra obra;
	private Date dataDe;
	private Date dataAte;

	private List<OrdenacaoAditivo> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoAditivo getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoAditivo ordenacao) {
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

	public Date getDataDe() {
		return dataDe;
	}

	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}

	public Date getDataAte() {
		return dataAte;
	}

	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}

	public List<OrdenacaoAditivo> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoAditivo> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoAditivo ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoAditivo>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroAditivo newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoAditivo.DEFAULT);
	}
	
	public static FiltroAditivo newInstance(int registrosPorPagina, OrdenacaoAditivo ordenacaoDefault) {
		FiltroAditivo instance = new FiltroAditivo();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
