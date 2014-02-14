package aron.sinoai.templatemaniac.scripting.model;

import java.util.Collection;
import java.util.Map;

public interface GeneratorTarget {

	public GeneratorTarget getParentTarget();
	
	public void write(String key, String value);
	public void write(String key, Map<?,?> value);
	public void write(String key, Collection<?> value);
}
