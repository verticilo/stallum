package stallum.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stallum.modelo.Obra;

public class FiltroMedicao {

	private String palavraChave;
	private OrdenacaoMedicao ordenacao;
	private Paginacao paginacao;
	private Obra obra;
	private Boolean canceladas;

	private Date dataDe;
	private Date dataAte;
	
	private List<OrdenacaoMedicao> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoMedicao getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoMedicao ordenacao) {
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

	public Boolean getCanceladas() {
		return canceladas;
	}

	public void setCanceladas(Boolean canceladas) {
		this.canceladas = canceladas;
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

	public List<OrdenacaoMedicao> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoMedicao> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoMedicao ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoMedicao>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroMedicao newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoMedicao.DEFAULT);
	}
	
	public static FiltroMedicao newInstance(int registrosPorPagina, OrdenacaoMedicao ordenacaoDefault) {
		FiltroMedicao instance = new FiltroMedicao();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
