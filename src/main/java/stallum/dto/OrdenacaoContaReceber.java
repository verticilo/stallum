package stallum.dto;

import stallum.dao.ContaReceberDao;


public enum OrdenacaoContaReceber implements Ordenacao {

	DEFAULT	(ContaReceberDao.ALIAS + ".status", OrdenacaoContaReceber.ASCENDENTE),
	DATA	(ContaReceberDao.ALIAS + ".data"	, OrdenacaoContaReceber.DESCENDENTE),
	CONTA	(ContaReceberDao.ALIAS + ".nome"	, OrdenacaoContaReceber.ASCENDENTE),
	CLIENTE	(ContaReceberDao.ALIAS_CLIENTE + ".nome", OrdenacaoContaReceber.ASCENDENTE),
	VENCTO	(ContaReceberDao.ALIAS + ".vencto", OrdenacaoContaReceber.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoContaReceber(String nomeCampo, int ordem) {
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
