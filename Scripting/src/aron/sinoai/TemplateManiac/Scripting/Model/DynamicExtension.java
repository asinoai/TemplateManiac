package aron.sinoai.templatemaniac.scripting.model;


import java.io.File;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import bsh.Interpreter;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "dynamic-extension")
@XmlAccessorType(XmlAccessType.FIELD)
public class DynamicExtension extends ScriptingItemBase {

	@XmlAttribute
	private String expressionPattern;

	@XmlTransient
	private StringTemplate expressionTemplate;
	
	@XmlTransient
	private Interpreter interpreter;

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		expressionTemplate = createStringTemplate(expressionPattern, context, parent);
		this.interpreter = fetchBashInterpreter(context);
		
		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		prepareStringTemplate(expressionTemplate);
		
		final String expression = expressionTemplate.toString();
		executeBashScript(expression, interpreter);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
