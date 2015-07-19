package stallum.dto;

import stallum.dao.BonificacaoDao;


public enum OrdenacaoBonificacao implements Ordenacao {

	DEFAULT	(BonificacaoDao.ALIAS + ".data"	 , OrdenacaoBonificacao.DESCENDENTE),
	DATA	(BonificacaoDao.ALIAS + ".data"	 , OrdenacaoBonificacao.DESCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoBonificacao(String nomeCampo, int ordem) {
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
