package aron.sinoai.templatemaniac.configuration.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "main_properties")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class MainProperty implements Serializable {

	private static final long serialVersionUID = -8785080335559588727L;

	@Id
    @XmlAttribute
	private String name;
    @XmlAttribute
	private String value;
    
    public MainProperty() {
    }
    
    public MainProperty(String name, String value) {
    	this.name = name;
    	this.value = value;
    }
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
}

