package aron.sinoai.templatemaniac.configuration.dal.hibernateimplementation;

import java.util.Properties;

import aron.sinoai.templatemaniac.dalutils.hibernateimplementation.HibernateDAOManagerBase;
import aron.sinoai.templatemaniac.configuration.dal.DAOManager;
import aron.sinoai.templatemaniac.configuration.dal.MainPropertyDAO;
import aron.sinoai.templatemaniac.configuration.dal.SystemDAO;



public class HibernateDAOManager extends HibernateDAOManagerBase implements
		DAOManager {
	
	private final HibernateMainPropertyDAO mainPropertyDAO;
	private final SystemDAO systemDAO;

	public HibernateDAOManager(String path) {
		super("configuration.hibernate.cfg.xml", getExtraProperties(path));

		mainPropertyDAO = new HibernateMainPropertyDAO(this);
		systemDAO = new HibernateSystemDAO(this);
	}
	
	protected static Properties getExtraProperties(String path) {
		Properties result = new Properties();
		result.setProperty("hibernate.connection.url", "jdbc:sqlite:" + path);
		
		return result;
	}

	@Override
	public MainPropertyDAO getMainPropertyDAO() {
		return mainPropertyDAO;
	}

	@Override
	public SystemDAO getSystemDAO() {
		return systemDAO;
	}	
}
