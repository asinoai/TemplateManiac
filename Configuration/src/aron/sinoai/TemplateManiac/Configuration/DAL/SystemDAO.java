package aron.sinoai.templatemaniac.configuration.dal;

import aron.sinoai.templatemaniac.dalutils.DataAccessObject;
import aron.sinoai.templatemaniac.configuration.model.System;


public interface SystemDAO extends DataAccessObject<System, Long>{
	System getSingleElement();
}

