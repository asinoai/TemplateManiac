package aron.sinoai.templatemaniac.configuration;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import aron.sinoai.templatemaniac.configuration.dal.DAOManager;
import aron.sinoai.templatemaniac.configuration.dal.MainPropertyDAO;
import aron.sinoai.templatemaniac.configuration.dal.hibernateimplementation.HibernateDAOManager;
import aron.sinoai.templatemaniac.configuration.model.Configuration;
import aron.sinoai.templatemaniac.configuration.model.MainProperty;


public class ConfigurationManager {

	private DAOManager database = null;
	private Unmarshaller jaxbUnmarshaller = null;
	//private Marshaller jaxbMarshaller = null;
	private static final String SETTINGS_FILE = "configuration.xml";

	public ConfigurationManager() {
		database = new HibernateDAOManager(SETTINGS_FILE + ".db");
		
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("aron.sinoai.templatemaniac.configuration.model");
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			//jaxbMarshaller = jaxbContext.createMarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		update();
	}
	
	private void refreshMainProperties(List<MainProperty> mainProperties) {
		if (mainProperties != null) {
			final MainPropertyDAO mainPropertyDAO = database.getMainPropertyDAO();
			
			for (MainProperty mainProperty : mainProperties) {
				mainPropertyDAO.save(mainProperty);
			}
		}
	}
	
	
	public void update()
	{
		database.getConnection().recreateSchema();
			
		try {
			Configuration data = (Configuration) jaxbUnmarshaller.unmarshal(new File(SETTINGS_FILE));
			refreshMainProperties(data.getMainProperties().getItems());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		database.flush();
	}
	
	public DAOManager getDatabase() {
		return database;
	}

	
}
