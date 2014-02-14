package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "one-or-many-multi")
@XmlAccessorType(XmlAccessType.FIELD)
public class OneOrManyMulti extends MatcherContainmentBase {
	public OneOrManyMulti() {
		super();
	}
	
	protected boolean isAMatchOverride() {
		//start > 0 means that we continue from at least one iteration; thus isMatch, from the beginning
		boolean isMatch = (start() > 0) ? true : getMatchPiece().isAMatch();
		while (getMatchPiece().isAMatch());
		
		return isMatch;
	}
}
