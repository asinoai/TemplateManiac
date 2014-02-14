package aron.sinoai.templatemaniac.scripting.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

public abstract class DataGeneratorBase extends GeneratorTargetBase implements DataSource{

	@XmlAttribute
	private String keyPattern;
	
	@XmlAttribute
	private boolean clearOnExecution = false;

	@XmlAttribute
	private boolean inheritTarget = true;

	@XmlTransient
	private StringTemplate keyTemplate;

	@XmlTransient
	private String key;

	@XmlTransient
	private Map<String, DataSource> all;
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (keyPattern != null) {
			keyTemplate = createStringTemplate(keyPattern, context, parent);
		}

		all = context.getAll();
		
		context.registerToAll(this, getName());
		
		super.compile(context, parent);
	}

	@Override
	public void execute() {
		if (keyTemplate != null) {
			prepareStringTemplateForDataSource(keyTemplate, getParentDataSource());
			key = keyTemplate.toString();
		}

		if (clearOnExecution) {
			clearResult();
		}
		
		super.execute();

		doExecute();
		
	}

	protected abstract void doExecute();
	protected abstract void clearResult();

	@Override
	public GeneratorTarget getParentTarget() {
		return inheritTarget ? super.getParentTarget() : null;
	}
	
	public void setKeyPattern(String keyPattern) {
		this.keyPattern = keyPattern;
	}

	public String getKeyPattern() {
		return keyPattern;
	}

	public String getKey() {
		return key;
	}

	@Override
	public DataSource getDataSource() {
		return this;
	}

	@Override
	public DataSource getParentDataSource() {
		return super.getDataSource();
	}

	public void setAll(Map<String, DataSource> all) {
		this.all = all;
	}

	public Map<String, DataSource> getAll() {
		return all;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		all = null;
	}

	public void setClearOnExecution(boolean clearOnExecution) {
		this.clearOnExecution = clearOnExecution;
	}

	public boolean isClearOnExecution() {
		return clearOnExecution;
	}

	public void setInheritTarget(boolean inheritTarget) {
		this.inheritTarget = inheritTarget;
	}

	public boolean isInheritTarget() {
		return inheritTarget;
	}

}
