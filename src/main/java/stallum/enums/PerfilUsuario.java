package stallum.enums;

public enum PerfilUsuario {

	USUARIO		("Usário"),
	CLIENTE		("Cliente"),
	OPERADOR	("Operador"),
	GERENTE		("Gerente"),
	ADMIN		("Administrador"),
	;
	
	private String nome;
	
	PerfilUsuario(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
