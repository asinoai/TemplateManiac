package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult.Piece;

@XmlRootElement(name = "pair-match")
public class PairMatch extends MatcherContainerBase {
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		if (getMatchPieces().size() != 2) {
			throw new RuntimeException(String.format("PairMatch '%s' should have exactly two mathers (i.e. start and end delimiters)!", getName()));
		}
	}
	
	@Override
	protected boolean isAMatchOverride() {
		
		final Source source = getSource();
		
		final MatcherItem startMatcher = getMatchPieces().get(0);
		final MatcherItem endMatcher = getMatchPieces().get(1);

		int pos = source.getPos();
		
		boolean result = startMatcher.isAMatch();
		if (result) {
			int nestedLevel = 1;
			
			while (!source.isEOParse() && nestedLevel > 0) {
				int innerPos = source.getPos();
				boolean innerResult = endMatcher.isAMatch();
				if (innerResult) {
					nestedLevel--;
					
				} else {
					innerResult = startMatcher.isAMatch();
					if (innerResult) {
						nestedLevel++;
					} else {
						source.setPos(innerPos+1);
					}
				} 
			}
			
			result = (nestedLevel == 0);
		}

		if (!result) {
			source.setPos(pos);
		}
		
		return result;
	}

	private int getInnerStartPos() {
		final MatcherItem startMatcher = getMatchPieces().get(0);

		int result = 0;
		
		for(final Piece piece : getResult().getCurrentPiece().getSubPieces()) {
			if (piece.getParentMatcherItem() == startMatcher) {
				result = piece.getEndPos();
				break;
			}
		}
		
		return result;
	}

	private int getInnerEndPos() {
		final MatcherItem endMatcher = getMatchPieces().get(1);

		int result = 0;
		
		List<Piece> subPieces = getResult().getCurrentPiece().getSubPieces();
		//searching backwards
		for(int i = subPieces.size() - 1; i >= 0 ; i--) {
			final Piece piece = subPieces.get(i);
			if (piece.getParentMatcherItem() == endMatcher) {
				result = piece.getStartPos();
				break;
			}
		}
		
		return result;
	}
	
	
	public String getInnerValue() {
		return getResult().isMatch() ? getSource().getTheString().substring(getInnerStartPos(), getInnerEndPos()) : "";
	}
}
