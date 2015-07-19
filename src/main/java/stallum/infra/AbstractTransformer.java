package stallum.infra;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@SuppressWarnings("rawtypes")
public abstract class AbstractTransformer implements Serializable {
	private static final long serialVersionUID = -5312920917058229235L;

	private static final String RESULT_CLASS = "resultClass";
	private static final String[] KEY_DEFAULT = { "id" };
	private Map groups = new HashMap();
	private String delimitador = ".";
	private List array = new ArrayList();
	private Map arrayAlias = new HashMap();
	private Collection pathGrupos;
	private String delId = "#_#";

	public AbstractTransformer(Class resultClass) {
		this(resultClass, KEY_DEFAULT);
	}

	public AbstractTransformer(Class resultClass, String[] key) {
		if (resultClass == null) {
			throw new IllegalArgumentException("resultClass cannot be null");
		}
		if (key == null) {
			key = KEY_DEFAULT;
		}
		addGroup(RESULT_CLASS, resultClass, key);
	}

	public AbstractTransformer(Class resultClass, String key) {
		if (resultClass == null) {
			throw new IllegalArgumentException("resultClass cannot be null");
		}
		if (key == null) {
			addGroup(RESULT_CLASS, resultClass, KEY_DEFAULT);
		} else {
			addGroup(RESULT_CLASS, resultClass, key);
		}
	}

	public AbstractTransformer(Class resultClass, Collection result)
			throws IllegalAccessException, NoSuchFieldException {
		this(resultClass, KEY_DEFAULT);
		tratarResultInit(result);
	}

	public AbstractTransformer(Class resultClass, String[] key,
			Collection result) throws IllegalAccessException,
			NoSuchFieldException {
		this(resultClass, key);
		tratarResultInit(result);
	}

	public AbstractTransformer(Class resultClass, String key, Collection result)
			throws IllegalAccessException, NoSuchFieldException {
		this(resultClass, key);
		tratarResultInit(result);
	}

	@SuppressWarnings("unchecked")
	private void tratarResultInit(Collection result)
			throws IllegalAccessException, NoSuchFieldException {
		if (result != null && result.size() > 0) {
			GroupTransformer grupo = getGroup(RESULT_CLASS);
			array = new ArrayList(result);

			for (Iterator iterator = result.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				String keyValue = "";
				for (int i = 0; i < grupo.getKey().length; i++) {
					String k = grupo.getKey()[i];
					Object o = getAtributoVO(object, k);
					keyValue += o.toString() + delId;
				}
				grupo.getValues().put(keyValue, object);
			}
		}
	}

	private Object getAtributoVO(Object objeto, String nomeParametro)
			throws IllegalAccessException, NoSuchFieldException {
		Object retorno = null;
		int pos = nomeParametro.indexOf(delimitador);
		String nomeField = nomeParametro;
		Field field = null;
		if (pos > 0) {
			nomeField = nomeParametro.substring(0, pos);
			nomeParametro = nomeParametro.substring(pos + 1);
			// field = Util.getFieldOfClass(objeto.getClass(), nomeField);
			field = ReflexaoUtil.getFieldOfClass(objeto.getClass(), nomeField);
			field.setAccessible(true);
			Object newObject = field.get(objeto);
			retorno = getAtributoVO(newObject, nomeParametro);
		} else {
			// field = Util.getFieldOfClass(objeto.getClass(), nomeField);
			field = ReflexaoUtil.getFieldOfClass(objeto.getClass(), nomeField);
			field.setAccessible(true);
			retorno = field.get(objeto);
		}
		return retorno;
	}

	/**
	 * 
	 * Serve para popular colecao dentro de um VO
	 * 
	 * EX: Collection [0]PessoaVO(id = 1) listaCarro:Collection
	 * [0]carroVO:CarroVO (id = 12) [1]carroVO:CarroVO (id = 13)
	 * 
	 * [1]PessoaVO(id = 2)
	 * 
	 * 
	 * @param classAlias
	 *            - Nome do alias dado na query
	 * @param resultClass
	 *            - Tipo a ser populado dentro da colecao
	 * @param key
	 *            - alias do atributo chave do vo que ser� populado na nova
	 *            cole��o
	 */
	public void addGroup(String classAlias, Class resultClass, String key) {
		String[] arrayKey = new String[1];
		arrayKey[0] = key;
		addGroup(classAlias, resultClass, arrayKey);
	}

