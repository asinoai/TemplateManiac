package aron.sinoai.templatemaniac.scripting.common;

import java.util.HashMap;
import java.util.Map;


public abstract class ScriptExtensionFactoryBase<T> {
	private final Map<String, Class<? extends T>> mapping = new HashMap<String, Class<? extends T>>();
	private final ClassLoader loader;

	public ScriptExtensionFactoryBase()
	{
		this(ClassLoader.getSystemClassLoader());
	}
	
	public ScriptExtensionFactoryBase(ClassLoader loader){
		this.loader = loader;
	}
	
	abstract protected void createMapping();
	
	@SuppressWarnings("unchecked")
	public void registerClass(final String alias, final String className){
		Class<? extends T> clazz;
		try {
			clazz = (Class<? extends T>) loader.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		registerClass(alias, clazz);
	}

	public void registerClass(final String alias, final Class<? extends T> clazz) {
		mapping.put(alias, clazz);
	}
	
	@SuppressWarnings("unchecked")
	public T createInstance(String alias) {
		if (mapping.isEmpty()) {
			createMapping();
		}
		
		Object result = null;
		try {
			result = mapping.get(alias).newInstance();
		} catch (InstantiationException e) {
			new RuntimeException(e);
		} catch (IllegalAccessException e) {
			new RuntimeException(e);
		}
		
		return (T)(result);
	}

}
