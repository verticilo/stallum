package stallum.enums;

import java.util.ArrayList;
import java.util.Collection;


public enum ItemMenu {

	DADOS_GERAIS	("/dadosGerais/listar"),
	EMPRESAS		("/empresas/listar"),
	FUNCOES			("/funcoes/listar"),
	SINDICATOS		("/sindicatos/listar"),
	FUNCIONARIOS	("/funcionarios/listar"),
	CLIENTES		("/clientes/listar"),
	OBRAS			("/obras/listar"),
	CENTROS_CUSTO	("/centrosCusto/listar"),
//	FORNECEDORES	("/fornecedores/listar"),
//	CONTAS_CORRENTES("/contasCorrentes/listar"),
//	PLANO_CONTAS	("/planoContas/listar"),
	
	DIV1			("/#"),
	
	ADITIVOS		("/aditivos/listar"),
	DESPESAS		("/despesas/listar"),
	MEDICOES		("/medicoes/listar"),
	DISSIDIOS		("/dissidios/listar"),
	APONTAMENTOS	("/apontamentos/listar"),
//	APONT_NOVO		("/apontamentos/listar-novo"),
	BONIFICACOES	("/bonificacoes/listar"),
	
//	DIV2			("/#"),
//	
//	CONTAS_PAGAR	("/contasPagar/listar"),
//	CONTAS_RECEBER	("/contasReceber/listar"),
	
	DIV3			("/#"),
	
	REL_PONTO		("/relatorios/ponto"),
	REL_OBRA		("/relatorios/obra"),
	REL_FUNCIONARIO	("/relatorios/funcionarios"),
	REL_OCORRENCIA	("/relatorios/ocorrencias")
	;
	
	private String uri;
	
	ItemMenu(String uri) {
		this.uri = uri;
	}
	
	public static Collection<ItemMenu> getMenu() {
		ArrayList<ItemMenu> grupos = new ArrayList<ItemMenu>();
		for (int i = 0; i < values().length; i++)
			grupos.add(values()[i]);
		return grupos;
	}
	
	public String getUri() {
		return uri;
	}
		
}