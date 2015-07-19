package stallum.dto;

import stallum.dao.DespesaDao;


public enum OrdenacaoDespesa implements Ordenacao {

	DEFAULT		(DespesaDao.ALIAS + ".descricao"	, OrdenacaoDespesa.ASCENDENTE),
	OBRA		(DespesaDao.ALIAS_OBRA + ".nome"	, OrdenacaoAditivo.ASCENDENTE),
	DESCRICAO	(DespesaDao.ALIAS + ".descricao"	, OrdenacaoDespesa.ASCENDENTE),
	DATA		(DespesaDao.ALIAS + ".data"			, OrdenacaoDespesa.DESCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoDespesa(String nomeCampo, int ordem) {
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
