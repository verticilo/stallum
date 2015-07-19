package stallum.dto;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ListaPonto {

	private String semana;
	private Long idObra;
	@XStreamImplicit(itemFieldName = "itens")
	private List<PontoNovo> itens;

	public String getSemana() {
		return semana;
	}

	public void setSemana(String semana) {
		this.semana = semana;
	}

	public Long getIdObra() {
		return idObra;
	}

	public void setIdObra(Long idObra) {
		this.idObra = idObra;
	}

	public List<PontoNovo> getItens() {
		return itens;
	}

	public void setItens(List<PontoNovo> itens) {
		this.itens = itens;
	}

}