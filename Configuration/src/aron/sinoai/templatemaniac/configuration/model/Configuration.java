package aron.sinoai.templatemaniac.configuration.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Configuration implements Serializable {

	private static final long serialVersionUID = 5887230459400187161L;

	private MainProperties mainProperties;
	
	public MainProperties getMainProperties() {
		return mainProperties;
	}
	
	public void setMainProperty(MainProperties mainProperties) {
		this.mainProperties = mainProperties;
	}

	
}
