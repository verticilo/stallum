package stallum.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractModelo implements Serializable {
	private static final long serialVersionUID = -1814926558385648532L;

	@Id
	@GeneratedValue
	private Long id;

	private Date criadoEm;

//	@Version
	private Date alteradoEm;
	
	private transient Long versao;

	public AbstractModelo() {
		if (criadoEm == null)
			criadoEm = new Date(System.currentTimeMillis());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCriadoEm() {
		return criadoEm;
	}

	public void setCriadoEm(Date criadoEm) {
		this.criadoEm = criadoEm;
	}

	public Date getAlteradoEm() {
		return alteradoEm;
	}

	public void setAlteradoEm(Date alteradoEm) {
		this.alteradoEm = alteradoEm;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
		if (versao != null)
			this.alteradoEm = new Date(versao);
	}
	
	public Long getVersao() {
		if (alteradoEm != null)
			return alteradoEm.getTime();
		return versao;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		AbstractModelo other = (AbstractModelo) obj;
		return this == other || (getId() != null && getId().equals(other.getId()));
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

}
