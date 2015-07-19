package stallum.dto;

import stallum.dao.UsuarioDao;

public enum OrdenacaoUsuario implements Ordenacao {

	DEFAULT			(UsuarioDao.ALIAS + ".nome"		, OrdenacaoUsuario.ASCENDENTE),
	NOME			(UsuarioDao.ALIAS + ".nome"		, OrdenacaoUsuario.ASCENDENTE),
	EMAIL			(UsuarioDao.ALIAS + ".email"	, OrdenacaoUsuario.ASCENDENTE)
	; 

	private String nomeCampo;
	private int ordem;

	OrdenacaoUsuario(String nomeCampo, int ordem) {
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
