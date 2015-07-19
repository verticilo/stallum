package stallum.enums;

public enum MotivoFalta {

	FALTA	('T'),
	FERIAS	('T'),
	ABONO	('T');
	
	public static final char ADMIN = 'A';
	public static final char TODOS = 'T';
	
	private char visivel;
	
	private MotivoFalta(char visibilidade) {
		this.visivel = visibilidade;
	}
	
	public boolean isVisivel(char visibilidade) {
		return visivel == visibilidade;
	}
}
