package aron.sinoai.templatemaniac.scripting.model;


import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "include-script")
@XmlAccessorType(XmlAccessType.FIELD)
public class IncludeScript extends ScriptingItemBase implements GeneratorItem {

	@XmlAttribute
	private String fileName;

	@XmlTransient
	private ScriptContainer script;
	
	@XmlTransient
	private GeneratorTarget target;

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (parent != null && parent instanceof GeneratorItem) {
			this.target = ((GeneratorItem)parent).getTarget();
		}
		
		super.compile(context, parent);

		script = ScriptingItemBase.loadScript(fileName, context);

		Defaults defaults = context.getDefaults();
		final File currentScriptFile = defaults.getCurrentScriptFile();
		context.changeCurrentScriptFile(fileName);
		try {
			script.compile(context, this);
		} finally {
			defaults.setCurrentScriptFile(currentScriptFile);
		}
	}
	
	@Override
	public void execute() {
		script.execute();
	}

	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		if (script != null) {
			script.visitAllFromTopToDown(visitor);
		}
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public GeneratorTarget getTarget() {
		return target;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		target = null;
	}
	
}
