package stallum.dto;

public interface Ordenacao {

	public static final int ASCENDENTE  = 1;
	public static final int DESCENDENTE = 2;

	public String getNomeCampo();

	public int getOrdem();
	
}
