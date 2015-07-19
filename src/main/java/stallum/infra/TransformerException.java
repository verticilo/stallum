package stallum.infra;

public class TransformerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TransformerException() {
		super();
	}

	public TransformerException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransformerException(String message) {
		super(message);
	}

	public TransformerException(Throwable cause) {
		super(cause);
	}

	
}
