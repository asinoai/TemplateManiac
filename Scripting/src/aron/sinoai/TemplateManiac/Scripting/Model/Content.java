package aron.sinoai.templatemaniac.scripting.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import aron.sinoai.templatemaniac.scripting.util.FormatHelper;
import aron.sinoai.templatemaniac.scripting.util.FormatHelper.FormatterKind;

@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content extends GeneratorItemBase {

	@XmlAttribute
	private String key;
	
	@XmlElement
	private ArrayList<String> text;

	@XmlAttribute
	private FormatterKind formatterKind = null;	
	
	@XmlAttribute
	private boolean noContentIfEmpty = false;	

	public Content() {
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);

		if (getTarget() == null ) {
			throw new RuntimeException(String.format("Content (%s): Error, no target!", getName()));
		}
	}

	@Override
	public void execute() {
		String value = linesToString4Windows(text);
		
		if (formatterKind != null) {
			value = FormatHelper.format(formatterKind, value);
		}
		
		if (!noContentIfEmpty || (value != null && !value.isEmpty()) ) {
			getTarget().write(key, value);
		}
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
