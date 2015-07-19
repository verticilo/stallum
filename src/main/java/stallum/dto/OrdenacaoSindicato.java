package stallum.dto;

import stallum.dao.SindicatoDao;


public enum OrdenacaoSindicato implements Ordenacao {

	DEFAULT			(SindicatoDao.ALIAS + ".nome"			, OrdenacaoSindicato.ASCENDENTE),
	NOME			(SindicatoDao.ALIAS + ".nome"			, OrdenacaoSindicato.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoSindicato(String nomeCampo, int ordem) {
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
