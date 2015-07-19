package stallum.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stallum.modelo.Sindicato;

public class FiltroDissidio {

	private String palavraChave;
	private OrdenacaoDissidio ordenacao;
	private Paginacao paginacao;
	private Sindicato sindicato;
	private Date dataDissidio;

	private List<OrdenacaoDissidio> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoDissidio getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoDissidio ordenacao) {
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

	public Date getDataDissidio() {
		return dataDissidio;
	}

	public void setDataDissidio(Date dataDissidio) {
		this.dataDissidio = dataDissidio;
	}

	public List<OrdenacaoDissidio> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoDissidio> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoDissidio ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoDissidio>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroDissidio newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoDissidio.DEFAULT);
	}
	
	public static FiltroDissidio newInstance(int registrosPorPagina, OrdenacaoDissidio ordenacaoDefault) {
		FiltroDissidio instance = new FiltroDissidio();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
