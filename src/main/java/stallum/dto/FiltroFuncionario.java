package stallum.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import stallum.modelo.Funcionario;

public class FiltroFuncionario {

	private String palavraChave;
	private OrdenacaoFuncionario ordenacao;
	private Paginacao paginacao;
	private Boolean removidos = false;
	private Boolean centroCusto;
	
	private Collection<Funcionario> funcionarios;

	private List<OrdenacaoFuncionario> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoFuncionario getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoFuncionario ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}
	
	public Boolean getRemovidos() {
		return removidos;
	}

	public void setRemovidos(Boolean removidos) {
		this.removidos = removidos;
	}

	public Boolean getCentroCusto() {
		return centroCusto;
	}

	public void setCentroCusto(Boolean centroCusto) {
		this.centroCusto = centroCusto;
	}

	public List<OrdenacaoFuncionario> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoFuncionario> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public Collection<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Collection<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public void addOrdencacao(OrdenacaoFuncionario ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoFuncionario>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroFuncionario newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoFuncionario.DEFAULT);
	}
	
	public static FiltroFuncionario newInstance(int registrosPorPagina, OrdenacaoFuncionario ordenacaoDefault) {
		FiltroFuncionario instance = new FiltroFuncionario();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
