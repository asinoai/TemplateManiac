package aron.sinoai.templatemaniac.scripting.model.dbextension;

import aron.sinoai.templatemaniac.scripting.common.ScriptExtensionFactoryBase;
import aron.sinoai.templatemaniac.scripting.model.dbextension.ScriptDatabase;
import aron.sinoai.templatemaniac.scripting.model.dbextension.mock.CustomDAODatabase;


public class DatabaseFactory extends ScriptExtensionFactoryBase<ScriptDatabase> {
	public static enum DatabaseKind {
		CustomDAO
	}

	public DatabaseFactory(){
		super();
	}

	@Override
	protected void createMapping() {
		registerClass("CustomDAO", CustomDAODatabase.class);
	}
	

}
