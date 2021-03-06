package stallum.modelo;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Empresa extends AbstractModelo {
	private static final long serialVersionUID = 6488002285009322320L;

	@NotEmpty
	private String nomeCurto;
	@NotEmpty
	private String razaoSocial;
	@NotEmpty
	private String cnpj;
	private String inscricao;
	private String ddd1;
	@NotEmpty
	private String telefone1;
	private String ddd2;
	private String telefone2;
	@Email
	private String email;
	
	@OneToOne
	private Endereco endereco;
	
	private Boolean ativa;

	public String getNomeCurto() {
		if (nomeCurto == null && razaoSocial != null && razaoSocial.length() > 20)
			return razaoSocial.substring(0, 20) + "...";
		return nomeCurto;
	}

	public void setNomeCurto(String nomeCurto) {
		this.nomeCurto = nomeCurto;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricao() {
		return inscricao;
	}

	public void setInscricao(String inscricao) {
		this.inscricao = inscricao;
	}

	public String getDdd1() {
		return ddd1;
	}

	public void setDdd1(String ddd1) {
		this.ddd1 = ddd1;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getDdd2() {
		return ddd2;
	}

	public void setDdd2(String ddd2) {
		this.ddd2 = ddd2;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Boolean getAtiva() {
		return !Boolean.FALSE.equals(ativa);
	}

	public void setAtiva(Boolean ativa) {
		this.ativa = ativa;
	}
}
