package aron.sinoai.templatemaniac.scripting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.transform.ResultWrapper;
import aron.sinoai.templatemaniac.scripting.model.transform.ResultWrapperFactory.WrapperKind;

@XmlRootElement(name = "result-transformer")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResultTansformer extends DataSourceBase {

	@XmlTransient
	private ResultWrapper result;

	@XmlAttribute
	private String kind;
	
	public ResultTansformer() {
	}
	
	@Override
	public Object getResult() {
		return result;
	}

	public void setKind(final String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return kind;
	}

	@Override 
	public void compile(ScriptingContext context, ScriptingItem parent) {
		result = context.getWrapperFactory().createInstance(kind);
		
		super.compile(context, parent);
	}

	@Override 
	public void execute() {
		result.setInner(getParentDataSource().getResult());
		
		super.execute();
	}
	
}
