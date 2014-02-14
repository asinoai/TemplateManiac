package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "end-of-parse")
@XmlAccessorType(XmlAccessType.FIELD)
public class EndOfParse extends MatcherItemBase {
	public EndOfParse() {
		super();
	}
	
	@Override
	protected boolean isAMatchOverride() {
		Source source = getSource();
		return source.isEOParse();
	}
}
