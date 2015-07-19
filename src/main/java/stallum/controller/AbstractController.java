package stallum.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import stallum.enums.PerfilUsuario;
import stallum.infra.Mailer;
import stallum.infra.SessaoUsuario;
import stallum.infra.Util;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.validator.ValidationMessage;

public abstract class AbstractController {

	protected final Result result;
	protected final Validator validator;
	protected final Localization i18n;
	protected final SessaoUsuario sessao;
	protected final Mailer mailer;
	
	private final File dirImagens;
	
	public AbstractController(Validator validator, Localization i18n, Result result, SessaoUsuario sessao) {
		this.result = result;
		this.validator = validator;
		this.i18n = i18n;
		this.sessao = sessao;
		
		this.mailer = new Mailer(i18n.getLocale());
		
		result.include("versao", getConfig("config.versao"));
		
		dirImagens = new File(getConfig("config.dir.imagens"));
		if (!dirImagens.isDirectory())
			dirImagens.mkdir();
		
	}
	
	protected String getMessage(String chave, Object... param) {
		return i18n.getMessage(chave, param);
	}
	
	protected String getConfig(String chave) {
		return Util.getConfig(chave);
	}
	
	protected Integer getRegistrosPagina() {
		return new Integer(getConfig("config.registrosPagina"));
	}
	
	protected boolean preenchido(Object obj) {
		return obj != null && obj.toString().length() > 0;
	}
	
	protected boolean preenchido(Object obj, int tamMin) {
		return obj != null && obj.toString().length() >= tamMin;
	}
	
	protected boolean maximo(Object obj, int tamMax) {
		return obj == null || obj.toString().length() <= tamMax;
	}

	protected boolean emailValido(String email) {
		return Util.isEmailValido(email);
	}
	
	protected boolean cpfCnpjValido(String numero) {
		return Util.isCpfCnpjValido(numero);
	}
	
	protected boolean urlValida(String url) {
		return Util.isUrlValida(url);
	}
	
	/**
	 * Valida a sess‹o so usu‡rio e, se este n‹o estiver logado, redireciona para a tela de login, 
	 * guardando a opera�‹o desejada para redirecionamento ap—s login v‡lido.
	 * @param acao A�‹o para a qual ser‡ redirecionada ap—s o login.
	 */
	protected void validaSessao(String acao) {
		if (!sessao.isLogado()) {
			validator.add(new ValidationMessage(getMessage("loginRequerido"), "semCategoria"));
			sessao.setAcao(acao);
		}
		validator.onErrorRedirectTo(UsuariosController.class).formLogin();
	}
	
	/**
	 * Valida a sess‹o do usu‡rio e, se este n‹o estiver logado, 
	 * redireciona para a tela inicial do sistema exibindo mensagem 'semPermissao'.
	 */
	protected void validaSessao() {
		if (!sessao.isLogado())
			validator.add(new ValidationMessage(getMessage("semPermissao"), "semCategoria"));
		validator.onErrorRedirectTo(IndexController.class).home();
	}
	
	/**
	 * Valida a sess‹o so usu‡rio e, se este n‹o estiver logado com o perfil informado, 
	 * redireciona para a tela inicial do sistema exibindo mensagem 'semPermissao'.
	 */
	protected void validaSessao(PerfilUsuario perfil) {
		if (!sessao.isLogado() || sessao.getUsuario().getPerfil().compareTo(perfil) < 0)			
			validator.add(new ValidationMessage(getMessage("semPermissao"), "semCategoria"));
		validator.onErrorRedirectTo(IndexController.class).home();
	}
	
	protected File getImagem(File diretorio, String prefixo, String idRegistro) {
		
		File imagem = new File(diretorio, prefixo + idRegistro);
		
		if (!imagem.isFile())
			imagem = new File(dirImagens, prefixo + 0);
		
		return imagem;
	}
	
	protected void reduzirImagem(UploadedFile imagem, File origem, File destino, int larg, int alt) throws IOException {
		reduzirImagem(imagem, origem, destino, larg, alt, false);
	}
		
	protected void reduzirImagem(UploadedFile imagem, File origem, File destino, int larg, int alt, boolean comFundo) throws IOException {
		
		BufferedImage img = ImageIO.read(origem);
		double altImg = img.getHeight();
		double larImg = img.getWidth();
		double escala = 1.0;
		
		if (altImg > larImg)
			escala = alt / altImg;
		else
			escala = larg / larImg;
		
		// So ajusta altera as dimencoes da imagem se form reducao
		if (escala < 1.0) {
			larImg = larImg * escala;
			altImg = altImg * escala;
		}
		
		Double novaLarg = Math.floor(larImg) + (Math.floor(larImg) == larImg ? 0 : 1);
		Double novaAlt = Math.floor(altImg) + (Math.floor(altImg) == altImg ? 0 : 1);
		int x = new Double((larg - novaLarg) / 2).intValue();
		int y = new Double((alt - novaAlt) / 2).intValue();
		
		String tipo = "JPG";
		if (imagem.getContentType().contains("png"))
			tipo = "PNG";
		else if (imagem.getContentType().contains("gif"))
			tipo = "GIF";
		
		BufferedImage novaImagem = new BufferedImage(larg, alt, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graph2D = novaImagem.createGraphics();
		
		if (comFundo)
			graph2D.setColor(new Color(225, 225, 225));
		else
			graph2D.setColor(new Color(255, 255, 255));

		graph2D.fillRect(0, 0, larg, alt);
		graph2D.drawImage(img.getScaledInstance(novaLarg.intValue(), novaAlt.intValue(), Image.SCALE_SMOOTH), x, y, null);
		
		ImageIO.write(novaImagem, tipo, destino);
			
	}
    
}
