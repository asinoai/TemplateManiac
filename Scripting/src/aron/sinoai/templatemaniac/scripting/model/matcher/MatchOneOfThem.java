package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.MatcherState;
import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.MatcherStateBase;
import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.ProcessState;

@XmlRootElement(name = "match-one-of-them")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatchOneOfThem extends MatcherContainerBase {
	
	public static enum MatchingKind {
		First
		,Longest
		,Shortest
	}
	
	public static class OneOfThemState extends MatcherStateBase {
	    public OneOfThemState(MatchingResult.Piece parent) {
	    	super(parent);
	    }
	}
	
	@XmlAttribute
	private MatchingKind matchingKind = MatchingKind.First;
	
	public MatchOneOfThem() {
		super();
	}
	
	@Override
	protected boolean isAMatchOverride() {
		boolean isMatch = false;
		
		List<MatcherItem> matchPieces = getMatchPieces();
		switch (matchingKind) {
		case First:
		{
			int count = matchPieces.size();
			BacktrackingContext backtrackingContext = getSource().getBacktrackingContext();
			for(int i = start(); i < count; i++) {
				MatcherItem item = matchPieces.get(i);
				if (backtrackingContext != null && 
						backtrackingContext.getProcessState() == ProcessState.GoingBack &&
						backtrackingContext.getMatcherStates().peek().getParent() == getResult().getCurrentPiece()) {
					//we arrived to the destination with the going back state, so we restore
					MatcherState state = backtrackingContext.getMatcherStates().pop();
					backtrackingContext.setProcessState(ProcessState.Normal);
					item.getResult().resetToState(state);
					//set the current result to no match and simply continuing
					getResult().recordMatch(false);
				} else {
					isMatch = item.isAMatch();
					if (isMatch) {
						if (backtrackingContext != null && 
								backtrackingContext.getProcessState() == ProcessState.Normal &&
								i < count - 1) {
							//we are not at the last choice, we could come back here to choose another sub-element, in case of backtracking
							backtrackingContext.getMatcherStates().add(new OneOfThemState(getResult().getCurrentPiece()));
						}
						
						break;
					}
				}
			}
			break;
		}
		case Longest:
		{
			noBacktrackingCheck();
			
			Source source = getSource();
			int maxPos = source.getPos(); 
			MatcherItem longestItem = null;
			
			for(MatcherItem item : matchPieces) {
				int beforeMatchPos = source.getPos();	
				if ( item.isAMatch() ) {
					int endPos = source.getPos();
					if (maxPos < endPos) {
						longestItem = item;
						maxPos = endPos;
					}
					source.setPos(beforeMatchPos);
				}
			}
			
			isMatch = (longestItem != null); 
			if (isMatch)
			{
				for(MatcherItem item : matchPieces) {
					if (longestItem != item) {
						item.getResult().reset();
					}
				}
				
				source.setPos(maxPos);
			}
			
			break;
		}
		case Shortest:
		{
			noBacktrackingCheck();

			Source source = getSource();
			int minPos = source.getTheString().length(); 
			MatcherItem longestItem = null; 
			
			for(MatcherItem item : matchPieces) {
				int beforeMatchPos = source.getPos();
				if ( item.isAMatch() ) {
					int endPos = source.getPos();
					if (minPos > endPos) {
						longestItem = item;
						minPos = endPos;
					}
					source.setPos(beforeMatchPos);
				}
			}
			
			isMatch = (longestItem != null); 
			if (isMatch)
			{
				for(MatcherItem item : matchPieces) {
					if (longestItem != item) {
						item.getResult().reset();
					}
				}
				
				source.setPos(minPos);
			}
			
			break;
		}
		default:
		}
		
		return isMatch;
	}

	public void setMatchingKind(MatchingKind matchingKind) {
		this.matchingKind = matchingKind;
	}

	public MatchingKind getMatchingKind() {
		return matchingKind;
	}
}
