package aron.sinoai.templatemaniac.scripting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "context-data-source")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextDataSource extends DataSourceBase {

	@XmlTransient
	private ScriptingContext result;

	
	public ContextDataSource() {
	}
	
	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		this.result = context;
		
		super.compile(context, parent);
	}
	

	@Override
	public void dispose() {
		super.dispose();
		
		result = null;
	}
}
