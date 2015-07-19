package stallum.infra;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class GroupTransformer {
	private Map values;
	private Class type;
	private String [] key;
	private String keyValue;
	
	private String [] parentKey;
	private String parentKeyValue;
	private String pathParentKey; 
	private Object object;
	private String path;
	
	public GroupTransformer(Class type, String[] key, String path){
		values = new HashMap();
		this.type = type;
		this.path = path;
		if(key != null)
			this.key = (String[])key.clone();
	}

	public Class getType() {
		return type;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public Map getValues() {
		return values;
	}

	public void setValues(Map values) {
		this.values = values;
	}

	public String[] getKey() {
		if(key != null)
			return (String[])key.clone();
		return null;
	}

	public void setKey(String[] key) {
		if(key != null)
			this.key = (String[])key.clone();
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String[] getParentKey() {
		if(parentKey != null)
			return (String[])parentKey.clone();
		return null;
	}

	public void setParentKey(String[] parentKey) {
		if(parentKey != null)
			this.parentKey = (String[])parentKey.clone();
	}

	public String getParentKeyValue() {
		return parentKeyValue;
	}

	public void setParentKeyValue(String parentKeyValue) {
		this.parentKeyValue = parentKeyValue;
	}

	public String getPathParentKey() {
		return pathParentKey;
	}

	public void setPathParentKey(String pathParentKey) {
		this.pathParentKey = pathParentKey;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}	
