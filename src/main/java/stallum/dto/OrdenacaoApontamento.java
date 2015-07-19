package stallum.dto;

import stallum.dao.ApontamentoDao;


public enum OrdenacaoApontamento implements Ordenacao {

	DEFAULT		 (ApontamentoDao.ALIAS + ".data"		 , OrdenacaoApontamento.DESCENDENTE),
	DATA		 (ApontamentoDao.ALIAS + ".data"		 , OrdenacaoApontamento.ASCENDENTE),
	OBRA		 (ApontamentoDao.ALIAS_OBRA + ".nome" , OrdenacaoApontamento.ASCENDENTE),
	CENTRO_CUSTO (ApontamentoDao.ALIAS_CCUSTO + ".nome" , OrdenacaoApontamento.ASCENDENTE),
	FUNCIONARIO	 (ApontamentoDao.ALIAS_FUNCIONARIO + ".nome" , OrdenacaoApontamento.ASCENDENTE),
	STATUS_FUNC	 (ApontamentoDao.ALIAS_FUNCIONARIO + ".status" , OrdenacaoApontamento.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoApontamento(String nomeCampo, int ordem) {
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
