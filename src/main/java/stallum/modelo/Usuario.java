package stallum.modelo;

import java.util.Date;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import stallum.enums.PerfilUsuario;
import stallum.infra.Util;

@Entity
public class Usuario extends AbstractModelo {
	private static final long serialVersionUID = 503547292649712511L;
	
	@NotEmpty @Email
	private String email;
	@NotEmpty
	private String nome;
	private String cpf;
	private String rg;
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	private String dddTelefone;
	private String telefone;
	private String celular;
	private String dddCelular;
	@NotEmpty
	private String senha;
	@Transient
	private String confirmaSenha;
	private Boolean newsletter;
	private Boolean lembrar;
	@NotEmpty
	private Long confirmacaoEmail;
	@NotEmpty
	@Temporal(TemporalType.DATE)
	private Date ultimoAcesso;
	private Locale locale;
	private Boolean bloqueado;
	private PerfilUsuario perfil;
	private String apelido;
	private String imagem;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Endereco endereco;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
		
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getApelido() {
		return getNomeCurto();
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getDddTelefone() {
		return dddTelefone;
	}

	public void setDddTelefone(String dddTelefone) {
		this.dddTelefone = dddTelefone;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getDddCelular() {
		return dddCelular;
	}

	public void setDddCelular(String dddCelular) {
		this.dddCelular = dddCelular;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public Boolean getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(Boolean newsletter) {
		this.newsletter = newsletter;
	}

	public Boolean getLembrar() {
		return lembrar;
	}

	public void setLembrar(Boolean lembrar) {
		this.lembrar = lembrar;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Long getConfirmacaoEmail() {
		return confirmacaoEmail;
	}

	public void setConfirmacaoEmail(Long confirmacaoEmail) {
		this.confirmacaoEmail = confirmacaoEmail;
	}

	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	public PerfilUsuario getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilUsuario perfil) {
		this.perfil = perfil;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public String getNomeCurto() {
		if (!Util.isVazioOuNulo(apelido))
			return apelido;
		
		if (nome != null && nome.length() > 20)
			return nome.substring(0, 20) + "...";
		
		return nome;
	}
	
	public String getDddContato() {
		if (!Util.isVazioOuNulo(dddTelefone))
			return dddTelefone;
		if (!Util.isVazioOuNulo(dddCelular))
			return dddCelular;
		return null;
	}
	
	public String getTelContato() {
		if (!Util.isVazioOuNulo(telefone))
			return telefone;
		if (!Util.isVazioOuNulo(celular))
			return celular;
		return null;
	}

}
