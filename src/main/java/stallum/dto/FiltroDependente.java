package stallum.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import stallum.modelo.Funcionario;

public class FiltroDependente {

	private String palavraChave;
	private OrdenacaoDependente ordenacao;
	private Paginacao paginacao;
	private Funcionario funcionario;

	private Date dataDe;
	private Date dataAte;
	
	private List<OrdenacaoDependente> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoDependente getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoDependente ordenacao) {
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

	public List<OrdenacaoDependente> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoDependente> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
	}

	public void addOrdencacao(OrdenacaoDependente ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoDependente>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroDependente newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoDependente.DEFAULT);
	}
	
	public static FiltroDependente newInstance(int registrosPorPagina, OrdenacaoDependente ordenacaoDefault) {
		FiltroDependente instance = new FiltroDependente();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
