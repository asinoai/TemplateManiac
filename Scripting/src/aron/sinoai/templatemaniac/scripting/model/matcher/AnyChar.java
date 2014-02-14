package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "any-char")
public class AnyChar extends MatcherItemBase {
	
	public AnyChar() {
		super();
	}
	
	@Override
	protected boolean isAMatchOverride() {
		Source source = getSource();
		
		boolean success = !source.isEOParse();
		if (success) {
			source.incPos();
		}
		
		return success;
	}
}
