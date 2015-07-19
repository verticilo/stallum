package stallum.dto;

import stallum.dao.CentroCustoDao;


public enum OrdenacaoCentroCusto implements Ordenacao {

	DEFAULT			(CentroCustoDao.ALIAS + ".nome"			, OrdenacaoCentroCusto.ASCENDENTE),
	NOME			(CentroCustoDao.ALIAS + ".nome"			, OrdenacaoCentroCusto.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoCentroCusto(String nomeCampo, int ordem) {
		this.nomeCampo = nomeCampo;
		this.ordem = ordem;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public int getOrdem() {
		return ordem;
	}

}
