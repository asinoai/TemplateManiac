package aron.sinoai.templatemaniac.configuration.dal.hibernateimplementation;

import java.util.List;

import aron.sinoai.templatemaniac.dalutils.hibernateimplementation.HibernateDAOManagerBase;
import aron.sinoai.templatemaniac.dalutils.hibernateimplementation.HibernateDataAccessObject;
import aron.sinoai.templatemaniac.configuration.dal.SystemDAO;
import aron.sinoai.templatemaniac.configuration.model.System;


public class HibernateSystemDAO extends HibernateDataAccessObject<System, Long> implements SystemDAO{

	public HibernateSystemDAO(HibernateDAOManagerBase manager) {
		super(manager, System.class);
	}

	@Override
	public System getSingleElement() {
		System result;
		try {
			final List<System> list = getAll();
			result = (list.size() == 0) ? null : list.get(0);
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}
}

