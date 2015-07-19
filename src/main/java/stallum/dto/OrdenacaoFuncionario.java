package stallum.dto;

import stallum.dao.FuncionarioDao;


public enum OrdenacaoFuncionario implements Ordenacao {

	DEFAULT		(FuncionarioDao.ALIAS + ".nome"			, OrdenacaoFuncionario.ASCENDENTE),
	NOME		(FuncionarioDao.ALIAS + ".nome"			, OrdenacaoFuncionario.ASCENDENTE),
	FUNCAO		(FuncionarioDao.ALIAS_FUNCAO + ".nome"	, OrdenacaoFuncionario.DESCENDENTE),
	STATUS		(FuncionarioDao.ALIAS + ".status"		, OrdenacaoFuncionario.DESCENDENTE)
	; 

	private String nomeCampo;
	private int ordem;

	OrdenacaoFuncionario(String nomeCampo, int ordem) {
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
