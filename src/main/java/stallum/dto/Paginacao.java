package stallum.dto;

public class Paginacao {

	public static final int PRIMEIRA_PAGINA = 1;
	public static final int PAGINA_ANTERIOR = 2;
	public static final int PROXIMA_PAGINA 	= 3;
	public static final int ULTIMA_PAGINA 	= 4;
	public static final int LIMPAR 			= 5;
	
	private int paginaAtual = 1;
	private int registrosPorPagina = 10;
	private int totalRegistros;
	private int totalPaginas = 1;
	private int acao;

	public Paginacao() {
		super();
	}
	
	public Paginacao(int registrosPorPagina) {
		this.registrosPorPagina = registrosPorPagina;
	}

	/**
	 * Retorna o numero da pagina atual, entre 1 e totalPaginas
	 * @return
	 */
	public int getPaginaAtual() {
		if (paginaAtual < 1) {
			return 1;
		}
		if (paginaAtual > getTotalPaginas()) {
			return getTotalPaginas();
		}
		return paginaAtual;
	}

	public void setPaginaAtual(int paginaAtual) {
		this.paginaAtual = paginaAtual; 
	}

	public int getRegistrosPorPagina() {
		return registrosPorPagina;
	}

	public void setRegistrosPorPagina(int registrosPorPagina) {
		this.registrosPorPagina = registrosPorPagina;
		
	}

	public int getTotalRegistros() {
		return totalRegistros;
	}
	
	/**
	 * Define o n�mero total de registros e o n�mero total de p�ginas de acordo com o n�mero de registros por p�gina definido.<br/>
	 * <br/>
	 * totalPagnas = totalRegistros / registrosPorPagina
	 * @param totalRegistros
	 */
	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
		
		double tr = this.totalRegistros;
		double rp = getRegistrosPorPagina();
		setTotalPaginas(Double.valueOf(Math.ceil(tr / rp)).intValue());
	}
	
	public int getTotalPaginas() {
		if (totalPaginas < 1) {
			return 1;
		}
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}

	public int getAcao() {
		return acao;
	}
	
	public void setAcao(int acao) {
		this.acao = acao;
	}

	public static Paginacao newInstance(int registrosPorPagina) {
		if (registrosPorPagina >= 0)
			return new Paginacao(registrosPorPagina);
		return null;
	}

	/**
	 * Calcula o n�mero do primeiro registro da p�gina atual para ser usado no offset da query do banco de dados.
	 * @return int
	 */
	public int getPrimeiroRegistro() {
		return (getPaginaAtual() - 1) * getRegistrosPorPagina();
	}

	/**
	 * Atualiza a pagina��o conforme acao definida:<br/>
	 * <br/>
	 * PRIMEIRA_PAGINA = 1 : Retorna para a p�gina 1;<br/>
	 * PAGINA_ANTERIOR = 2 : Volta uma p�gina se j� n�o estiver na p�gina 1;<br/>
	 * PROXIMA_PAGINA  = 3 : Avan�a uma p�gina se j� n�o estiver na �ltima p�gina;<br/>
	 * ULTIMA_PAGINA   = 4 : Avan�a para a �ltima p�gina.
	 */
	public void atualizar() {
		switch (getAcao()) {
		case PRIMEIRA_PAGINA:
			setPaginaAtual(1);
			break;
		case PAGINA_ANTERIOR:
			if (getPaginaAtual() > 1)
				setPaginaAtual(getPaginaAtual() - 1);
			break;
		case PROXIMA_PAGINA:
			if (getPaginaAtual() < getTotalPaginas())
				setPaginaAtual(getPaginaAtual() + 1);
			break;
		case ULTIMA_PAGINA:
			setPaginaAtual(getTotalPaginas());
		}
	}

	
}
