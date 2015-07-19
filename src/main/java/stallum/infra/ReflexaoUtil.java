package stallum.infra;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class ReflexaoUtil {

	private ReflexaoUtil() {
	}

	public static String mountGetMethodName(String atributeName) {
		return "get" + atributeName.substring(0, 1).toUpperCase()
				+ atributeName.substring(1, atributeName.length());
	}

	public static String mountSetMethodName(String atributeName) {
		return "set" + atributeName.substring(0, 1).toUpperCase()
				+ atributeName.substring(1, atributeName.length());
	}

	@SuppressWarnings("rawtypes")
	public static Set getFieldsOfClass(Object object) {
		return getFieldsOfClass(object.getClass());
	}

	@SuppressWarnings("rawtypes")
	public static Set getFieldsOfClass(Class klass) {

		Set fields = new HashSet();
		fields.addAll(Arrays.asList(klass.getDeclaredFields()));

		if (klass.getSuperclass() != null) {
			fields.addAll(getFieldsOfClass(klass.getSuperclass()));
		}

		return fields;

	}

	public static Field getFieldOfClass(Object instanceOfClass, String fieldName)
			throws NoSuchFieldException {

		return getFieldOfClass(instanceOfClass.getClass(), fieldName);

	}

	@SuppressWarnings("rawtypes")
	public static Field getFieldOfClass(Class classOfClass, String fieldName)
			throws NoSuchFieldException {

		Field field = null;

		try {
			field = classOfClass.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			if (classOfClass.getSuperclass() != null) {
				// Se esse n�o tiver o campo mas tiver heran�a o pai pode
				// ter
				field = getFieldOfClass(classOfClass.getSuperclass(), fieldName);
			} else {
				throw new NoSuchFieldException(e.getMessage());
			}
		}

		return field;

	}

	public static Method getMethodAcessorOfClass(Object instanceOfClass,
			String methodName) throws NoSuchMethodException {

		return getMethodAcessorOfClass(instanceOfClass.getClass(), methodName);

	}

	@SuppressWarnings("rawtypes")
	public static Method getMethodAcessorOfClass(Class classOfClass,
			String methodName) throws NoSuchMethodException {

		Method method = null;
		try {
			method = classOfClass.getDeclaredMethod(methodName, (Class[]) null);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if (classOfClass.getSuperclass() != null) {
				// Se esse n�o tiver o method mas tiver heran�a o pai pode
				// ter
				method = getMethodAcessorOfClass(classOfClass.getSuperclass(),
						methodName);
			} else {
				throw new NoSuchMethodException(e.getMessage());
			}
		}

		return method;

	}

	@SuppressWarnings("rawtypes")
	public static Method getMethodAcessorOfClass(Object instanceOfClass,
			String methodName, Class[] attributes) throws NoSuchMethodException {

		return getMethodAcessorOfClass(instanceOfClass.getClass(), methodName,
				attributes);

	}

	@SuppressWarnings("rawtypes")
	public static Method getMethodAcessorOfClass(Class classOfClass,
			String methodName, Class[] attributes) throws NoSuchMethodException {

		Method method = null;

		try {
			method = classOfClass.getDeclaredMethod(methodName, attributes);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			if (classOfClass.getSuperclass() != null) {
				// Se esse n�o tiver o method mas tiver heran�a o pai pode
				// ter
				method = getMethodAcessorOfClass(classOfClass.getSuperclass(),
						methodName, attributes);
			} else {
				throw new NoSuchMethodException(e.getMessage());
			}
		}

		return method;

	}

	@SuppressWarnings("rawtypes")
	public static int getType(Class classe) throws Exception {
		if (classe == null)
			throw new Exception("getType: Attribute null");
		if (classe.toString().equals("int"))
			return ConstantesTiposDados.INT;
		if (classe.toString().equals("double"))
			return ConstantesTiposDados.DOUBLE;
		if (classe.toString().equals("float"))
			return ConstantesTiposDados.FLOAT;
		if (classe.toString().equals("long"))
			return ConstantesTiposDados.LONG;
		if (classe.toString().equals("class java.lang.String"))
			return ConstantesTiposDados.STRING;
		if (classe.toString().equals("class java.sql.Date"))
			return ConstantesTiposDados.DATE_SQL;
		if (classe.toString().equals("class java.util.Date"))
			return ConstantesTiposDados.DATE_UTIL;
		if (classe.toString().equals("boolean"))
			return ConstantesTiposDados.BOOLEAN;
		if (classe.toString().equals("class java.sql.Timestamp"))
			return ConstantesTiposDados.TIMESTAMP;
		if (classe.toString().endsWith("java.util.List"))
			return ConstantesTiposDados.LIST;
		if (classe.toString().endsWith("java.util.Collection"))
			return ConstantesTiposDados.LIST;
		if (classe.toString().equals("class java.sql.Time"))
			return ConstantesTiposDados.TIME;
		if (classe.toString().equals("class java.lang.Object"))
			return ConstantesTiposDados.OBJECT;
		if (classe.toString().endsWith("TipoBasico"))
			return ConstantesTiposDados.OBJECT;
		if (classe.toString().endsWith("class java.util.HashMap"))
			return ConstantesTiposDados.HASHMAP;
		if (classe.toString().endsWith("class java.lang.Long"))
			return ConstantesTiposDados.LONG;
		if (classe.toString().endsWith("class java.lang.Integer"))
			return ConstantesTiposDados.INT;
		if (classe.toString().endsWith("class java.lang.Double"))
			return ConstantesTiposDados.DOUBLE;
		if (classe.toString().endsWith("class java.lang.Boolean"))
			return ConstantesTiposDados.BOOLEAN;
		if (classe.toString().endsWith("java.util.Set"))
			return ConstantesTiposDados.SET;
		if (classe.toString().endsWith("java.util.SortedSet"))
			return ConstantesTiposDados.SORTEDSET;
		if (classe.toString().endsWith("java.util.TreeSet"))
			return ConstantesTiposDados.TREESET;

		// TIPO NAO DEFAULT
		return -1;
	}

	public static void insertValue(Field atributosFonte, Object objectNew,
			Object value) throws Exception {
		// Verificacao de erro
		if (objectNew == null)
			throw new Exception(
					"Objeto nulo passado para o m�todo insereValor ");
		if (atributosFonte == null)
			throw new Exception(
					"Atributo nulo passado para o m�todo insereValor (valor sendo inserido ="
							+ value + ")");
		atributosFonte.setAccessible(true);

		// MODIFICA��O FEITA PELO RODRIGO, VERIFICANDO SE O VALOR FOR NULO
		if (value == null) {
			atributosFonte.set(objectNew, null);
			return;
		}

		Object newValor = formatValue(atributosFonte.getType(), value);
		atributosFonte.set(objectNew, newValor);
	}

	@SuppressWarnings("rawtypes")
	public static Object formatValue(Class classe, Object value)
			throws Exception {
		int tipo = getType(classe);
		Object retorno = null;
		switch (tipo) {
		case ConstantesTiposDados.LONG:
			Long valorLong = null;
			try {
				valorLong = Long.valueOf(value.toString());
			} catch (Exception e) {
				valorLong = Long.valueOf(0L);
			}
			retorno = valorLong;
			break;
		case ConstantesTiposDados.DOUBLE:
			Double valorDouble = null;
			try {
				valorDouble = Double.valueOf(value.toString());
			} catch (Exception e) {
				valorDouble = Double.valueOf(0D);
			}
			retorno = valorDouble;
			break;
		case ConstantesTiposDados.DATE_UTIL:
			Date valorDate = null;
			try {
				if (value instanceof Date) {
					valorDate = (Date) value;
				} else {
					valorDate = formatDate(value.toString());
				}
			} catch (Exception e) {
				valorDate = null;
			}
			retorno = valorDate;
			break;
		case ConstantesTiposDados.BOOLEAN:
			String teste = value.toString().toLowerCase();
			Boolean valorBoolean = Boolean.valueOf(teste.equals("y")
					|| teste.equals("s") || teste.equals("yes")
					|| teste.equals("sim") || teste.equals("true")
					|| teste.equals("t") || teste.equals("x")
					|| teste.equals("1"));
			retorno = valorBoolean;
			break;
		case ConstantesTiposDados.INT:
			int aux = value.toString().lastIndexOf(".");
			if (aux > 0)
				value = value.toString().substring(0, aux);

			Integer valorInt = null;
			try {
				valorInt = Integer.valueOf(value.toString());
			} catch (Exception e) {
				valorInt = Integer.valueOf(0);
			}
			retorno = valorInt;
			break;
		case ConstantesTiposDados.STRING:
			retorno = value.toString();
			break;
		default:
			retorno = value;
		}
		return retorno;
	}

	public static Date formatDate(String valor) throws Exception {
		if ((valor == null) || (valor.equals(""))) {
			return null;
		}

		try {
			long v = Long.parseLong(valor);
			return new Date(v);
		} catch (Exception e) {
		}

		SimpleDateFormat sdfInput = null;
		java.util.Date data = null;

		if (valor.indexOf('-') == 2) {

			try {
				sdfInput = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd-MM-yyyy");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}
		}

		if (valor.indexOf('/') == 2) {

			try {
				sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("dd/MM/yyyy");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}
		}

		if (valor.indexOf('-') == 4) {
			try {
				sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy-MM-dd");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

		}

		if (valor.indexOf('/') == 4) {
			try {
				sdfInput = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.S");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy/MM/dd HH:mm");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

			try {
				sdfInput = new SimpleDateFormat("yyyy/MM/dd");
				data = sdfInput.parse(valor);
				return data;
			} catch (Exception e) {
			}

		}

		try {

			sdfInput = new SimpleDateFormat("yyyyMMdd");
			data = sdfInput.parse(valor);

			return data;
		} catch (Exception e) {
		}

		throw new Exception("Data inv�lida : " + valor);

	}

	@SuppressWarnings("rawtypes")
	public static void setAtributoVO(Object objeto, String nomeParametro,
			Object valor, String delimitador) throws Exception {
		Object newValor = valor;
		int pos = nomeParametro.indexOf(delimitador);
		String nomeField = nomeParametro;
		Field field = null;
		if (pos > 0) {
			nomeField = nomeParametro.substring(0, pos);
			nomeParametro = nomeParametro.substring(pos + 1);
			field = getFieldOfClass(objeto.getClass(), nomeField);
			field.setAccessible(true);
			Object newObject = field.get(objeto);
			if (newObject == null) {
				Class classe = field.getType();
				newObject = classe.newInstance();
			}
			if (!nomeParametro.equals("")) {
				setAtributoVO(newObject, nomeParametro, valor, delimitador);
				newValor = newObject;
			}
		} else {
			field = getFieldOfClass(objeto.getClass(), nomeField);
		}
		insertValue(field, objeto, newValor);
	}

	public static Object getValueOfField(Object reference, String atributeName)
			throws Exception {

		try {

			Field field = ReflexaoUtil.getFieldOfClass(reference, atributeName);
			field.setAccessible(true);
			return field.get(reference);

		} catch (SecurityException e) {
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			throw new Exception(e);
		}

	}

	public static void setValueOfField(Object reference, String atributeName,
			Object value) throws Exception {

		try {

			Field field = ReflexaoUtil.getFieldOfClass(reference, atributeName);
			field.setAccessible(true);
			field.set(reference, value);

		} catch (SecurityException e) {
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			throw new Exception(e);
		}

	}

	@SuppressWarnings("rawtypes")
	public static Object getStaticValueOfField(Class reference,
			String atributeName) throws Exception {

		try {

			Field field = ReflexaoUtil.getFieldOfClass(reference, atributeName);
			field.setAccessible(true);
			return field.get(reference);

		} catch (SecurityException e) {
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			throw new Exception(e);
		}

	}

	private class TipoPrimitivo {
	}

	@SuppressWarnings("rawtypes")
	public static Object getValueAtributo(Object objeto, String nomeParametro,
			Object valor, String delimitador) throws Exception {
		if (objeto instanceof TipoPrimitivo) {
			return tratarTipoPrimitivo((TipoPrimitivo) objeto, nomeParametro,
					valor, delimitador);
		} else if (objeto instanceof Map) {
			return tratarMap((Map) objeto, nomeParametro, valor, delimitador);
		} else if (objeto instanceof List) {
			return tratarList((List) objeto, nomeParametro, valor, delimitador);
		} else {
			return tratarVO(objeto, nomeParametro, valor, delimitador);
		}
	}

	@SuppressWarnings("rawtypes")
	private static Object tratarMap(Map objeto, String nomeParametro,
			Object valor, String delimitador) throws Exception {
		int pos = nomeParametro.indexOf(delimitador);
		if (pos > 0) {
			String nomeVariavel = nomeParametro.substring(0, pos);
			nomeParametro = nomeParametro.substring(pos + 1);

			Object obj = objeto.get(nomeVariavel);
			String nomeClasse = nomeParametro;
			pos = nomeParametro.indexOf(delimitador);
			if (pos > 0) {
				nomeClasse = nomeParametro.substring(0, pos);
				nomeParametro = nomeParametro.substring(pos + 1);
			} else {
				nomeParametro = "";
			}
			if (obj == null) {
				obj = newInstance(nomeClasse);
			}
			valor = getValueAtributo(obj, nomeParametro, valor, delimitador);

			objeto.put(nomeVariavel, valor);
		}
		return objeto;
	}

	@SuppressWarnings("rawtypes")
	private static Object tratarList(List objeto, String nomeParametro,
			Object valor, String delimitador) throws Exception {
		int pos = nomeParametro.indexOf(delimitador);
		if (pos > 0) {

			int posicao = Integer.valueOf(nomeParametro.substring(0, pos))
					.intValue();
			nomeParametro = nomeParametro.substring(pos + 1);

			int qtdLista = objeto.size();
			for (int i = qtdLista; i <= posicao; i++) {
				objeto.add(null);
			}

			Object obj = objeto.get(posicao);

			String nomeClasse = nomeParametro;
			pos = nomeClasse.indexOf(delimitador);
			if (pos > 0) {
				nomeClasse = nomeParametro.substring(0, pos);
				nomeParametro = nomeParametro.substring(pos + 1);
			} else {
				nomeParametro = "";
			}

			if (obj == null) {
				obj = newInstance(nomeClasse);
			}
			valor = getValueAtributo(obj, nomeParametro, valor, delimitador);

			objeto.set(posicao, valor);
		}
		return objeto;
	}

	@SuppressWarnings("rawtypes")
	private static Object tratarVO(Object objeto, String nomeParametro,
			Object valor, String delimitador) throws Exception {

		String nomeField = nomeParametro;
		int pos = nomeParametro.indexOf(delimitador);
		if (pos > 0) {
			nomeField = nomeParametro.substring(0, pos);
			nomeParametro = nomeParametro.substring(pos + 1);
		}
		Field field = getFieldOfClass(objeto.getClass(), nomeField);
		field.setAccessible(true);
		Object obj = field.get(objeto);
		if (obj == null || isTipoPrimitivo(field.getType().getName())) {
			Class classe = field.getType();
			obj = newInstance(classe.getName());
		}

		valor = getValueAtributo(obj, nomeParametro, valor, delimitador);
		insertValue(field, objeto, valor);
		return objeto;
	}

	private static Object tratarTipoPrimitivo(TipoPrimitivo objeto,
			String nomeParametro, Object valor, String delimitador)
			throws Exception {
		return URLDecoder.decode(valor.toString(), "ISO-8859-1");
	}

	@SuppressWarnings("rawtypes")
	private static HashMap tiposPrimitivos = new HashMap();

	static {
		tiposPrimitivos.put("String", String.class);
		tiposPrimitivos.put("Long", Long.class);
		tiposPrimitivos.put("long", Long.class);
		tiposPrimitivos.put("Double", Double.class);
		tiposPrimitivos.put("bouble", Double.class);
		tiposPrimitivos.put("Integer", Integer.class);
		tiposPrimitivos.put("int", Integer.class);
		tiposPrimitivos.put("Float", Float.class);
		tiposPrimitivos.put("float", Float.class);
		tiposPrimitivos.put("Date", Date.class);
		tiposPrimitivos.put("Boolean", Boolean.class);
		tiposPrimitivos.put("boolean", Boolean.class);
		tiposPrimitivos.put("Timestamp", Timestamp.class);
	}

	@SuppressWarnings("rawtypes")
	private static Object newInstance(String nomeClasse) throws Exception {
		if (nomeClasse.endsWith("List") || nomeClasse.endsWith("Collection")) {
			return new ArrayList();
		} else if (nomeClasse.endsWith("Map") || nomeClasse.endsWith("HashMap")) {
			return new HashMap();
		} else {

			Class c = getTipoPrimitivo(nomeClasse);
			if (c != null) {
				TipoPrimitivo tipo = new ReflexaoUtil().new TipoPrimitivo();
				return tipo;
			} else {
				if (nomeClasse.indexOf("|") > 0) {
					nomeClasse = nomeClasse.replaceAll("\\|", ".");
				}
				Class classe = Class.forName(nomeClasse);
				return classe.newInstance();
			}
		}
	}

	public static String getNomeClasse(String nome) {
		int p = nome.lastIndexOf(".");
		String temp = nome;
		if (p > 0) {
			temp = nome.substring(p + 1);
		}
		return temp;
	}

	@SuppressWarnings("rawtypes")
	public static Class getTipoPrimitivo(String nome) {
		return (Class) tiposPrimitivos.get(getNomeClasse(nome));
	}

	public static boolean isTipoPrimitivo(String nome) {
		return getTipoPrimitivo(nome) != null;
	}

}