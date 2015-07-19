package stallum.dto;

import stallum.dao.AditivoDao;


public enum OrdenacaoAditivo implements Ordenacao {

	DEFAULT		(AditivoDao.ALIAS_OBRA + ".nome"	, OrdenacaoAditivo.DESCENDENTE),
	OBRA		(AditivoDao.ALIAS_OBRA + ".nome"	, OrdenacaoAditivo.ASCENDENTE),
	NUMERO		(AditivoDao.ALIAS + ".numero"		, OrdenacaoAditivo.ASCENDENTE),
	DESCRICAO	(AditivoDao.ALIAS + ".descricao"	, OrdenacaoAditivo.ASCENDENTE),
	DATA		(AditivoDao.ALIAS + ".data"			, OrdenacaoAditivo.DESCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoAditivo(String nomeCampo, int ordem) {
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
