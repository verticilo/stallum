package stallum.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import stallum.dao.UsuarioDao;
import stallum.dto.FiltroUsuario;
import stallum.dto.OrdenacaoUsuario;
import stallum.dto.Paginacao;
import stallum.enums.Estado;
import stallum.enums.PerfilUsuario;
import stallum.infra.SessaoInterceptor;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import stallum.modelo.Usuario;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;

@Resource
public class UsuariosController extends AbstractController {

	private Logger log = Logger.getLogger(UsuariosController.class);
	
	private final UsuarioDao dao;
	private final File dirPerfil;
	private final String PREFIXO_TOKEN = "STL-20130207";
	private final String IMG_PERFIL = "IMG_PERF_";
	private final String TBN_PERFIL = "TBN_PERF_";
	private final HttpServletResponse response;

	public UsuariosController(UsuarioDao dao, Validator validator, Localization i18n, 
			HttpServletResponse response, Result result, SessaoUsuario sessao) {
		super(validator, i18n, result, sessao);
		this.dao = dao;
		this.response = response;
		
		dirPerfil = new File(getConfig("config.dir.perfil"));
		if (!dirPerfil.isDirectory())
			dirPerfil.mkdir();
	}

	/**
	 * Form de usuario para inclusao pelo pr�prio ('cadastre-se')
	 * @param usuario
	 * @return
	 */
//	@Get @Path("/usuarios/novo")
	public Usuario formNovoUsuario(Usuario usuario) {
		return usuario;
	}	
	@Post @Path("/usuarios")
	public void adiciona(final Usuario usuario) {
		validator.checking(new Validations() {{
			that(preenchido(usuario.getNome(), 4), "usuario.nome", "obrigatorio", 4);
			that(preenchido(usuario.getEmail()), "usuario.email", "obrigatorio");
			if (preenchido(usuario.getEmail()))
				that(emailValido(usuario.getEmail()), "usuario.email", "invalido");
			that(preenchido(usuario.getSenha(), 6), "usuario.senha", "tamanho.minimo", 6);
			if (preenchido(usuario.getSenha(), 6))
				that(usuario.getSenha().equals(usuario.getConfirmaSenha()), "usuario.confirmaSenha", "confirmacaoSenha.incorreta");
		}});
		validator.onErrorUsePageOf(this).formNovoUsuario(usuario);

		if (dao.existeEmail(usuario.getEmail()))
			validator.add(new ValidationMessage(getMessage("jaCadastrado"), "usuario.email"));
		validator.onErrorRedirectTo(this).formNovoUsuario(usuario);
		
		Transaction tx = dao.abreTransacao();
		
		try {
						
			usuario.setConfirmacaoEmail(System.currentTimeMillis()); // Para calculo do numero de dias para confirmacao do e-mail
			usuario.setUltimoAcesso(new Date());		
			usuario.setSenha(Util.encripta(usuario.getSenha()));
			usuario.setPerfil(PerfilUsuario.USUARIO);
			
			dao.incluir(usuario);

			tx.commit();
			
			sessao.login(usuario);

			enviarConfirmacaoEmail(usuario);
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formNovoUsuario.falha.adicionar"));
			result.redirectTo(this).formNovoUsuario(usuario);			
		} catch (EmailException e) {
			sessao.addErro(e.getMessage() + " " + getMessage("confirmacaoEmail.reenviar"));
		}

		result.redirectTo("/bem-vindo");
	}


	/**
	 * Form de usuario para alteracao pelo pr�prio usuario ou inclusao/exclusao pelo administrador
	 * @param usuario
	 * @return
	 */
	@Get @Path({"/usuarios/incluir", "/usuarios/{usuario.id}/editar"})
	public Usuario formUsuario(Usuario usuario) {

		validaSessao();		

		// Se for inclusao (usuario == null), so o administrador tem acesso
		// senao, tem que ser os dados do usuario logado
		if (usuario == null || !sessao.getIdUsuario().equals(usuario.getId()))
			validaSessao(PerfilUsuario.ADMIN);
			
		try {

			if (usuario == null)
				usuario = new Usuario();
			else if (usuario.getId() != null && usuario.getVersao() == null)
				usuario = consultarPorId(usuario.getId());
			
			result.include("perfis", PerfilUsuario.values());
			result.include("estados", Estado.values());
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formUsuario.falha.carregar"));
			result.redirectTo("/usuarios/listar");
		}
		
		return usuario;
	}		
	
