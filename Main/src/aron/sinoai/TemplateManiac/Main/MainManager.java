package aron.sinoai.templatemaniac.main;

import aron.sinoai.templatemaniac.runtime.MainPropertiesHelper;
import aron.sinoai.templatemaniac.runtime.RuntimeManager;
import aron.sinoai.templatemaniac.runtime.MainPropertiesHelper.PropertyItem;
import aron.sinoai.templatemaniac.scripting.ScriptingManager;
import aron.sinoai.templatemaniac.configuration.ConfigurationManager;


@SuppressWarnings("unused")
public class MainManager {

	private static final MainManager instance;
	private final ConfigurationManager configurationManager;
	private final RuntimeManager runtimeManager;
	private final ScriptingManager scriptingManager;

	static {
		instance = new MainManager();
	}

	private MainManager() {
		configurationManager = new ConfigurationManager();
		
		runtimeManager = new RuntimeManager();
		runtimeManager.setConfigurationManager(configurationManager);
		
		scriptingManager = new ScriptingManager();
		scriptingManager.setRuntimeManager(runtimeManager);
		
	}

	public static MainManager get() {
		return instance;
	}
	
	public void setCommandLineArguments(String[] args) {
		runtimeManager.setCommandLineArguments(args);
	}

	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}


	public void startUp() {
		runtimeManager.update();
		scriptingManager.open(runtimeManager.getMainProperty(PropertyItem.SCRIPT_FILE));
	}

	public ScriptingManager getScriptingManager() {
		return scriptingManager;
	}
}
