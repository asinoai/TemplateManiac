package aron.sinoai.templatemaniac.runtime;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.runtime.dal.DAOManager;
import aron.sinoai.templatemaniac.runtime.dal.hibernateimplementation.HibernateDAOManager;
import aron.sinoai.templatemaniac.runtime.MainPropertiesHelper.PropertyItem;
import aron.sinoai.templatemaniac.runtime.model.MainProperty;
import aron.sinoai.templatemaniac.configuration.ConfigurationManager;


public class RuntimeManager {

	static private final Logger LOGGER = Logger.getLogger(RuntimeManager.class);
	
	private DAOManager database = null;
	private final MainPropertiesHelper mainPropertiesUpdater;
	private ConfigurationManager configurationManager;

	public RuntimeManager() {
		
		File tempFile;
		try {
			tempFile = File.createTempFile("tdparser", ".db");
			tempFile.deleteOnExit();
			
			LOGGER.debug(String.format("The runtime database file is: \"%s\"", tempFile.getAbsolutePath()));
			
			database = new HibernateDAOManager(tempFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		database.getConnection().recreateSchema();
		
		mainPropertiesUpdater = new MainPropertiesHelper(database);

	}
	
	public DAOManager getDatabase() {
		return database;
	}
	
	public void setCommandLineArguments(String[] arguments) {
		mainPropertiesUpdater.setCommandLineArguments(arguments);
	}

	public String[] getCommandLineArguments() {
		return mainPropertiesUpdater.getCommandLineArguments();
	}

	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}
	
	public String getMainProperty(MainPropertiesHelper.PropertyItem item) {
		MainProperty mainProperty = database.getMainPropertyDAO().getById(item.getName(), false);
		return mainProperty == null ? null : mainProperty.getValue();
	}
	
	public String getCustom1() {
		return getMainProperty(PropertyItem.CUSTOM1);
	}

	public String getCustom2() {
		return getMainProperty(PropertyItem.CUSTOM2);
	}

	public String getCustom3() {
		return getMainProperty(PropertyItem.CUSTOM3);
	}

	public void update()
	{
		aron.sinoai.templatemaniac.configuration.dal.MainPropertyDAO mainPropertyDAO = 
			configurationManager.getDatabase().getMainPropertyDAO();

		List<aron.sinoai.templatemaniac.configuration.model.MainProperty> configurationMainProperties =
			mainPropertyDAO.getAll();
		
		mainPropertiesUpdater.setConfigurationMainProperties(configurationMainProperties);
		
		mainPropertiesUpdater.update();
	}


}
