package stallum.dto;

import java.util.ArrayList;
import java.util.List;

import stallum.enums.PerfilUsuario;

public class FiltroUsuario {

	private String palavraChave;
	private OrdenacaoUsuario ordenacao;
	private Paginacao paginacao;
	private PerfilUsuario perfilMin;

	private List<OrdenacaoUsuario> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoUsuario getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoUsuario ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public PerfilUsuario getPerfilMin() {
		return perfilMin;
	}

	public void setPerfilMin(PerfilUsuario perfilMin) {
		this.perfilMin = perfilMin;
	}

	public List<OrdenacaoUsuario> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoUsuario> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoUsuario ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoUsuario>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroUsuario newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoUsuario.DEFAULT);
	}

	public static FiltroUsuario newInstance(int registrosPorPagina, OrdenacaoUsuario ordenacaoDefault) {
		FiltroUsuario instance = new FiltroUsuario();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

	public static FiltroUsuario newInstance(int registrosPorPagina, PerfilUsuario perfilMin) {
		FiltroUsuario instance = newInstance(registrosPorPagina);
		instance.setPerfilMin(perfilMin);
		return instance;
	}

}
