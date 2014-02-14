package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItemVisitor;
import aron.sinoai.templatemaniac.scripting.model.matcher.antrlextension.AntlrMatcher;
import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.ProcessState;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult.Piece;


@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MatcherContainerBase extends MatcherItemBase {
	//contains the not mapped elements; keep this anyhow, because otherwise 
	//not mapped elements might arrive in the explicitly mapped mixed lists
	@XmlAnyElement
	@XmlMixed
	private ArrayList<Object> others;
	
	@XmlElementRefs( {
		@XmlElementRef( name = "any-char", type = AnyChar.class )
		,@XmlElementRef( name = "char-in-string", type = CharInString.class)
		,@XmlElementRef( name = "char-not-in-string", type = CharNotInString.class)
		,@XmlElementRef( name = "empty", type = Empty.class)
		,@XmlElementRef( name = "end-of-parse", type = EndOfParse.class)
		,@XmlElementRef( name = "not-end-of-parse", type = NotEndOfParse.class)
		,@XmlElementRef( name = "fix-nr-of-times", type = FixNrOfTimes.class)
		,@XmlElementRef( name = "match-one-of-them", type = MatchOneOfThem.class)
		,@XmlElementRef( name = "no-digit", type = NoDigit.class)
		,@XmlElementRef( name = "no-white", type = NoWhite.class)
		,@XmlElementRef( name = "null-or-many-multi", type = NullOrManyMulti.class)
		,@XmlElementRef( name = "one-digit", type = OneDigit.class)
		,@XmlElementRef( name = "one-or-many-multi", type = OneOrManyMulti.class)
		,@XmlElementRef( name = "match-one-after-another", type = MatchOneAfterAnother.class)
		,@XmlElementRef( name = "one-white", type = OneWhite.class)
		,@XmlElementRef( name = "string-match", type = StringMatch.class)
		,@XmlElementRef( name = "string-pattern-match", type = StringPatternMatch.class)
		,@XmlElementRef( name = "byte-match", type = ByteMatch.class)
		,@XmlElementRef( name = "word-by-word", type = WordByWord.class)
		,@XmlElementRef( name = "is-in-collection", type = IsInCollection.class)
		,@XmlElementRef( name = "reg-exp", type = RegExp.class)
		,@XmlElementRef( name = "until-the-string", type = UntilTheString.class)
		,@XmlElementRef( name = "optional-matching", type = OptionalMatching.class)
		,@XmlElementRef( name = "matcher-reference", type = MatcherReference.class)
		,@XmlElementRef( name = "dynamic-matcher", type = DynamicMatcher.class)
		,@XmlElementRef( name = "matcher-timer", type = MatcherTimer.class)
		,@XmlElementRef( name = "antlr-matcher", type = AntlrMatcher.class)
		,@XmlElementRef( name = "matcher-with-target", type = MatcherWithTarget.class)
		,@XmlElementRef( name = "forward-no-match", type = ForwardNoMatch.class)
		,@XmlElementRef( name = "forward-match", type = ForwardMatch.class)
		,@XmlElementRef( name = "pair-match", type = PairMatch.class)
		,@XmlElementRef( name = "start-of-parse", type = StartOfParse.class)} )
	@XmlMixed
	private List<MatcherItem> matchPieces = new ArrayList<MatcherItem>();

	public MatcherContainerBase() {
		super();
	}

	public void setMatchPieces(List<MatcherItem> matchPieces) {
		this.matchPieces = matchPieces;
	}

	public List<MatcherItem> getMatchPieces() {
		return matchPieces;
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		for(MatcherItem item : matchPieces) {
			item.compile(context, this);
		}
	}

	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);

		for(MatcherItem item : matchPieces) {
			item.visitAllFromTopToDown(visitor);
		}
	}
	@Override
	protected int start() {
		int result = super.start();
		
		BacktrackingContext backtrackingContext = getSource().getBacktrackingContext();
		if (backtrackingContext != null && backtrackingContext.getProcessState() == ProcessState.GoingBack) {
			Piece subPiece = getResult().getCurrentPiece().getSubPieces().get(result);
			//we search for the right match-piece index; result piece and match-piece indexes might be out of sync
			result = getMatchPieces().indexOf(subPiece.getParentMatcherItem());
		}
		
		return result;
	}
}
