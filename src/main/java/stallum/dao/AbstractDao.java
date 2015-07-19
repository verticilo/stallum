package stallum.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import stallum.infra.SessaoUsuario;

public class AbstractDao {

	protected final Session session;
	protected final SessaoUsuario sessaoUsuario;

	public AbstractDao(Session session, SessaoUsuario sessaoUsuario) {
		this.session = session;
		this.sessaoUsuario = sessaoUsuario;
	}
	
	public Session getSession() {
		return session;
	}
	
	public Transaction abreTransacao() {
		return getSession().beginTransaction();
	}
	
	public void incluir(Object obj) {
		getSession().save(obj);
	}
	
	public void alterar(Object obj) {
		getSession().update(obj);
	}
	
	public void salvar(Object obj) {
		getSession().saveOrUpdate(obj);
	}
	
	public void excluir(Object obj) {
		getSession().delete(obj);
	}
	
}