	/**
	 * 
	 * Serve para popular colecao dentro de um VO
	 * 
	 * EX: Collection [0]PessoaVO(id = 1) listaCarro:Collection
	 * [0]carroVO:CarroVO (id = 12) [1]carroVO:CarroVO (id = 13)
	 * 
	 * [1]PessoaVO(id = 2)
	 * 
	 * 
	 * @param classAlias
	 *            - Nome do alias dado na query
	 * @param resultClass
	 *            - Tipo a ser populado dentro da colecao
	 * @param key
	 *            - array de alias do atributo chave do vo que ser� populado
	 *            na nova cole��o
	 */
	@SuppressWarnings("unchecked")
	public void addGroup(String classAlias, Class resultClass, String[] key) {
		String path = delimitador;
		if (!classAlias.equals(RESULT_CLASS)) {
			int p = 0;
			String cmp = getAlias(key[0]);
			if ((p = cmp.indexOf(delimitador + classAlias + delimitador)) > -1) {
				path = cmp.substring(0, p + classAlias.length() + 2);
			}
		}
		GroupTransformer group = new GroupTransformer(resultClass, key, path);
		groups.put(classAlias, group);
	}

	/**
	 * Popula a colecao principal com uma hierarquia
	 * 
	 * Collection [0]PessoaVO(id = 1) filhos:Collection [0]pessoaVO: PessoaVO
	 * (id = 12) filhos:Collection [0] pessoaVO: PessoaVO (id = 72) [1]
	 * pessoaVO: PessoaVO (id = 73) [1] pessoaVO: PessoaVO (id = 13)
	 * 
	 * [1]PessoaVO(id = 2) filhos:Collection [0]pessoaVO: PessoaVO (id = 15)
	 * filhos:Collection [0] pessoaVO: PessoaVO (id = 82) [1] pessoaVO: PessoaVO
	 * (id = 83) [1] pessoaVO: PessoaVO (id = 18) [2]PessoaVO(id = 3)
	 * 
	 * @param parentKey
	 *            - Array de chave do atributo que identifica a classe mae
	 * @param pathParentKey
	 *            -
	 */
	public void addHierarchy(String[] parentKey, String pathParentKey) {
		addHierarchy(RESULT_CLASS, parentKey, pathParentKey);
	}

	public void addHierarchy(String parentKey, String pathParentKey) {
		addHierarchy(RESULT_CLASS, parentKey, pathParentKey);
	}

	public void addHierarchy(String classAlias, String[] parentKey,
			String pathParentKey) {
		GroupTransformer group = (GroupTransformer) groups.get(classAlias);
		group.setParentKey(parentKey);
		group.setPathParentKey(pathParentKey);
	}

	public void addHierarchy(String classAlias, String parentKey,
			String pathParentKey) {
		String array[] = new String[1];
		array[0] = parentKey;
		addHierarchy(classAlias, array, pathParentKey);
	}

	protected List transformResult() {
		return array;
	}

	/**
	 * 
	 * @return retorna o delimitador
	 */
	public String getDelimitador() {
		return delimitador;
	}

	/**
	 * EX: pessoa.id - delimitador = "."
	 * 
	 * @param delimitador
	 *            - parametro que separa os atributos
	 */
	public void setDelimitador(String delimitador) {
		this.delimitador = delimitador;
	}

	/**
	 * Usado para quando o alias que tem que ser colocado na query for muito
	 * grande (No oracle existe um limite do alias ter apenas 30 caracteres)
	 * 
	 * Ex: pessoaVO.carroVO.pecaVO.descricao
	 * 
	 * @param column
	 *            - nome da coluna do banco
	 * @param as
	 *            - nome do alias
	 */
	@SuppressWarnings("unchecked")
	public AbstractTransformer addAlias(String column, String as) {
		arrayAlias.put(column.toUpperCase(), as);
		return this;
	}

	@SuppressWarnings("unchecked")
	protected Object transformTuple(Object[] tuple, String[] aliases) {
		Object result = null;
		try {
			pathGrupos = new ArrayList();
			for (Iterator iter = groups.values().iterator(); iter.hasNext();) {

				GroupTransformer group = (GroupTransformer) iter.next();

				String[] keys = group.getKey();
				String[] parentKeys = group.getParentKey();

				String id = "";
				int idCount = 0;
				String idParent = "";
				int idParentCount = 0;

				for (int i = 0; i < aliases.length
						&& (idCount < keys.length || (parentKeys != null && idParentCount < parentKeys.length)); i++) {

					for (int j = 0; j < keys.length && idCount < keys.length; j++) {

						if (getAlias(aliases[i]).toUpperCase().equals(
								getAlias(keys[j]).toUpperCase())
								&& tuple[i] != null) {
							id += tuple[i].toString() + delId;
							idCount++;
							break;
						}
					}

					if (parentKeys != null) {

						for (int j = 0; j < parentKeys.length
								&& idParentCount < parentKeys.length; j++) {

							if (getAlias(aliases[i]).toUpperCase().equals(
									getAlias(parentKeys[j]).toUpperCase())
									&& tuple[i] != null) {
								idParent += tuple[i].toString() + delId;
								idParentCount++;
								break;
							}
						}
					}
				}

				group.setKeyValue(id);
				group.setParentKeyValue(idParent);

				group.setObject(null);
				pathGrupos.add(group.getPath());
			}

			GroupTransformer group = getGroup(RESULT_CLASS);
			result = transformer(array, group, "");
			for (int i = 0; i < aliases.length; i++) {
				try {
					String campo = getAlias(aliases[i]);
					if (tuple[i] != null) {
						boolean executar = false;

						for (Iterator iterator = pathGrupos.iterator(); iterator
								.hasNext();) {
							String p = (String) iterator.next();
							if (p.equals(delimitador) || campo.indexOf(p) > -1) {
								executar = true;
								break;
							}
						}

						if (executar) {
							this.setAttributeVO(result, campo, tuple[i],
									group.getKeyValue());
						}
					}
				} catch (IllegalAccessException e) {
					throw new TransformerException(
							"Could not instantiate result class: "
									+ group.getType().getName(), e);
				} catch (InstantiationException e) {
					throw new TransformerException(
							"Could not instantiate result class: "
									+ group.getType().getName(), e);
				} catch (NoSuchFieldException e) {
					if (!e.getMessage().toLowerCase().equals("rownum_")) {
						throw new TransformerException("No such field '"
								+ aliases[i] + "' found in result class "
								+ group.getType().getName(), e);
					}
				}
			}
		} catch (Exception e) {
			throw new TransformerException(e.getMessage(), e);
		}
		return result;
	}

