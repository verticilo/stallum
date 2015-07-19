package stallum.infra;

import br.com.caelum.vraptor.interceptor.multipart.DefaultMultipartConfig;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class CustomMultipartConfig extends DefaultMultipartConfig {

	public static final long SIZE_LIMIT = 6 * 1024 * 1024; // 6MB
	
	@Override
	public long getSizeLimit() {
		return SIZE_LIMIT;
	}
	
}
