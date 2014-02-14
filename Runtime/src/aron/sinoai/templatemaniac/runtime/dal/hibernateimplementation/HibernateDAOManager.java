package aron.sinoai.templatemaniac.runtime.dal.hibernateimplementation;

import java.util.Properties;

import aron.sinoai.templatemaniac.dalutils.hibernateimplementation.HibernateDAOManagerBase;
import aron.sinoai.templatemaniac.runtime.dal.DAOManager;
import aron.sinoai.templatemaniac.runtime.dal.MainPropertyDAO;


public class HibernateDAOManager extends HibernateDAOManagerBase implements DAOManager {

	private final HibernateMainPropertyDAO mainPropertyDAO;

	public HibernateDAOManager(String path) {
		super("runtime.hibernate.cfg.xml", getExtraProperties(path));
		
		mainPropertyDAO = new HibernateMainPropertyDAO(this);
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
}
