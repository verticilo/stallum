package stallum.dto;

import stallum.dao.FuncaoDao;


public enum OrdenacaoFuncao implements Ordenacao {

	DEFAULT			(FuncaoDao.ALIAS + ".nome"			, OrdenacaoFuncao.ASCENDENTE),
	NOME			(FuncaoDao.ALIAS + ".nome"			, OrdenacaoFuncao.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoFuncao(String nomeCampo, int ordem) {
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
