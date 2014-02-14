package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult.Piece;

public class BacktrackingContext implements Disposable {

	public static enum ProcessState {
		Normal
		,GoingBack
	}

	public static interface MatcherState extends Disposable {
		MatchingResult.Piece getParent();
		boolean isOnPath(MatchingResult.Piece resultPiece);
	}

	public static abstract class MatcherStateBase implements MatcherState {
		private MatchingResult.Piece parent;
		private Set<MatchingResult.Piece> pathCache;
 
		public MatcherStateBase(MatchingResult.Piece parent) {
			this.parent = parent;
			
		}

		@Override
		public MatchingResult.Piece getParent() {
			return parent;
		}

		public boolean isOnPath(MatchingResult.Piece resultPiece) {
			if (pathCache == null) {
				pathCache = new HashSet<MatchingResult.Piece>();
				
				final List<Piece> subPieces = parent.getSubPieces();
				int size = subPieces.size();
				final Piece currentSubPiece = size > 0 ? subPieces.get(size - 1) :  null;
				if (currentSubPiece != null) {
					pathCache.add(currentSubPiece);
				}
				
				MatchingResult.Piece item = parent;
				while (item != null) {
					pathCache.add(item);
					item = item.findParent();
				}
			}
			
			return pathCache.contains(resultPiece);
		}

		@Override
		public void dispose() {
			parent = null;
			
			if (pathCache != null) {
				pathCache.clear();
				pathCache = null;
			}
		}
	}

	// contains only the matchers which participate to the backtracking process
	private Stack<MatcherState> matcherStates = new Stack<BacktrackingContext.MatcherState>();
	private ProcessState processState = ProcessState.Normal;

	public BacktrackingContext() {
	}
	
	public Stack<MatcherState> getMatcherStates() {
		return matcherStates;
	}

	public void setProcessState(ProcessState processState) {
		this.processState = processState;
	}

	public ProcessState getProcessState() {
		return processState;
	}
	
	public void reset() {
		for (MatcherState state : matcherStates) {
			state.dispose();
		}
		
		matcherStates.clear();
	}

	public void dispose() {
		reset();
	}

}
