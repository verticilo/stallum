package stallum.infra;

import br.com.caelum.vraptor.validator.Message;

public class SimpleMessage implements Message {
	private static final long serialVersionUID = -1453107424603531224L;

	private String message;
	
	public SimpleMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String getCategory() {
		return null;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}
