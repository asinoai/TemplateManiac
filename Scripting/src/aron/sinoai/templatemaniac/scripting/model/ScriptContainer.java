package aron.sinoai.templatemaniac.scripting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "script-container")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class ScriptContainer extends ScriptContainerBase implements GeneratorItem {
	
	@XmlAttribute
	private String database;

	@XmlAttribute
	private String bscFile;
	
	
	@XmlAttribute
	private boolean inheritTarget = true;

	@XmlTransient
	private GeneratorTarget target;

	
	
	public ScriptContainer() {
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (inheritTarget && parent != null && parent instanceof GeneratorItem) {
			this.target = ((GeneratorItem)parent).getTarget();
		}

		Defaults oldDefaults = context.getDefaults();
		try {
			//we override the database and groupFile
			Defaults newDefaults = new Defaults(oldDefaults);
			newDefaults.setDatabase(database);
			newDefaults.setGroupFile(getGroupFile());
			newDefaults.setBscFile(bscFile);
			context.setDefaults(newDefaults);
			
			super.compile(context, parent);
		} finally {
			context.setDefaults(oldDefaults);
		}
	}
	
	public void setDefaultDatabase(final String defaultDatabase) {
		this.database = defaultDatabase;
	}

	public String getDefaultDatabase() {
		return database;
	}

	public GeneratorTarget getTarget() {
		return target;
	}

	public void setInheritTarget(boolean inheritTarget) {
		this.inheritTarget = inheritTarget;
	}

	public boolean isInheritTarget() {
		return inheritTarget;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		target = null;
	}
}
