package aron.sinoai.templatemaniac.scripting.model.dbextension;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import aron.sinoai.templatemaniac.scripting.model.dbextension.ScriptDatabase;

public class DatabaseContext {
	private DatabaseFactory factory = new DatabaseFactory();
	private Map<String, ScriptDatabase> map = new HashMap<String, ScriptDatabase>();
	
	
	public DatabaseContext() {
	}
	
	public ScriptDatabase getDatabase(File folder, String kind) {
		ScriptDatabase result = map.get(kind);

		if (result == null) {
			result = factory.createInstance(kind);
			
			result.init(folder);
			map.put(kind, result);
		}
		
		return result;
	}
}
