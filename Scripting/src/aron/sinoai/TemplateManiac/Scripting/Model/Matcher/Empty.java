package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "empty")
@XmlAccessorType(XmlAccessType.FIELD)
public class Empty extends MatcherItemBase {
	public Empty() {
		super();
	}
	
	@Override
	protected boolean isAMatchOverride() {
		return true;
	}
}
