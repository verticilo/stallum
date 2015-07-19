package stallum.dto;

import java.util.Calendar;
import java.util.Date;

import stallum.enums.DiaSemana;
import stallum.infra.Util;


public class DiaApontamento {

	private Date data;
	private int numFuncionarios;
	private int numPontos;
	private Calendar cal = Calendar.getInstance();

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
		cal.setTime(data);
	}

	public int getNumFuncionarios() {
		return numFuncionarios;
	}

	public void setNumFuncionarios(int numFuncionarios) {
		this.numFuncionarios = numFuncionarios;
	}

	public int getNumPontos() {
		return numPontos;
	}

	public void setNumPontos(int numPontos) {
		this.numPontos = numPontos;
	}

	public int getDiaMes() {		
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public DiaSemana getDiaSemana() {
		return DiaSemana.values()[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public char getDia() {
		return getDiaSemana().toString().charAt(0);
	}
	
	public String getCor1() {
		if (getDiaSemana() == null)
			return "#ffffff";
		if (DiaSemana.SAB.equals(getDiaSemana()))
			return "#ca8f8f";
		else if (DiaSemana.DOM.equals(getDiaSemana()))
			return "#ca8f8f";
		return "#105d8a";
	}
	
	public String getCor2() {
		if (data == null)
			return "#fafafa";
		if ((DiaSemana.SAB.equals(getDiaSemana()) || DiaSemana.DOM.equals(getDiaSemana())) && numPontos <= 0)
			return "#ccc"; 
		if (data.compareTo(new Date()) > 0 || numPontos < 0)
			return "#eee"; 
		if (numFuncionarios < 0)
			return "#ca8f8f"; // Vermelho		
		if (numFuncionarios <= numPontos)
			return "#a5c598"; // Verde
		return "#e8e790"; // Amarelo
	}
	
	public String toString() {
		if (data == null)
			return "";
		return getDiaMes() + "";
	}
	
	public String getStrData() {
		if (data == null)
			return null;
		return Util.dataToString(data).replaceAll("/", "-");
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		DiaApontamento other = (DiaApontamento) obj;
		if (getData() == null || other.getData() == null)
			return false;
		return this == other || getData().compareTo(other.getData()) == 0;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((getData() == null) ? 0 : getData().hashCode());
		return result;
	}
}