	private String getAlias(String alias) {
		String result = null;
		if ((result = (String) arrayAlias.get(alias.toUpperCase())) == null) {
			result = alias;
		}
		return result;
	}

	private GroupTransformer getGroup(String classAlias) {

		GroupTransformer group = (GroupTransformer) groups.get(classAlias);
		if (group == null) {
			throw new TransformerException(
					"Not has define a group for the Class " + classAlias);
		}
		return group;
	}

	@SuppressWarnings("unchecked")
	private Object transformer(Collection array, GroupTransformer group,
			String keyValue) throws Exception {
		Object result = null;

		if (group.getObject() == null) {

			String key = keyValue + group.getKeyValue();

			if (key != null) {
				result = group.getValues().get(key);
			}

			if (result == null) {

				result = group.getType().newInstance();
				group.getValues().put(key, result);
				Object parent = null;

				if (!group.getParentKeyValue().equals("")
						&& (parent = group.getValues().get(
								keyValue + group.getParentKeyValue())) != null) {

					Field field = ReflexaoUtil.getFieldOfClass(
							parent.getClass(), group.getPathParentKey());
					field.setAccessible(true);
					Collection children = (Collection) field.get(parent);
					if (children == null) {
						children = (Collection) newInstance(Collection.class);
						ReflexaoUtil.insertValue(field, parent, children);
					}
					children.add(result);
				} else {
					array.add(result);
				}

			} else {
				pathGrupos.remove(group.getPath());
			}
			group.setObject(result);

		} else {
			result = group.getObject();
		}
		return result;
	}

	private void setAttributeVO(Object objeto, String nomeParametro,
			Object valor, String keyValue) throws Exception {
		Object newValor = valor;
		int pos = nomeParametro.indexOf(delimitador);
		String nomeField = nomeParametro;
		Field field = null;
		if (pos > 0) {
			nomeField = nomeParametro.substring(0, pos);
			nomeParametro = nomeParametro.substring(pos + 1);
			field = ReflexaoUtil.getFieldOfClass(objeto.getClass(), nomeField);
			field.setAccessible(true);
			Object newObject = field.get(objeto);
			Class classe = field.getType();
			if (newObject == null) {
				newObject = newInstance(classe);
			}
			if (newObject instanceof Collection) {
				tratarObject((Collection) newObject, nomeParametro, valor,
						keyValue);
				newValor = newObject;
			} else if (!nomeParametro.equals("")) {
				setAttributeVO(newObject, nomeParametro, valor, keyValue);
				newValor = newObject;
			}
		} else {
			field = ReflexaoUtil.getFieldOfClass(objeto.getClass(), nomeField);
		}
		ReflexaoUtil.insertValue(field, objeto, newValor);
	}

	private Object newInstance(Class classe) throws Exception {
		if (ReflexaoUtil.getType(classe) == ConstantesTiposDados.LIST) {
			return new ArrayList();
		} else if (ReflexaoUtil.getType(classe) == ConstantesTiposDados.SET) {
			return new HashSet();
		} else if (ReflexaoUtil.getType(classe) == ConstantesTiposDados.SORTEDSET) {
			return new TreeSet();
		} else {
			return classe.newInstance();
		}
	}

	private void tratarObject(Collection colecao, String nomeParametro,
			Object valor, String keyValue) throws Exception {
		String nomeClasse = nomeParametro.substring(0,
				nomeParametro.indexOf(delimitador));
		nomeParametro = nomeParametro.substring(nomeParametro
				.indexOf(delimitador) + 1);
		GroupTransformer group = getGroup(nomeClasse);

		Object result = transformer(colecao, group, keyValue);
		this.setAttributeVO(result, nomeParametro, valor,
				keyValue + group.getKeyValue());
	}
}