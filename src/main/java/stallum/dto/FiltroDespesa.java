package stallum.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stallum.modelo.CentroCusto;
import stallum.modelo.Obra;

public class FiltroDespesa {

	private String palavraChave;
	private OrdenacaoDespesa ordenacao;
	private Paginacao paginacao;
	private Obra obra;
	private CentroCusto centroCusto;
	
	private Date dataDe;
	private Date dataAte;

	private List<OrdenacaoDespesa> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoDespesa getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoDespesa ordenacao) {
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

	public CentroCusto getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
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

	public List<OrdenacaoDespesa> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoDespesa> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoDespesa ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoDespesa>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroDespesa newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoDespesa.DEFAULT);
	}
	
	public static FiltroDespesa newInstance(int registrosPorPagina, OrdenacaoDespesa ordenacaoDefault) {
		FiltroDespesa instance = new FiltroDespesa();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
