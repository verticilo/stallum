package stallum.relatorio;

import java.util.Date;

import stallum.dto.OrdenacaoApontamento;
import stallum.dto.Paginacao;
import stallum.enums.MotivoFalta;
import stallum.enums.StatusFuncionario;
import stallum.infra.Util;
import stallum.modelo.CentroCusto;
import stallum.modelo.Empresa;
import stallum.modelo.Funcionario;
import stallum.modelo.Obra;
import stallum.modelo.Sindicato;

public class FuncionarioRelatorio {

	public static final String NENHUM = "nenhum";
	public static final String FALTAS = "faltas";
	public static final String ABONOS = "abonos";
	public static final String FERIAS = "ferias";
	
	private Funcionario funcionario;
	private Obra obra;
	private Empresa empresa;
	private Sindicato sindicato;
	private CentroCusto centroCusto;
	private MotivoFalta motivoFalta;
	private StatusFuncionario status;
	private String filtroLista;
	private Date dataDe;
	private Date dataAte;
	private Boolean analitico;
	private OrdenacaoApontamento ordenacao;
	private boolean excel;
	private Paginacao paginacao;	
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Sindicato getSindicato() {
		return sindicato;
	}

	public void setSindicato(Sindicato sindicato) {
		this.sindicato = sindicato;
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

	public StatusFuncionario getStatus() {
		return status;
	}

	public void setStatus(StatusFuncionario status) {
		this.status = status;
	}

	public String getFiltroLista() {
		return filtroLista;
	}

	public void setFiltroLista(String filtroLista) {
		this.filtroLista = filtroLista;
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

	public Boolean getAnalitico() {
		return analitico;
	}

	public void setAnalitico(Boolean analitico) {
		this.analitico = analitico;
	}

	public OrdenacaoApontamento getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(OrdenacaoApontamento ordenacao) {
		this.ordenacao = ordenacao;
	}

	public boolean isExcel() {
		return excel;
	}

	public void setExcel(boolean excel) {
		this.excel = excel;
	}

	public Paginacao getPaginacao() {
		return paginacao;
	}

	public void setPaginacao(Paginacao paginacao) {
		this.paginacao = paginacao;
	}

	public String getStrDataDe() {
		return Util.dataToString(dataDe).replaceAll("/", "-");
	}
}

