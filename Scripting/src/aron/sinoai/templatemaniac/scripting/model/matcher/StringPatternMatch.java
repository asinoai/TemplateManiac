package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "string-pattern-match")
@XmlAccessorType(XmlAccessType.FIELD)
public class StringPatternMatch extends MatcherItemWithStringPatternBase {
	public StringPatternMatch() {
		super();
	}
	
	protected boolean isAMatchOverride() {
		Source source = getSource();
		String theString = getTheString();
		
		boolean isMatch = source.isAValidPosition(source.getPos() + theString.length() - 1)
			&& (theString.equals(source.getParseString(theString.length())));
		if (isMatch) {
			source.incPos(theString.length());
		}
		
		return isMatch;
	}


}
