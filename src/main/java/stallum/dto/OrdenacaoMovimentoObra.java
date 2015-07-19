package stallum.dto;

import stallum.dao.MovimentoObraDao;



public enum OrdenacaoMovimentoObra implements Ordenacao {

	DEFAULT		(MovimentoObraDao.ALIAS + ".data"	, OrdenacaoMovimentoObra.DESCENDENTE),
	DATA		(MovimentoObraDao.ALIAS + ".data"	, OrdenacaoMovimentoObra.DESCENDENTE), 
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoMovimentoObra(String nomeCampo, int ordem) {
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
