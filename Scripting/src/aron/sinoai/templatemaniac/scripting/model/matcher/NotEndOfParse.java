package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "not-end-of-parse")
@XmlAccessorType(XmlAccessType.FIELD)
public class NotEndOfParse extends MatcherItemBase {
	public NotEndOfParse() {
		super();
	}
	
	@Override
	protected boolean isAMatchOverride() {
		Source source = getSource();
		return !source.isEOParse();
	}
}
