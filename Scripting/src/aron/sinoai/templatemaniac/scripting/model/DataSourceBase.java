package aron.sinoai.templatemaniac.scripting.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

public abstract class DataSourceBase extends ScriptContainerBase implements DataSource, GeneratorItem {
	@XmlTransient
	private GeneratorTarget target;

	@XmlTransient
	private Map<String, DataSource> all;
	
	@Override
	public DataSource getDataSource() {
		return this;
	}

	@Override
	public DataSource getParentDataSource() {
		return super.getDataSource();
	}
	
	@Override 
	public void compile(ScriptingContext context, ScriptingItem parent) {
		all = context.getAll();
		
		context.registerToAll(this, getName());
		
		if (parent != null && parent instanceof GeneratorItem) {
			this.target = ((GeneratorItem)parent).getTarget();
		}

		super.compile(context, parent);
	}

	@Override
	public GeneratorTarget getTarget() {
		return target;
	}

	public Map<String, DataSource> getAll() {
		return all;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		all = null;
		target = null;
	}
	
}
