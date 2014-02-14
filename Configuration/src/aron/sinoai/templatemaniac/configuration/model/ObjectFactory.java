package aron.sinoai.templatemaniac.configuration.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry 
public class ObjectFactory {
	
	public MainProperty createMainProperty() {
		return new MainProperty();
	}
	
	public Configuration createConfiguration() {
		return new Configuration();
	}
	

}
