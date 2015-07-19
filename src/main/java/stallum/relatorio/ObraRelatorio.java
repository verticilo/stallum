package stallum.relatorio;

import java.util.Date;

import stallum.modelo.Obra;

public class ObraRelatorio {

	private Obra obra;
	private boolean aditivos;
	private boolean despesas;
	private boolean medicoes;
	private boolean apontamentos;
	private Date dataDe;
	private Date dataAte;
	private boolean excel;
	
	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public boolean isAditivos() {
		return aditivos;
	}

	public void setAditivos(boolean aditivos) {
		this.aditivos = aditivos;
	}

	public boolean isDespesas() {
		return despesas;
	}

	public void setDespesas(boolean despesas) {
		this.despesas = despesas;
	}

	public boolean isMedicoes() {
		return medicoes;
	}

	public void setMedicoes(boolean medicoes) {
		this.medicoes = medicoes;
	}

	public boolean isApontamentos() {
		return apontamentos;
	}

	public void setApontamentos(boolean apontamentos) {
		this.apontamentos = apontamentos;
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
	
	public boolean isAnalitico() {
		return this.aditivos || this.apontamentos || this.despesas || this.medicoes;
	}

	public boolean isExcel() {
		return excel;
	}

	public void setExcel(boolean excel) {
		this.excel = excel;
	}

}

