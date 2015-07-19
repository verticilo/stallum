package stallum.enums;

public enum TipoDocumento {

	BOLETO	("F"),
	CARNE	("F"),
	NOTA	("FC"),
	RECIBO	("F");
	
	public static final char FISCAL = 'F';
	public static final char CONTA = 'C';
	
	private String visivel;
	
	private TipoDocumento(String visibilidade) {
		this.visivel = visibilidade;
	}
	
	public boolean isVisivel(String visibilidade) {
		return visivel == visibilidade;
	}
}
