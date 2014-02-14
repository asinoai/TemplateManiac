package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "null-or-many-multi")
@XmlAccessorType(XmlAccessType.FIELD)
public class NullOrManyMulti extends MatcherContainmentBase {
	public NullOrManyMulti() {
		super();
	}
	
	@Override
	protected boolean isAMatchOverride() {
		while (getMatchPiece().isAMatch());
		return true;
	}
}
