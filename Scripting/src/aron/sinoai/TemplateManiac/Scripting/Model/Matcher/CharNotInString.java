package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "char-not-in-string")
@XmlAccessorType(XmlAccessType.FIELD)
public class CharNotInString extends MatcherItemWithStringBase {
	public CharNotInString() {
		super();
	}
	
	protected boolean isAMatchOverride() {
		Source source = getSource();
		boolean isMatch = !source.isEOParse() && ( getTheString().indexOf(source.getParseChar()) == -1 );
		if (isMatch) {
			source.incPos();
		}
		return isMatch;
	}
}
