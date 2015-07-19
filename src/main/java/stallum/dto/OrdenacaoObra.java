package stallum.dto;

import stallum.dao.ObraDao;


public enum OrdenacaoObra implements Ordenacao {

	DEFAULT			(ObraDao.ALIAS + ".nome"			, OrdenacaoObra.ASCENDENTE),
	NOME			(ObraDao.ALIAS + ".nome"			, OrdenacaoObra.ASCENDENTE),
	CLIENTE			(ObraDao.ALIAS_CLIENTE + ".nome"	, OrdenacaoObra.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoObra(String nomeCampo, int ordem) {
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
