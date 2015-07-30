
package stallum.infra;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import stallum.enums.ItemMenu;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class SessaoInterceptor implements Interceptor {

	private final SessaoUsuario sessao;
	private final HttpServletRequest request;
	
	public SessaoInterceptor(SessaoUsuario sessao, HttpServletRequest request) {
		this.sessao = sessao;
		this.request = request;
	}
	
	@Override
	public boolean accepts(ResourceMethod method) {
		
		Locale.setDefault(request.getLocale());
		
		if (sessao.getMenu() == null)
			sessao.setMenu(ItemMenu.getMenu());
		
		return false;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object object) throws InterceptionException {
		
	}
	
}
