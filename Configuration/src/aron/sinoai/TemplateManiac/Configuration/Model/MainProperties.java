package aron.sinoai.templatemaniac.configuration.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class MainProperties implements Serializable {

	private static final long serialVersionUID = -2962445570881609926L;

	@XmlElement(name = "mainProperty")
	private ArrayList<MainProperty> items;
	
	public List<MainProperty> getItems() {
		return items;
	}
}
