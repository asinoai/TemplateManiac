package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "until-the-string")
@XmlAccessorType(XmlAccessType.FIELD)
public class UntilTheString extends MatcherItemWithStringBase {
	public UntilTheString() {
		super();
	}
	
	protected boolean isAMatchOverride() {
		Source source = getSource();
		String theString = getTheString();
		
		int index = source.getTheString().indexOf(theString, source.getPos());

		boolean result = (index != -1); 
		if (result) {
			source.setPos(index);
		}
		
		return result;
	}
}
