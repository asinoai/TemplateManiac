package aron.sinoai.templatemaniac.scripting.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import aron.sinoai.templatemaniac.scripting.util.FormatHelper;
import aron.sinoai.templatemaniac.scripting.util.FormatHelper.FormatterKind;

@XmlRootElement(name = "dynamic-content")
@XmlAccessorType(XmlAccessType.FIELD)
public class DynamicContent extends GeneratorItemBase {

	@XmlAttribute
	private String keyPattern;
	
	@XmlElement
	private ArrayList<String> pattern;

	@XmlAttribute
	private FormatterKind formatterKind = null;	
	
	@XmlAttribute
	private boolean noContentIfEmpty = false;	

	@XmlTransient
	private StringTemplate template;

	@XmlTransient
	private StringTemplate keyTemplate;

	public DynamicContent() {
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		template = createStringTemplate(linesToString(pattern), context, parent);

		if (keyPattern != null) {
			keyTemplate = createStringTemplate(keyPattern, context, parent);
		}

		super.compile(context, parent);

		if (getTarget() == null ) {
			throw new RuntimeException(String.format("DynamicContent (%s): Error, no target!", getName()));
		}
	}

	@Override
	public void execute() {
		prepareStringTemplate(template);
		
		String key = null;
		if (keyTemplate != null) {
			prepareStringTemplate(keyTemplate);
			key = keyTemplate.toString();
		}
		
		String value = template.toString();
		
		if (formatterKind != null) {
			value = FormatHelper.format(formatterKind, value);
		}
		
		if (!noContentIfEmpty || (value != null && !value.isEmpty()) ) {
			getTarget().write(key, value);
		}
	}

	public void setKeyTemplate(StringTemplate keyTemplate) {
		this.keyTemplate = keyTemplate;
	}

	public StringTemplate getKeyTemplate() {
		return keyTemplate;
	}

	public void setFormatterKind(FormatterKind formatterKind) {
		this.formatterKind = formatterKind;
	}

	public FormatterKind getFormatterKind() {
		return formatterKind;
	}

	public void setNoContentIfEmpty(boolean noContentIfEmpty) {
		this.noContentIfEmpty = noContentIfEmpty;
	}

	public boolean isNoContentIfEmpty() {
		return noContentIfEmpty;
	}
}
