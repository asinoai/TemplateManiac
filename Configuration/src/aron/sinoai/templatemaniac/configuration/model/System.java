package aron.sinoai.templatemaniac.configuration.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "system")
public class System implements Serializable {

	private static final long serialVersionUID = -7513482672703985731L;
	@Id
	private long version;
    
    public System() {
    }
    
    public System(long version) {
    	this.version = version;
    }
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
}

