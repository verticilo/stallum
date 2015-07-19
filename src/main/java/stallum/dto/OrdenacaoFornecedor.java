package stallum.dto;

import stallum.dao.FornecedorDao;


public enum OrdenacaoFornecedor implements Ordenacao {

	DEFAULT				(FornecedorDao.ALIAS + ".nomeCurto"	, OrdenacaoFornecedor.ASCENDENTE),
	NOME_CURTO			(FornecedorDao.ALIAS + ".nomeCurto"	, OrdenacaoFornecedor.ASCENDENTE), 
	RAZAO_SOCIAL		(FornecedorDao.ALIAS + ".razaoSocial"	, OrdenacaoFornecedor.ASCENDENTE),
	;

	private String nomeCampo;
	private int ordem;

	OrdenacaoFornecedor(String nomeCampo, int ordem) {
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
