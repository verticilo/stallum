package stallum.dto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import stallum.enums.MotivoFalta;
import stallum.enums.StatusFuncionario;
import stallum.infra.Util;
import stallum.modelo.CentroCusto;
import stallum.modelo.Empresa;
import stallum.modelo.Funcionario;
import stallum.modelo.Obra;

public class FiltroApontamento {

	private String palavraChave;
	private OrdenacaoApontamento ordenacao;
	private Paginacao paginacao;
	private Obra obra;
	private CentroCusto centroCusto;
	private MotivoFalta motivoFalta;
	private boolean ocorrencias;
	private String strData;
	private Date data;
	private Date dataFim;
	private Calendar cal = Calendar.getInstance();
	private DiaApontamento dia;
	private Funcionario funcionario;
	private Empresa empresa;
	private StatusFuncionario statusFuncionario;

	private List<OrdenacaoApontamento> listaOrdenacao;

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public OrdenacaoApontamento getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoApontamento ordenacao) {
		this.ordenacao = ordenacao;
	}
	
	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public List<OrdenacaoApontamento> getListaOrdenacao() {
		return listaOrdenacao;
	}

	public void setListaOrdenacao(List<OrdenacaoApontamento> listaOrdenacao) {
		this.listaOrdenacao = listaOrdenacao;
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

	public MotivoFalta getMotivoFalta() {
		return motivoFalta;
	}

	public void setMotivoFalta(MotivoFalta motivoFalta) {
		this.motivoFalta = motivoFalta;
	}

	public boolean isOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(boolean ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public String getStrData() {
		return strData;
	}

	public void setStrData(String strData) {
		this.strData = strData;
	}	
	
	public void setData(String strData) {
		setData(Util.stringToData(strData));
		cal.setTime(getData());
	}
	
	public String getMesAno() {
		return Util.getMessage("calendario.mes" + (cal.get(Calendar.MONTH) + 1)) + "/" + cal.get(Calendar.YEAR);
	}
	
	public String getProximoMes() {
		return (cal.get(Calendar.MONTH) + 2) + "-" + cal.get(Calendar.YEAR);
	}
	
	public String getMesAnterior() {
		return cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.YEAR);
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Calendar getCal() {
		return cal;
	}

	public DiaApontamento getDia() {
		return dia;
	}

	public void setDia(DiaApontamento dia) {
		this.dia = dia;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public StatusFuncionario getStatusFuncionario() {
		return statusFuncionario;
	}

	public void setStatusFuncionario(StatusFuncionario statusFuncionario) {
		this.statusFuncionario = statusFuncionario;
	}

	public void addOrdencacao(OrdenacaoApontamento ordem) {
		if (listaOrdenacao == null)
			listaOrdenacao = new ArrayList<OrdenacaoApontamento>();
		listaOrdenacao.add(ordem);
	}

	public static FiltroApontamento newInstance(int registrosPorPagina) {
		return newInstance(registrosPorPagina, OrdenacaoApontamento.DEFAULT);
	}
	
	public static FiltroApontamento newInstance(int registrosPorPagina, OrdenacaoApontamento ordenacaoDefault) {
		FiltroApontamento instance = new FiltroApontamento();
		instance.setOrdenacao(ordenacaoDefault);
		instance.setPaginacao(Paginacao.newInstance(registrosPorPagina));
		return instance;
	}

}
