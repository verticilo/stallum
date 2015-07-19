
package stallum.infra;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import stallum.dao.UsuarioDao;
import stallum.enums.ItemMenu;
import stallum.modelo.Usuario;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class SessaoInterceptor implements Interceptor {

	public static final String COOKIE_USUARIO 	= "idu";
	public static final String COOKIE_CARRINHO	= "idc";
	
	private Logger log = Logger.getLogger(SessaoInterceptor.class);
	
	private final SessaoUsuario sessao;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final UsuarioDao usuarioDao;
	
	public SessaoInterceptor(SessaoUsuario sessao, HttpServletRequest request, HttpServletResponse response, 
			UsuarioDao usuarioDao) {
		this.sessao = sessao;
		this.request = request;
		this.response = response;
		this.usuarioDao = usuarioDao;
	}
	
	@Override
	public boolean accepts(ResourceMethod method) {
		
		Locale.setDefault(request.getLocale());
		
		if (sessao.getMenu() == null)
			sessao.setMenu(ItemMenu.getMenu());
		
		Cookie[] cookies = request.getCookies();

		if (cookies == null)
			return false;
		
		for (Cookie cookie: cookies) {
		
			try {

				if (!sessao.isLogado() && cookie.getName().equals(COOKIE_USUARIO)) {
				
					String ck[] = cookie.getValue().split(",");
					Long id = Long.valueOf(ck[1]);
					String chave = ck[0];
					
					Usuario usuario = usuarioDao.autenticarUsuario(id, chave);
					if (usuario == null)
						throw new Exception("usuario > " + COOKIE_USUARIO);

					sessao.login(usuario);
					
					// Verifica se existe lancamento de vale refeicao para o mes corrente
					if (!usuarioDao.checarBonificacao())
						sessao.addErro(Util.getMessage("bonificacao.naoLancada"));
					
					// Verifica se os dissidio ja foram lancados
					if (!usuarioDao.checarDissidio())
						sessao.addErro(Util.getMessage("dissidio.naoLancado"));
					
				}
				
			} catch (Exception e) {
				log.warn("Falha ao acessar Cookie: " + e.getMessage());
				cookie.setMaxAge(0); // Apaga o cookie
				response.addCookie(cookie);
			}
				
		}
		 
		return false;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object object) throws InterceptionException {
		
	}
	
}
