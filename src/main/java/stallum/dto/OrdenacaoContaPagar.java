package stallum.dto;

import stallum.dao.ContaPagarDao;


public enum OrdenacaoContaPagar implements Ordenacao {

	DEFAULT		(ContaPagarDao.ALIAS + ".status", OrdenacaoContaPagar.ASCENDENTE),
	DATA		(ContaPagarDao.ALIAS + ".data"	, OrdenacaoContaPagar.DESCENDENTE),
	CONTA		(ContaPagarDao.ALIAS + ".nome"	, OrdenacaoContaPagar.ASCENDENTE),
	FORNECEDOR	(ContaPagarDao.ALIAS_FORNECEDOR + ".nome", OrdenacaoContaPagar.ASCENDENTE),
	VENCTO		(ContaPagarDao.ALIAS + ".vencto", OrdenacaoContaPagar.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoContaPagar(String nomeCampo, int ordem) {
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
