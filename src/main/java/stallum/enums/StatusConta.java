package stallum.enums;

public enum StatusConta {

	ABERTA		('T'),
	LIQUIDADA	('T'),
	CANCELADA	('T'),
	VENCIDA		('T'),
	PARCELADA	('T'),
	PRORROGADA	('T');
	
	public static final char ADMIN = 'A';
	public static final char TODOS = 'T';
	
	private char visivel;
	
	private StatusConta(char visibilidade) {
		this.visivel = visibilidade;
	}
	
	public boolean isVisivel(char visibilidade) {
		return visivel == visibilidade;
	}
}
