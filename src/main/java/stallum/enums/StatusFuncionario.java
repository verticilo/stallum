package stallum.enums;

import java.util.ArrayList;
import java.util.List;

public enum StatusFuncionario {

	ATIVO		('T'),
	FERIAS		('T'),
	LICENCA		('T'),
	DEMITIDO	('T'),
	REMOVIDO	('A');
	
	public static final char ADMIN = 'A';
	public static final char TODOS = 'T';
	
	private char visivel;
	
	private StatusFuncionario(char visibilidade) {
		this.visivel = visibilidade;
	}
	
	public boolean isVisivel(char visibilidade) {
		return visivel == visibilidade;
	}
	
	public static StatusFuncionario[] values(PerfilUsuario perfil) {
		List<StatusFuncionario> lista = new ArrayList<StatusFuncionario>();
		for (StatusFuncionario status: StatusFuncionario.values()) {
			if (status.isVisivel(TODOS) || PerfilUsuario.ADMIN.equals(perfil) && status.isVisivel(ADMIN))
				lista.add(status);
		}
		return lista.toArray(new StatusFuncionario[]{});
	}
}
