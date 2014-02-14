package aron.sinoai.templatemaniac.configuration.dal;

import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;

public interface DAOManager extends DAOManagerBase {

	MainPropertyDAO getMainPropertyDAO();
	SystemDAO getSystemDAO();
}
