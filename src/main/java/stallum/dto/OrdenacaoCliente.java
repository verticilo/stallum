package stallum.dto;

import stallum.dao.ClienteDao;


public enum OrdenacaoCliente implements Ordenacao {

	DEFAULT				(ClienteDao.ALIAS + ".nomeCurto"	, OrdenacaoCliente.ASCENDENTE),
	NOME_CURTO			(ClienteDao.ALIAS + ".nomeCurto"	, OrdenacaoCliente.ASCENDENTE), 
	RAZAO_SOCIAL		(ClienteDao.ALIAS + ".razaoSocial"	, OrdenacaoCliente.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoCliente(String nomeCampo, int ordem) {
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
