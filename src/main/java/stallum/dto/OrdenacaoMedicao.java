package stallum.dto;

import stallum.dao.MedicaoDao;


public enum OrdenacaoMedicao implements Ordenacao {

	DEFAULT		(MedicaoDao.ALIAS_OBRA + ".nome"	, OrdenacaoMedicao.ASCENDENTE),
	OBRA		(MedicaoDao.ALIAS_OBRA + ".nome"	, OrdenacaoAditivo.ASCENDENTE),
	NOTA_FISCAL	(MedicaoDao.ALIAS + ".notaFiscal"	, OrdenacaoMedicao.ASCENDENTE),
	DATA		(MedicaoDao.ALIAS + ".data"			, OrdenacaoMedicao.DESCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoMedicao(String nomeCampo, int ordem) {
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
