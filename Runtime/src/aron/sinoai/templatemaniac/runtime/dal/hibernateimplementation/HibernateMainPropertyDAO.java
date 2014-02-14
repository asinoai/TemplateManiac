package aron.sinoai.templatemaniac.runtime.dal.hibernateimplementation;

import aron.sinoai.templatemaniac.dalutils.hibernateimplementation.HibernateDAOManagerBase;
import aron.sinoai.templatemaniac.dalutils.hibernateimplementation.HibernateDataAccessObject;
import aron.sinoai.templatemaniac.runtime.dal.MainPropertyDAO;
import aron.sinoai.templatemaniac.runtime.model.MainProperty;


public class HibernateMainPropertyDAO extends HibernateDataAccessObject<MainProperty, String> implements MainPropertyDAO{

	public HibernateMainPropertyDAO(HibernateDAOManagerBase manager) {
		super(manager, MainProperty.class);
	}
}

