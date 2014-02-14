package aron.sinoai.templatemaniac.scripting.model;

import java.util.Map;

public interface DataSource {
	DataSource getParentDataSource();
	
	Object getResult();
	
	public Map<String, DataSource> getAll();
	
}
