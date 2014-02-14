package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fix-nr-of-times")
@XmlAccessorType(XmlAccessType.FIELD)
public class FixNrOfTimes extends MatcherContainmentBase {
	
	@XmlAttribute
	private int nrOfTimes;
	
	protected boolean isAMatchOverride() {
		boolean isMatch = true;
		for(int i = start(); i < nrOfTimes; i++) {
			isMatch = getMatchPiece().isAMatch();
			if (!isMatch) {
				break;
			}
		}
		return isMatch;
	}

	public FixNrOfTimes() {
		super();
	}
	
	public void setNrOfTimes(int nrOfTimes) {
		this.nrOfTimes = nrOfTimes;
	}

	public int getNrOfTimes() {
		return nrOfTimes;
	}
}
