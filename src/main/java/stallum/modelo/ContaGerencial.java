package stallum.modelo;

import java.math.BigDecimal;

import javax.persistence.Entity;

import stallum.enums.ClasseContaGerencial;

@Entity
public class ContaGerencial extends AbstractModelo {
	private static final long serialVersionUID = 6407887344173770547L;
	
	private String numero;
	private String nome;
	private Boolean totalizadora = false;
	private ClasseContaGerencial classe;
	private BigDecimal saldo;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getTotalizadora() {
		return totalizadora;
	}

	public void setTotalizadora(Boolean totalizadora) {
		this.totalizadora = totalizadora;
	}
	
	public ClasseContaGerencial getClasse() {
		return classe;
	}

	public void setClasse(ClasseContaGerencial classe) {
		this.classe = classe;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public void debita(BigDecimal valor) {
		if (saldo == null)
			saldo = BigDecimal.ZERO;
		saldo = saldo.subtract(valor);
	}

	public void credita(BigDecimal valor) {
		if (saldo == null)
			saldo = BigDecimal.ZERO;
		saldo = saldo.add(valor);
	}

	@Override
	public String toString() {
		if (nome == null)
			return null;
		
		String nomeCurto = nome;
		if (nome.length() > 20)
			nomeCurto = nome.toUpperCase().substring(0, 20);
		return "(" + numero + ") " + nomeCurto;
	}
}
