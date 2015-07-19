package stallum.infra;

import java.util.Collection;
import java.util.List;

import org.hibernate.transform.ResultTransformer;

public class HibernateTransformer extends AbstractTransformer implements ResultTransformer {
	private static final long serialVersionUID = 169198571393098497L;

	@SuppressWarnings("rawtypes")
	public HibernateTransformer(Class resultClass, String key) {
		super(resultClass, key);
	}

	@SuppressWarnings("rawtypes")
	public HibernateTransformer(Class resultClass, String[] key) {
		super(resultClass, key);
	}

	@SuppressWarnings("rawtypes")
	public HibernateTransformer(Class resultClass) {
		super(resultClass);
	}
	
	@SuppressWarnings("rawtypes")
	public HibernateTransformer(Class resultClass, Collection result)
			throws IllegalAccessException, NoSuchFieldException {
		super(resultClass, result);
	}

	@SuppressWarnings("rawtypes")
	public HibernateTransformer(Class resultClass, String key,
			Collection result) throws IllegalAccessException,
			NoSuchFieldException {
		super(resultClass, key, result);
	}

	@SuppressWarnings("rawtypes")
	public HibernateTransformer(Class resultClass, String[] key,
			Collection result) throws IllegalAccessException,
			NoSuchFieldException {
		super(resultClass, key, result);
	}

	@SuppressWarnings("rawtypes")
	public List transformList(List arg0) {
		return super.transformResult();
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		return super.transformTuple(tuple, aliases);
	}
	
}