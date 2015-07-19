package stallum.dto;

import java.util.ArrayList;
import java.util.List;

import stallum.modelo.Funcionario;

public class FiltroMovimento {

	private String palavraChave;
	private OrdenacaoMovimento ordenacao;
	private Paginacao paginacao;
	private Funcionario funcionario;

	private List<OrdenacaoMovimento> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoMovimento getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoMovimento ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<OrdenacaoMovimento> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoMovimento> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoMovimento ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoMovimento>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroMovimento newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoMovimento.DEFAULT);
	}
	
	public static FiltroMovimento newInstance(int registrosPorPagina, OrdenacaoMovimento ordenacaoDefault) {
		FiltroMovimento instance = new FiltroMovimento();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
