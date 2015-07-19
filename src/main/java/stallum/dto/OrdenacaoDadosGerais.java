package stallum.dto;

import stallum.dao.DadosGeraisDao;


public enum OrdenacaoDadosGerais implements Ordenacao {

	DEFAULT				(DadosGeraisDao.ALIAS + ".criadoEm"		, OrdenacaoDadosGerais.DESCENDENTE),
	DATA_INCLUSAO		(DadosGeraisDao.ALIAS + ".criadoEm"		, OrdenacaoDadosGerais.DESCENDENTE), 
	DESCRICAO			(DadosGeraisDao.ALIAS + ".descricao"	, OrdenacaoDadosGerais.ASCENDENTE), 
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoDadosGerais(String nomeCampo, int ordem) {
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
