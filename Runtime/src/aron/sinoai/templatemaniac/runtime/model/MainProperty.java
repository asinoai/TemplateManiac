package aron.sinoai.templatemaniac.runtime.model;
        

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "main_properties")
public class MainProperty implements Serializable {
	         
	private static final long serialVersionUID = 5651317189885391620L;

	@Id
	private String name;
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

