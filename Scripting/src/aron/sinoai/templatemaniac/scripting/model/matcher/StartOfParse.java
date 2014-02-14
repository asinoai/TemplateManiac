package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "start-of-parse")
@XmlAccessorType(XmlAccessType.FIELD)
public class StartOfParse extends MatcherItemBase {
	public StartOfParse() {
		super();
	}
	
	@Override
	protected boolean isAMatchOverride() {
		Source source = getSource();
		
		return (source.getPos() == 0);
	}
}
