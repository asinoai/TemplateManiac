package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "optional-matching")
@XmlAccessorType(XmlAccessType.FIELD)
public class OptionalMatching extends MatcherContainmentBase {
	public OptionalMatching() {
		super();
	}
	
	protected boolean isAMatchOverride() {
		//if start() > 0, we already had an iteration, thus we don't do another one
		if (start() == 0) {
			getMatchPiece().isAMatch();
		}
		
		return true;
	}
}
