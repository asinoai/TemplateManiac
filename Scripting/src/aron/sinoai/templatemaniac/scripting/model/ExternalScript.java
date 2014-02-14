package aron.sinoai.templatemaniac.scripting.model;


import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "external-script")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExternalScript extends ScriptingItemBase {

	@XmlAttribute
	private String fileNamePattern;

	@XmlTransient
	ScriptingContext externalContext;	

	@XmlTransient
	private StringTemplate fileNameTemplate;
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		fileNameTemplate = new StringTemplate(context.formatFileNameForScripts(fileNamePattern));
		this.externalContext = context;

		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		ScriptingContext context = new ScriptingContext(externalContext);
		context.setDefaults(externalContext.getDefaults().clone());
		
		try {
			//copying the parent context, so we can access it
			context.getAll().putAll(externalContext.getAll());
				
			prepareStringTemplate(fileNameTemplate);
			final String fileName = fileNameTemplate.toString();

			Defaults defaults = context.getDefaults();
			final File currentScriptFile = defaults.getCurrentScriptFile();
			try {
				ScriptingItemBase.runScript(fileName, context);
			} finally {
				defaults.setCurrentScriptFile(currentScriptFile);
				externalContext.setErrorLevel(context.getErrorLevel());
			}
		} finally {
			context.close();
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		externalContext = null;
	}
}
