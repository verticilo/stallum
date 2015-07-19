package stallum.dto;

import stallum.dao.ContaGerencialDao;


public enum OrdenacaoContaGerencial implements Ordenacao {

	DEFAULT		(ContaGerencialDao.ALIAS + ".numero"	, OrdenacaoContaGerencial.ASCENDENTE),
	NUMERO		(ContaGerencialDao.ALIAS + ".numero"	, OrdenacaoContaGerencial.ASCENDENTE),
	NOME		(ContaGerencialDao.ALIAS + ".nome"		, OrdenacaoContaGerencial.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoContaGerencial(String nomeCampo, int ordem) {
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
