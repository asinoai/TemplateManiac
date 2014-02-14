package aron.sinoai.templatemaniac.runtime;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.runtime.dal.DAOManager;
import aron.sinoai.templatemaniac.runtime.dal.MainPropertyDAO;
import aron.sinoai.templatemaniac.runtime.model.MainProperty;


public class MainPropertiesHelper {

	private String[] commandLineArguments = null;
	private List<aron.sinoai.templatemaniac.configuration.model.MainProperty> configurationMainProperties = null;
	private DAOManager runtimeDatabase = null;
	
	static private final HashMap<String, PropertyItem> PROPERTY_ITEMS_LOOK_UP = buildItemsLookUp();
	static private final Logger LOGGER = Logger.getLogger(MainPropertiesHelper.class);
	
	static public enum PropertyItem {
		TD_FILE("tdfile", null, "the td file"),
		TD_FORCE_REFRESH("tdForceRefresh", null, "when set to 1 will force reparsing of the td file."),
		TD_VACUUM("tdVacuum", null, "when set to 1 will vacuum the td db file."),
		SCRIPT_FILE("scriptFile", null, "the script"),
		RULE_PARSER("ruleparser", null, "the rule parser"),
		MATCHER_FILE("matcherfile", null, "the matcher test"),
		PLUGIN_MODE("pluginMode", "false", "main is executed as RG plugin"),
		PLUGIN_TEST_CONNECTION_DRIVER("pluginTestConnectionDriver", null, "the plugin test connection driver"),
		PLUGIN_TEST_CONNECTION_URL("pluginTestConnectionUrl", null, "the plugin test connection url"),
		PLUGIN_TEST_CONNECTION_USER("pluginTestConnectionUser", null, "the plugin test connection user"),
		PLUGIN_TEST_CONNECTION_PASSWORD("pluginTestConnectionPassword", null, "the plugin test connection password"),
		PLUGIN_LDM_PHYSYCAL_LOT_ID("pluginLdmPhysicalLotId", null, "the physical lot id of ldm data"),
		PLUGIN_LDM_STATUS("pluginLdmStatus", null, "the status property of ldm data"),
		PLUGIN_LDM_DIVISA("pluginLdmDivisa", null, "the divisa property of ldm data"),
		CUSTOM1("custom1", null,"custom parameter, to be used as needed in scripts"),
		CUSTOM2("custom2", null,"custom parameter, to be used as needed in scripts"),
		CUSTOM3("custom3", null,"custom parameter, to be used as needed in scripts");
		
		private final String name;
		private final String defaultValue;
		private final String description;
		
		PropertyItem(String name, String defaultValue, String description) {
			this.name = name;
			this.defaultValue = defaultValue;
			this.description = description;
		}
		
		public String getName() {
			return name;
		}
		
		public String getDescription() {
			return description;
		}

		public String getDefaultValue() {
			return defaultValue;
		}
		
	}
	
	private static HashMap<String, PropertyItem> buildItemsLookUp(){
		HashMap<String, PropertyItem> result = new HashMap<String, PropertyItem>();
		
		for(PropertyItem item : PropertyItem.values()) {
			result.put(item.getName().toUpperCase(), item);//putting in upper-case, since the property names are not case sensitive
		}
		
		return result;
	}
	
	protected static PropertyItem toProperyItem(String name)
	{
		return PROPERTY_ITEMS_LOOK_UP.get(name.toUpperCase());
	}
	
	public MainPropertiesHelper(DAOManager database) {
		this.runtimeDatabase = database;
	}

	public void setCommandLineArguments(String[] arguments) {
		this.commandLineArguments = arguments;
	}
	
	public String[] getCommandLineArguments() {
		return this.commandLineArguments;
	}
	
	public void setConfigurationMainProperties(List<aron.sinoai.templatemaniac.configuration.model.MainProperty> mainProperties) {
		this.configurationMainProperties = mainProperties;
	}

	public void update() {
		updateCommandLines();
		updateConfigurationMainProperties();
		updateDefaultValues();
		
		runtimeDatabase.flush();
	}

	private void updateCommandLines() {
		int argumentLength = commandLineArguments.length;
		if (argumentLength % 2 == 0) {
			MainPropertyDAO mainPropertyDAO = runtimeDatabase.getMainPropertyDAO();

			int propertiesLength = argumentLength / 2;
			for(int i = 0; i < propertiesLength; i++) {
				String name = commandLineArguments[2 * i];
				String value = commandLineArguments[2 * i + 1];
				
				if (name.startsWith("-")) {
					name = name.substring(1);
					PropertyItem property = toProperyItem(name);
					if (property != null) {
						MainProperty mainProperty = new MainProperty(property.getName(), value);
						mainPropertyDAO.save(mainProperty);
					} else {
						LOGGER.error(String.format("Not supported parameter: %s", new Object[]{name}));
					}
					
				} else {
					LOGGER.error("The command-line parameters should have the form: -<param-name1> <value1> -<param-name2> <value2>...");
				}
			}
		} else {
			LOGGER.error("The command-line parameters should have the form: -<param-name1> <value1> -<param-name2> <value2>...");
		}
	}

	private void updateConfigurationMainProperties() {
		MainPropertyDAO mainPropertyDAO = runtimeDatabase.getMainPropertyDAO();

		for(aron.sinoai.templatemaniac.configuration.model.MainProperty property : configurationMainProperties) {
			PropertyItem propertyItem = toProperyItem(property.getName());
			if (propertyItem != null) {
				MainProperty mainProperty = mainPropertyDAO.getById(property.getName(), false);
				
				if (mainProperty == null) {
					LOGGER.debug(String.format("Updating from configuration: '%s'", propertyItem.getName()));
					mainProperty = new MainProperty(propertyItem.getName(), property.getValue());
					mainPropertyDAO.save(mainProperty);
				}
			}
		}
	}
	
	private void updateDefaultValues() {
		MainPropertyDAO mainPropertyDAO = runtimeDatabase.getMainPropertyDAO();

		for(PropertyItem property : PropertyItem.values()) {
			
			if (mainPropertyDAO.getById(property.getName(), false) == null && property.getDefaultValue() != null) {
				MainProperty mainProperty = new MainProperty(property.getName(), property.getDefaultValue());
				mainPropertyDAO.save(mainProperty);
			}
		}
	}

	public static boolean String2Boolean(final String value) {
		return "1".equals(value) || "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
	}
	
}
