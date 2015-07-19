package stallum.dto;

import stallum.dao.EmpresaDao;


public enum OrdenacaoEmpresa implements Ordenacao {

	DEFAULT				(EmpresaDao.ALIAS + ".nomeCurto"	, OrdenacaoEmpresa.ASCENDENTE),
	NOME_CURTO			(EmpresaDao.ALIAS + ".nomeCurto"	, OrdenacaoEmpresa.ASCENDENTE), 
	RAZAO_SOCIAL		(EmpresaDao.ALIAS + ".razaoSocial"	, OrdenacaoEmpresa.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoEmpresa(String nomeCampo, int ordem) {
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
