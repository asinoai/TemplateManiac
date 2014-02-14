package aron.sinoai.templatemaniac.scripting.model.matcher;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.ProcessState;

@XmlRootElement(name = "match-one-after-another")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatchOneAfterAnother extends MatcherContainerBase {
	static private final Logger LOGGER = Logger.getLogger(MatchOneAfterAnother.class);

	public MatchOneAfterAnother() {
		super();
	}

	@Override
	protected boolean isAMatchOverride() {
		boolean isMatch = true;

		List<MatcherItem> matchPieces = getMatchPieces();
		int count = matchPieces.size();

		for (int i = start(); i < count; i++) {
			MatcherItem item = matchPieces.get(i);
			isMatch = item.isAMatch();
			if (!isMatch) {
				//we are trying to see, whether we can "backtrack" to saved state
				BacktrackingContext backtrackingContext = getSource().getBacktrackingContext();
				if (backtrackingContext != null && 
					!backtrackingContext.getMatcherStates().empty() &&
					getResult().getSubPieceIndexForState(backtrackingContext.getMatcherStates().peek()) != -1) {
					//the last saved state is on our path, we just need to go back to it
					if (backtrackingContext.getProcessState() == ProcessState.Normal) {
						backtrackingContext.setProcessState(ProcessState.GoingBack);
						i = start() - 1;
						continue;
					} else {
						throw new RuntimeException("Backtracking sync error!");
					}
				}
			}
			
			if (!isMatch) {
				break;
			}
		}
		
		return isMatch;
	}

}
