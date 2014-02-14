package aron.sinoai.templatemaniac.scripting.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import aron.sinoai.templatemaniac.scripting.util.FormatHelper;
import aron.sinoai.templatemaniac.scripting.util.FormatHelper.FormatterKind;

@XmlRootElement(name = "formatter")
@XmlAccessorType(XmlAccessType.FIELD)
public class Formatter extends DataSourceBase {

	@XmlAttribute
	private String expressionPattern;
	
	@XmlAttribute
	private FormatterKind kind;
	
	@XmlTransient
	private String result;
	
	@XmlTransient
	private StringTemplate expressionTemplate;

	public Formatter() {
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		expressionTemplate = createStringTemplate(expressionPattern, context, parent);

		super.compile(context, parent);
	}

	@Override
	public void execute() {
		prepareStringTemplate(expressionTemplate);
		
		result = expressionTemplate.toString();
		
		if (kind != null) {
			result = FormatHelper.format(kind, result);
		}
		
		super.execute();
	}

	@Override
	public Object getResult() {
		return result;
	}

	public void setProperyNamePattern(String properyNamePattern) {
		this.expressionPattern = properyNamePattern;
	}

	public String getProperyNamePattern() {
		return expressionPattern;
	}

	public void setKind(FormatterKind kind) {
		this.kind = kind;
	}

	public FormatterKind getKind() {
		return kind;
	}

}
