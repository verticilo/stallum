package stallum.enums;

public enum ClasseContaGerencial {

	RECEITA	("1"),
	DESPESA	("2"),
	CUSTO	("3")
	;
	
	private String prefixo;
	
	private ClasseContaGerencial(String prefixo) {
		this.prefixo = prefixo;
	}
	
	public static ClasseContaGerencial get(String prefixo) {
		for (ClasseContaGerencial ccg: ClasseContaGerencial.values()) {
			if (ccg.prefixo.equals(prefixo))
				return ccg;
		}
		return null;
	}
}
