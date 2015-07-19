package stallum.controller;

import stallum.infra.SessaoUsuario;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;

@Resource
public class IndexController extends AbstractController {

//	private Logger log = Logger.getLogger(IndexController.class);
	
	public IndexController(Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);		
	}

	@Get @Path("/home")
	public void home() {
				
	}
	
}
