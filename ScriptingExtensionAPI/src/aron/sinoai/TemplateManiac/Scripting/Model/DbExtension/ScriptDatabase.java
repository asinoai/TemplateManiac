package aron.sinoai.templatemaniac.scripting.model.dbextension;

import java.io.File;

import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;

public interface ScriptDatabase {
	void init(File baseFolder);
	DAOManagerBase getDAOManager();
}
