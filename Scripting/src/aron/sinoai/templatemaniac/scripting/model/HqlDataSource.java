package aron.sinoai.templatemaniac.scripting.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;

@XmlRootElement(name = "hql-data-source")
@XmlAccessorType(XmlAccessType.FIELD)
public class HqlDataSource extends QueryDataSource {

	public HqlDataSource() {
	}

	protected List<Object> query(DAOManagerBase manager, String queryText) {
		List<Object> list = manager.list(queryText);
		return list;
	}


}
