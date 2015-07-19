package stallum.dto;

import stallum.dao.DependenteDao;


public enum OrdenacaoDependente implements Ordenacao {

	DEFAULT				(DependenteDao.ALIAS_FUNCIONARIO + ".nome"	, OrdenacaoDependente.ASCENDENTE),
	NOME_FUNCIONARIO	(DependenteDao.ALIAS_FUNCIONARIO + ".nome"	, OrdenacaoDependente.ASCENDENTE),
	NOME				(DependenteDao.ALIAS + ".nome"				, OrdenacaoDependente.ASCENDENTE),
	DATA_NASCIMENTO		(DependenteDao.ALIAS + ".dataNascimento"	, OrdenacaoDependente.ASCENDENTE), 
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoDependente(String nomeCampo, int ordem) {
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