	/**
	 * Form de usuario para alteracao pelo pr�prio usuario ou inclusao/exclusao pelo administrador
	 * @param usuario
	 * @return
	 */
	@Get @Path("/usuarios/{id}/excluir")
	public void formUsuario(Long id) {
		
		validaSessao(PerfilUsuario.ADMIN);
		
		try {
			
			Usuario usuario = consultarPorId(id);
			
			Transaction tx = dao.getSession().beginTransaction();
			
			dao.excluir(usuario);
			
			tx.commit();
			
			sessao.addMensagem(getMessage("formUsuario.excluir.sucesso"));
			result.redirectTo("/usuarios/listar");
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("formUsuario.falha.excluir"));
			result.redirectTo("/usuarios/listar");
		}
		
	}	
	
	@Put @Post @Path("/usuarios/salvar")
	public void salvar(final Usuario usuario, String acao, final UploadedFile imagem) {
		
		// So valida campos se nao for remocao nem recuperacao
		if (acao == null || !"remover|recuperar".contains(acao)) {
			
			validator.checking(new Validations() {{
				that(preenchido(usuario.getPerfil()), "usuario.perfil", "obrigatorio");
				that(preenchido(usuario.getNome()), "usuario.nome", "obrigatorio");
				that(maximo(usuario.getApelido(), 25), "usuario.apelido", "tamanho.maximo", 25);
				that(preenchido(usuario.getEmail()), "usuario.email", "obrigatorio");
				that(emailValido(usuario.getEmail()), "usuario.email", "invalido");
				if (usuario.getId() == null || preenchido(usuario.getSenha()) || preenchido(usuario.getConfirmaSenha())) {
					that(preenchido(usuario.getSenha(), 6), "usuario.senha", "tamanho.minimo", 6);
					if (preenchido(usuario.getSenha(), 6))
						that(usuario.getSenha().equals(usuario.getConfirmaSenha()), "usuario.confirmaSenha", "confirmacaoSenha.incorreta");
				}
			}});
			validator.onErrorRedirectTo(this).formUsuario(usuario);
			
		}
		
		// Verifica se o e-mail ja existe
		Usuario usuarioBanco = dao.buscarPorEmail(usuario.getEmail());
		if (usuarioBanco != null && !usuarioBanco.getId().equals(usuario.getId()))
			validator.add(new ValidationMessage(getMessage("jaCadastrado"), "usuario.email"));
		validator.onErrorRedirectTo(this).formUsuario(usuario);
		
		Transaction tx = dao.getSession().beginTransaction();
		
		try {

			if (preenchido(usuario.getSenha()))
				usuario.setSenha(Util.encripta(usuario.getSenha()));

			if (imagem != null)
				salvarImagem(usuario, imagem);
					
			boolean enviarEmail = false;
			
			if (usuario.getId() == null) {
			
				usuario.setConfirmacaoEmail(System.currentTimeMillis());
				
				dao.incluir(usuario);								
				
			} else {
								
				usuarioBanco = dao.buscarPorId(usuario.getId());

				// Se nao for adminstrador logado ou se for alteracao dos dados do proprio usuario, 
				// mantem o perfil, pois o usuario nao pode alterar seu proprio perfil.
				if (!sessao.isAdministrador() || sessao.getIdUsuario().equals(usuario.getId())) 
					usuario.setPerfil(usuarioBanco.getPerfil());
				
				// Se houve alteracao no email, reseta prazo de confirmacao e envia e-mail
				if (usuarioBanco != null && !usuarioBanco.getEmail().equalsIgnoreCase(usuario.getEmail())) 
					enviarEmail  = true;
				
				dao.alterarUsuario(usuario);
			}
			
			if (usuario.getEndereco() != null && preenchido(usuario.getEndereco().getLogradouro())) {
				if (usuario.getEndereco().getId() == null)
					dao.incluir(usuario.getEndereco());
				else
					dao.alterar(usuario.getEndereco());
			} else {
				usuario.setEndereco(null);
			}
			
			tx.commit();
			
			if (enviarEmail)
				enviarConfirmacaoEmail(usuario);
				
			sessao.addMensagem(getMessage("formUsuario.sucesso"));
			result.redirectTo("/usuarios/listar");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formUsuario.falha.salvar"));
			result.redirectTo(this).formUsuario(usuario);
		} catch (FileNotFoundException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formPerfil.falha.upload"));
			result.redirectTo(this).formUsuario(usuario);
		} catch (IOException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formPerfil.falha.upload"));
			result.redirectTo(this).formUsuario(usuario);
		} catch (EmailException e) {
			log.error(e.getMessage());
			sessao.addErro(e.getMessage() + " " + getMessage("confirmacaoEmail.reenviar"));
			result.redirectTo(this).formUsuario(usuario);
		}
		
		
	}
			
	
	@Get @Path("/bem-vindo")
	public void bemVindo() {
		validaSessao();
	}

	@Get @Path("/login")
	public void formLogin() {
		sessao.logout();
	}
	
	@Post @Path("/login")
	public void login(final Usuario usuario) {
		validator.checking(new Validations() {{
			that(preenchido(usuario.getEmail()), "usuario.email", "obrigatorio");
			that(preenchido(usuario.getSenha()), "usuario.senha", "obrigatoria");
		}});
		validator.onErrorUsePageOf(this).formLogin();

		Transaction tx = dao.abreTransacao();
		
		try {
		
			Usuario usuarioValido = dao.autenticarUsuario(usuario.getEmail(), Util.encripta(usuario.getSenha()));
			if (usuarioValido == null) {
				tx.rollback();
				validator.add(new ValidationMessage(getMessage("formLogin.loginInvalido"), "semCategoria"));
			}
			validator.onErrorRedirectTo(this).formLogin();
			
			long limiteDias = new Long(getConfig("config.diasConfirmacao"));
			
			if (Boolean.TRUE.equals(usuarioValido.getBloqueado())) {
				tx.rollback();
				validator.add(new ValidationMessage(getMessage("usuarioBloqueado", limiteDias) + " " + getMessage("confirmacaoEmail.reenviar"), "semCategoria"));
			}
			validator.onErrorRedirectTo(this).formLogin();
	
			sessao.login(usuarioValido);
			
			Cookie cookie = new Cookie(SessaoInterceptor.COOKIE_USUARIO, Util.encripta(usuario.getSenha()) + "," + usuarioValido.getId());
			if (Boolean.TRUE.equals(usuario.getLembrar()))
				cookie.setMaxAge(7 * 24 * 60 * 60);
			else
				cookie.setMaxAge(0); // Apaga o cookie
			response.addCookie(cookie);
			
			// Verifica se a confirmacao de e-mail ainda nao foi feita
			if (usuarioValido.getConfirmacaoEmail() != 1L) {
				long difDias = Util.diferencaDias(new Date(usuarioValido.getConfirmacaoEmail()), new Date());
				long diasAviso = new Long(getConfig("config.diasAviso"));
				if (difDias >= limiteDias) {
					dao.bloquearUsuario(usuarioValido.getId());
					sessao.addMensagem(getMessage("prazoConfirmacaoExpirado"));
				} else if (difDias >= diasAviso) {
					sessao.addMensagem(getMessage("emailNaoConfirmado", new Long(limiteDias - difDias).toString()) + " " + getMessage("confirmacaoEmail.reenviar"));
				}
			}
			
			// Verifica se existe lancamento de vale refeicao para o mes corrente
			if (!dao.checarBonificacao())
				sessao.addErro(Util.getMessage("bonificacao.naoLancada"));
			
			// Verifica se os dissidio ja foram lancados
			if (!dao.checarDissidio())
				sessao.addErro(Util.getMessage("dissidio.naoLancado"));

			tx.commit();
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formLogin.falha.login"));
		}
		
		String acao = sessao.getAcao(); // Recupera a ultima acao do usuario para redirecionamento
		result.redirectTo(acao != null ? acao : "/");
	}

	@Path("/logout")
	public void logout() {
		sessao.logout();
		Cookie cookie = new Cookie(SessaoInterceptor.COOKIE_USUARIO, "#");
		cookie.setMaxAge(0); // Apaga o cookie
		response.addCookie(cookie);
		result.redirectTo("/");
	}
	
	@Get @Path("/troca-senha")
	public void formTrocaSenha() {
		validaSessao("/troca-senha");
	}
	
	@Put @Path("/troca-senha")
	public void trocaSenha(final Usuario usuario) {
		validator.checking(new Validations() {{
			that(preenchido(usuario.getSenha(), 6), "usuario.senha", "tamanho.minimo", 6);
			if (preenchido(usuario.getSenha(), 6))
				that(usuario.getSenha().equals(usuario.getConfirmaSenha()), "usuario.confirmaSenha", "confirmacaoSenha.incorreta");
		}});
		validator.onErrorUsePageOf(this).formTrocaSenha();

		Transaction tx = dao.abreTransacao();
		
		try {
		
			usuario.setId(sessao.getIdUsuario());
			usuario.setSenha(Util.encripta(usuario.getSenha()));
			dao.trocarSenha(usuario);
	
			tx.commit();
			
			sessao.addMensagem(getMessage("formTrocaSenha.sucesso"));
			result.redirectTo("/");
			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("formTrocaSenha.falha.salvar"));
			result.redirectTo(this).formTrocaSenha();
		}
	}

	@Get @Path("/redefinir-senha")
	public void redefinirSenha() {
		if (sessao.isLogado())
			result.redirectTo("/troca-senha");
	}
	
	@Put @Path("/redefinir-senha")
	public void enviaRedefinicaoSenha(final String email) {
		validator.checking(new Validations() {{
			that(preenchido(email), "usuario.email", "obrigatorio");
			if (preenchido(email))
				that(emailValido(email), "usuario.email", "invalido");
		}});
		validator.onErrorUsePageOf(this).redefinirSenha();
		
		try {
			
			Usuario usuario = buscarPorEmail(email);
			
			String token = Util.encripta(PREFIXO_TOKEN + usuario.getUltimoAcesso());
			String link = getConfig("config.url") + "/redefinir-senha/" + usuario.getId().toString() + "/" + token;
			System.out.println(link);
			
			mailer.enviarEmail("redefinirSenha", new String[] {usuario.getNomeCurto(), link}, new String[] {email}); 
			
			sessao.addMensagem(getMessage("redefinirSenha.sucesso"));	
			
			result.redirectTo("/");
			
		} catch (MessagingException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("redefinirSenha.falha.email"));
			result.redirectTo("/redefinir-senha");
		}

	}
	
	@Get @Path("/redefinir-senha/{id}/{token}")
	public void redefinirSenha(Long id, String token) {

		try {
		
			Usuario usuario = consultarPorId(id);
			if (usuario != null && token.equals(Util.encripta(PREFIXO_TOKEN + usuario.getUltimoAcesso()))) {
				sessao.login(usuario);
				result.redirectTo("/troca-senha");			
			} else {
				sessao.addMensagem(getMessage("redefinirSenha.tokenInvalido"));
				result.redirectTo("/");			
			}
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("redefinirSenha.falha.redefinir"));
			result.redirectTo(this).redefinirSenha();
		}
	}
	
	@Get @Path("/confirmacao-email")
	public void confirmacaoEmail() {
	}
	
	@Put @Path("/confirmacao-email")
	public void enviaConfirmacaoEmail(final String email) {

		try {
		
			if (sessao.isLogado()) {
				
				enviarConfirmacaoEmail(sessao.getUsuario());
				
			} else if (!sessao.isLogado()) {
				
				validator.checking(new Validations() {{
					that(preenchido(email), "usuario.email", "obrigatorio");
					that(emailValido(email), "usuario.email", "invalido");
				}});
				validator.onErrorUsePageOf(this).confirmacaoEmail();

				enviarConfirmacaoEmail(email);
			}
		
			result.redirectTo("/");

		} catch (EmailException e) {
			sessao.addMensagem(e.getMessage());
			result.redirectTo(this).confirmacaoEmail();
		}		
	}
	
	@Get @Path("/confirmacao-email/{id}/{token}")
	public void confirmarEmail(Long id, String token) {

		Transaction tx = dao.abreTransacao();
		
		try {
			
			Usuario usuario = consultarPorId(id);
			
			if (token.equals(Util.encripta(PREFIXO_TOKEN + usuario.getEmail()))) {

				// So muda o perfil do usuario para administrador na confirmacao do email.
				if (Util.getConfig("config.administradores").contains(usuario.getEmail()))
					usuario.setPerfil(PerfilUsuario.ADMIN);
					
				dao.confirmarEmail(usuario);
				usuario.setConfirmacaoEmail(1L);
				sessao.login(usuario);
				sessao.addMensagem(getMessage("emailConfirmado"));
			} else {
				sessao.addMensagem(getMessage("confirmacaoEmail.tokenInvalido"));
			}
			
			tx.commit();
			
			result.redirectTo("/");			
		} catch (HibernateException e) {
			tx.rollback();
			log.error(e.getMessage());
			sessao.addErro(getMessage("confirmacaoEmail.falha.confirmar"));
			result.redirectTo(this).confirmacaoEmail();
		}
	}
	
	@Get @Path("/confirmacao-email-enviada")
	public void confirmacaoEmailEnviada() {
		validaSessao();
	}
	
	private void enviarConfirmacaoEmail(final String email) throws EmailException {
		enviarConfirmacaoEmail(buscarPorEmail(email));		
	}
	
	private void enviarConfirmacaoEmail(final Usuario usuario) throws EmailException {
			
		try {
						
			String token = Util.encripta(PREFIXO_TOKEN + usuario.getEmail());
			String link = getConfig("config.url") + "/confirmacao-email/" + usuario.getId() + "/" + token;
			System.out.println(link);

			mailer.enviarEmail("confirmacaoEmail", new String[] {usuario.getNomeCurto(), link}, new String[] {usuario.getEmail()});

		} catch (MessagingException e) {
			log.error(e.getMessage());
			throw new EmailException(getMessage("confirmacaoEmail.falha.email"));
		}

		sessao.addMensagem(getMessage("confirmacaoEmail.sucesso"));
			
	}
		
	private void salvarImagem(Usuario usuario, UploadedFile imagem) throws FileNotFoundException, IOException {
		
		// Grava os arquivos em dirImagens
		if (imagem != null) {
			File arqTemp = new File(dirPerfil, "temp" + usuario.getId());   
			IOUtils.copyLarge(imagem.getFile(), new FileOutputStream(arqTemp));
			reduzirImagem(imagem, arqTemp, new File(dirPerfil, IMG_PERFIL + usuario.getId()), 100, 100);
			reduzirImagem(imagem, arqTemp, new File(dirPerfil, TBN_PERFIL + usuario.getId()), 75, 75);
			arqTemp.delete();
			usuario.setImagem(imagem.getFileName());
		}
				
	}
	
	@Get @Path("/usuarios/{id}/imagem")
	public File buscarImagem(String id) {
		return getImagem(dirPerfil, IMG_PERFIL, id);
	}
	
	@Get @Path("/usuarios/{id}/thumbnail")
	public File buscarMiniatura(String id) {
		return getImagem(dirPerfil, TBN_PERFIL, id);
	}
	
	@Get @Path("/portfolios/{id}/detalhar")
	public void detalharPortfolio(Long id) {
		result.redirectTo("/desenhos/listar/portfolio/" + id);
	}
	
	private Usuario consultarPorId(Long id) {
		
		Usuario usuario = null;
		try {
			
			usuario = dao.consultarPorId(id);

			if (usuario == null)
				validator.add(new ValidationMessage(getMessage("usuario.validacao.naoExiste", id), "semCategoria"));

			validator.onErrorRedirectTo(IndexController.class).home();
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("usuario.falha.recuperar.id", id));
			result.forwardTo(IndexController.class).home();
		}
		
		return usuario;
	}

	private Usuario buscarPorEmail(String email) {
		
		Usuario usuario = null;
		try {
			
			usuario = dao.buscarPorEmail(email);
			if (usuario == null)
				validator.add(new ValidationMessage(getMessage("naoEncontrado"), "usuario.email"));
			validator.onErrorRedirectTo(this).redefinirSenha();
	
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("usuario.falha.recuperar.email", email));
			result.forwardTo(IndexController.class).home();
		}
		
		return usuario;
	}
	
	@Get @Post @Path("/usuarios/listar")
	public void listarUsuarios(FiltroUsuario filtro) {

		validaSessao(PerfilUsuario.OPERADOR);

		try {
		
			if (filtro == null || filtro.getPaginacao().getAcao() == Paginacao.LIMPAR)
				filtro = FiltroUsuario.newInstance(getRegistrosPagina());

			for (OrdenacaoUsuario ordem: OrdenacaoUsuario.values())
				filtro.addOrdencacao(ordem);
			
			filtro.getPaginacao().atualizar();
		
			result.include("filtro", filtro);
			result.include("listaUsuarios", dao.filtrar(filtro));
			
		} catch (HibernateException e) {
			log.error(e.getMessage());
			sessao.addErro(getMessage("listarUsuarios.falha.listar"));
			result.redirectTo("/");
		}	
	}

}
