package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.ArrayList;
import java.util.List;

import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.MatcherState;

public class MatchingResult implements Disposable {

	public static class Piece implements Disposable{
		private int startPos;
		private int endPos;
		private boolean match;
		private List<Piece> subPieces = new ArrayList<Piece>(1); //for many cases we just have 1 element here
		private MatcherItem parentMatcherItem;

		public Piece(MatcherItem parentMatcherItem) {
			this.parentMatcherItem = parentMatcherItem;
		}

		public void setStartPos(int startPos) {
			this.startPos = startPos;
		}

		public int getStartPos() {
			return startPos;
		}

		public void setEndPos(int endPos) {
			this.endPos = endPos;
		}

		public int getEndPos() {
			return endPos;
		}

		public void setMatch(boolean match) {
			this.match = match;
		}

		public boolean isMatch() {
			return match;
		}

		public void addSubPiece(Piece piece) {
			getSubPieces().add(piece);
		}

		public List<Piece> getSubPieces() {
			return subPieces;
		}

		public MatcherItem getParentMatcherItem() {
			return parentMatcherItem;
		}
		
		public Piece findParent() {
			Piece result = null;
			
			MatchingResult parentResult = parentMatcherItem.getResult().getParent();
			if (parentResult != null) {
				result = parentResult.getCurrentPiece();
			}
			
			return result;
		}

		public void executeMatchScript() {
			if (match) {
				parentMatcherItem.getResult().setCurrentPiece(this);
				
				MatcherScriptContainer onMatch = parentMatcherItem.getOnMatch();
				if (onMatch != null) {
					onMatch.execute();
				}
				
				for (Piece item : subPieces) {
					item.executeMatchScript();
				}

				MatcherScriptContainer onAfterMatch = parentMatcherItem.getOnAfterMatch();
				if (onAfterMatch != null) {
					onAfterMatch.execute();
				}
			
			}
		}


		public void dispose() {
			subPieces.clear();
			parentMatcherItem = null;
		}
	}
	
	private Source source;
	private MatchingResult parent;
	private Piece current;
	
	public MatchingResult() {
	}
	
	public Source getSource() {
		return source;
	}
	
	public void setSource(Source source) {
		this.source = source;
	}
	
	public int getStartPos() {
		return current.getStartPos();
	}
	
	public int getEndPos() {
		return current.getEndPos();
	}
	
	public void recordStartPos() {
		current.setStartPos(source.getPos());
	}
	
	public void recordEndPos() {
		current.setEndPos(source.getPos());
	}
	
	public String getValue() {
		String result = null;
		result = isMatch() ? source.getTheString().substring(getStartPos(), getEndPos()) : "";

		return result;
	}

	public void recordMatch(boolean match) {
		current.setMatch(match);
	}

	public boolean isMatch() {
		return current.isMatch();
	}

	public void newPiece(MatcherItem parentMatcherItem) {
		current = new Piece(parentMatcherItem);
		
		if (parent != null) {
			parent.getCurrentPiece().addSubPiece(current);
		}
	}
	
	public Piece getCurrentPiece() {
		return current;
	}
	
	protected void setCurrentPiece(Piece current) {
		this.current = current;
	}
	
	
	public void setParent(MatchingResult parent) {
		this.parent = parent;
	}

	public MatchingResult getParent() {
		return parent;
	}

	public void executeMatchScript() {
		current.executeMatchScript();
	}

	public void reset() {
		if (current != null) {
			current.dispose();
			current = null;
		}
	}
	
	
	protected static int indexOfPieceForState(List<Piece> pieces, MatcherState matcherState) {
		int result = -1;
		
		int count = pieces.size();
		for(int i = 0; i < count; i++) {
			Piece piece = pieces.get(i);
			if (matcherState.isOnPath(piece)) {
				result = i;
				break;
			}
		}
		
		return result;
	}

	public void resetToState(MatcherState matcherState) {
		if (!matcherState.isOnPath(current)) {
			List<Piece> subPieces = parent.getCurrentPiece().getSubPieces();
			int index = indexOfPieceForState(subPieces, matcherState);
			
			current = subPieces.get(index);
		}

		List<Piece> subPieces = current.getSubPieces();
		int index = indexOfPieceForState(subPieces, matcherState);
		if (index != -1) {
			//removing the elements after the state
			int count = subPieces.size();
			if (index + 1 < count) {
				List<Piece> elementsToDelete = subPieces.subList(index + 1, count);
				
				elementsToDelete.clear();
			}
		}
	}

	public int getSubPieceIndexForState(MatcherState matcherState) {
		return indexOfPieceForState(current.getSubPieces(), matcherState);
	}
	
	@Override
	public void dispose() {
		parent = null;
		
		if (current != null) {
			current.dispose();
			current = null;
		}
	}
}
