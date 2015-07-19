package stallum.dto;

import stallum.dao.MovimentoDao;



public enum OrdenacaoMovimento implements Ordenacao {

	DEFAULT		(MovimentoDao.ALIAS + ".data"	, OrdenacaoMovimento.DESCENDENTE),
	DATA		(MovimentoDao.ALIAS + ".data"	, OrdenacaoMovimento.DESCENDENTE), 
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoMovimento(String nomeCampo, int ordem) {
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
