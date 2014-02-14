package aron.sinoai.templatemaniac.scripting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import aron.sinoai.templatemaniac.scripting.model.matcher.DynamicMatcher.EvaluateKind;

@SuppressWarnings("unused")
@XmlRootElement(name = "data-source-alias")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSourceAlias extends DataSourceBase {

	@XmlAttribute
	private String dsNamePattern;

	@XmlTransient
	private ScriptingContext context;

	@XmlTransient
	private StringTemplate templateDsNamePattern;
	
	@XmlTransient
	//the referenced data-source
	private DataSource refDs; 

	
	public DataSourceAlias() {
	}
	
	@Override
	public Object getResult() {
		return refDs.getResult();
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		templateDsNamePattern = createStringTemplate(dsNamePattern, context, parent);
		this.context = context;
		
		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		prepareStringTemplateForDataSource(templateDsNamePattern, getParentDataSource());
		String referencedDsName = templateDsNamePattern.toString();
		
		if (referencedDsName == null || referencedDsName.isEmpty()) {
			refDs = getParentDataSource();
		} else {
			refDs = context.getAll().get(referencedDsName);
		}
		
		//we need to re-register each time
		context.registerToAll(this, getName()); 

		super.execute();
	}
	

	@Override
	public void dispose() {
		super.dispose();
		
		context = null;
	}

	public void setDsNamePattern(String dsNamePattern) {
		this.dsNamePattern = dsNamePattern;
	}

	public String getDsNamePattern() {
		return dsNamePattern;
	}

	public void setRefDs(DataSource refDs) {
		this.refDs = refDs;
	}

	public DataSource getRefDs() {
		return refDs;
	}
}
