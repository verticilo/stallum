package stallum.dto;

import stallum.dao.ContaCorrenteDao;


public enum OrdenacaoContaCorrente implements Ordenacao {

	DEFAULT		(ContaCorrenteDao.ALIAS + ".nomeBanco"	, OrdenacaoContaCorrente.ASCENDENTE),
	NOME_BANCO	(ContaCorrenteDao.ALIAS + ".nomeBanco"	, OrdenacaoContaCorrente.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoContaCorrente(String nomeCampo, int ordem) {
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
