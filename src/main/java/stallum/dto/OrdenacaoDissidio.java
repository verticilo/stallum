package stallum.dto;

import stallum.dao.DissidioDao;


public enum OrdenacaoDissidio implements Ordenacao {

	DEFAULT		(DissidioDao.ALIAS + ".data"	, OrdenacaoDissidio.DESCENDENTE),
	DATA		(DissidioDao.ALIAS + ".data"	, OrdenacaoDissidio.DESCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoDissidio(String nomeCampo, int ordem) {
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
