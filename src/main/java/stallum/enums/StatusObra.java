package stallum.enums;

public enum StatusObra {

	ATIVA			('T'),
	INTERROMPIDA	('T'),
	CONCLUIDA		('T'),
	REMOVIDA		('T');
	
	public static final char ADMIN = 'A';
	public static final char TODOS = 'T';
	
	private char visivel;
	
	private StatusObra(char visibilidade) {
		this.visivel = visibilidade;
	}
	
	public boolean isVisivel(char visibilidade) {
		return visivel == visibilidade;
	}
}
