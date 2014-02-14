package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "forward-match")
public class ForwardMatch extends MatcherContainmentBase {
	
	
	@Override
	protected boolean isAMatchOverride() {
		
		final Source source = getSource();
		int pos = source.getPos();

		boolean result = getMatchPiece().isAMatch();

		source.setPos(pos);
		
		return result;
	}


}
