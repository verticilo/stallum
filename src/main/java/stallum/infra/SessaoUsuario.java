package stallum.infra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import stallum.enums.ItemMenu;
import stallum.enums.PerfilUsuario;
import stallum.modelo.Usuario;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class SessaoUsuario implements Serializable {
	private static final long serialVersionUID = -3315603751302476572L;

	private Usuario usuario;
	private String acao;
	private Collection<ItemMenu> menu;
	private Collection<String> mensagens = null;
	private Collection<String> erros = null;
	
	public void login(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public void logout() {
		usuario = null;
	}
	
	public Long getIdUsuario() {
		if (usuario == null)
			return 0L;
		return usuario.getId();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Collection<ItemMenu> getMenu() {
		return menu;
	}

	public void setMenu(Collection<ItemMenu> menu) {
		this.menu = menu;
	}

	public String getNome() {
		return usuario.getNome();
	}
	
	public String getNomeCurto() {
		return usuario.getNomeCurto();
	}
	
	public boolean isLogado() {
		return usuario != null;
	}
	
	public boolean isCliente() {
		return isLogado() && usuario.getPerfil().compareTo(PerfilUsuario.CLIENTE) >= 0;
	}
	
	public boolean isOperador() {
		return isLogado() && usuario.getPerfil().compareTo(PerfilUsuario.OPERADOR) >= 0;
	}

	public boolean isGerente() {
		return isLogado() && usuario.getPerfil().compareTo(PerfilUsuario.GERENTE) >= 0;
	}
	
	public boolean isAdministrador() {
		return isLogado() && usuario.getPerfil().equals(PerfilUsuario.ADMIN);
	}
	
	public boolean isEmailConfirmado() {
		return Long.valueOf(1).equals(usuario.getConfirmacaoEmail());
	}
	
	public String getAcao() {
		String ret = acao;
		acao = null;
		return ret;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Collection<String> getMensagens() {
		Collection<String> ret = mensagens;
		mensagens = null;
		return ret;
	}
	
	public boolean getComErro() {
		if (erros == null)
			erros = new ArrayList<String>();
		return erros.size() > 0;
	}
	
	public Collection<String> getErros() {
		Collection<String> ret = erros;
		erros = null;
		return ret;
	}
	
	public void addMensagem(String mensagem) {
		if (mensagens == null)
			mensagens = new ArrayList<String>();
		mensagens.add(mensagem);
	}
	
	public void addErro(String erro) {
		if (erros == null)
			erros = new ArrayList<String>();
		erros.add(erro);
	}
	
}
