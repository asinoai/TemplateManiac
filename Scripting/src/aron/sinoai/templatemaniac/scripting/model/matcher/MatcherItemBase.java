package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import aron.sinoai.templatemaniac.scripting.model.DataSource;
import aron.sinoai.templatemaniac.scripting.model.GeneratorItemBase;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItemVisitor;
import aron.sinoai.templatemaniac.scripting.model.ScriptingMemoryShortage;
import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.MatcherState;
import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext.ProcessState;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MatcherItemBase extends GeneratorItemBase implements MatcherItem, DataSource {

	@XmlAttribute
	private String displayName;
	
	@XmlAttribute
	private String expressionPattern;

	@XmlElement(name = "on-match")
	private MatcherScriptContainer onMatch;

	@XmlElement(name = "on-after-match")
	private MatcherScriptContainer onAfterMatch;

	@XmlElement(name = "on-partial-match")
	private MatcherScriptContainer onPartialMatch;
	
	@XmlAttribute
	private boolean eraseEvents = false;
	
	@XmlTransient
	private Map<String, DataSource> all;

	@XmlTransient
	private StringTemplate expressionTemplate;
	
	@XmlTransient
	private MatchingResult result = new MatchingResult();

	@XmlTransient
	private Source source;

	public MatcherItemBase() {
	}
	
	@Override
	public DataSource getDataSource() {
		return this;
	}

	@Override
	public DataSource getParentDataSource() {
		return super.getDataSource();
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (eraseEvents) {
			visitAllFromTopToDown(new ScriptingItemVisitor() {
				
				@Override
				public void invoke(ScriptingItem item) {
					MatcherItem matcherItem = (MatcherItem)item; 
					matcherItem.setOnMatch(null);
					matcherItem.setOnAfterMatch(null);
					matcherItem.setOnPartialMatch(null);
				}
			});
		}
		
		all = context.getAll();
		
		context.registerToAll(this, getName());
		
		if (expressionPattern != null) {
			expressionTemplate = createStringTemplate(expressionPattern, context, parent);
			source = new Source();
		} else {
			if (parent != null && parent instanceof MatcherItem) {
				source = ((MatcherItem)parent).getSource();
			} else {
				throw new RuntimeException(String.format("MatcherItem '%s' should have a source specified in the expressionPattern attribute!", getName()));
			}
		}

		result.setSource(source);
		if (parent != null && parent instanceof MatcherItem) {
			result.setParent(((MatcherItem)parent).getResult());
		}

		super.compile(context, parent);

		if (onMatch != null) {
			onMatch.compile(context, this);
		}

		if (onAfterMatch != null) {
			onAfterMatch.compile(context, this);
		}
		
		if (onPartialMatch != null) {
			onPartialMatch.compile(context, this);
		}

	}
	
	

	@Override
	public void execute() {
		final Runtime runtime = Runtime.getRuntime();
		long memory = runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory();
		if (memory < ScriptingContext.LOW_MEMORY_TRESHOLD) {
			throw new ScriptingMemoryShortage();//in case this happens, put the block inside of a reloadable-item!
		}
		
		if (expressionTemplate != null) {
			prepareStringTemplateForDataSource(expressionTemplate, getParentDataSource());
			source.reset();
			source.setTheString(expressionTemplate.toString());
			source.firstPos();
			
			//reseting the results
			visitAllFromTopToDown(new ScriptingItemVisitor() {
				
				@Override
				public void invoke(ScriptingItem item) {
					if (item instanceof MatcherItem) {
						((MatcherItem) item).reset();
					}
				}
			});
		}

		final int pos;
		
		BacktrackingContext backtrackingContext = getSource().getBacktrackingContext();
		if (backtrackingContext != null && backtrackingContext.getProcessState() == ProcessState.GoingBack) {
			//we are going back, so we reset the result to the saved state and continue iteration with the saved index
			MatcherState lastState = backtrackingContext.getMatcherStates().peek();
			result.resetToState(lastState);
			pos = result.getCurrentPiece().getStartPos();
		} else {
			result.newPiece(this);
			result.recordStartPos();
			pos = source.getPos();
		}
		
		
		boolean isMatch = isAMatchOverride();
		result.recordMatch(isMatch);
		
		if (!isMatch) {
			source.setPos(pos);
		}
		
		result.recordEndPos();
		
		if (isMatch && onPartialMatch != null) {
			onPartialMatch.execute();
		}

		if (expressionTemplate != null) {
			result.executeMatchScript();
		}
	}
	
	abstract protected boolean isAMatchOverride(); 


	public boolean isAMatch() {
		execute();
		return result.isMatch();
	}
	
	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		
		final MatcherScriptContainer[] onHandlers = {onMatch, onAfterMatch, onPartialMatch};
		for (MatcherScriptContainer item : onHandlers) {
			if (item != null) {
				item.visitAllFromTopToDown(visitor);
			}
		}
	}
	
	@Override
	public MatchingResult getResult() {
		return result;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public Source getSource() {
		return source;
	}

	public void setExpressionPattern(String expressionPattern) {
		this.expressionPattern = expressionPattern;
	}

	public String getExpressionPattern() {
		return expressionPattern;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getNs() {
		return "";
	}

	public Map<String, DataSource> getAll() {
		return all;
	}

	public void setOnMatch(MatcherScriptContainer onMatch) {
		this.onMatch = onMatch;
	}

	public MatcherScriptContainer getOnMatch() {
		return onMatch;
	}

	public void setOnPartialMatch(MatcherScriptContainer onPartialMatch) {
		this.onPartialMatch = onPartialMatch;
	}

	public MatcherScriptContainer getOnPartialMatch() {
		return onPartialMatch;
	}

	public void setOnAfterMatch(MatcherScriptContainer onAfterMatch) {
		this.onAfterMatch = onAfterMatch;
	}

	public MatcherScriptContainer getOnAfterMatch() {
		return onAfterMatch;
	}

	public void setEraseEvents(boolean eraseEvents) {
		this.eraseEvents = eraseEvents;
	}

	public boolean isEraseEvents() {
		return eraseEvents;
	}

	protected int start() {
		final int startIndex;
	
		BacktrackingContext backtrackingContext = getSource().getBacktrackingContext();
		if (backtrackingContext != null && backtrackingContext.getProcessState() == ProcessState.GoingBack) {
			MatchingResult currentResult = getResult();
			MatcherState lastState = backtrackingContext.getMatcherStates().peek();
			startIndex = currentResult.getSubPieceIndexForState(lastState);
		} else {
			startIndex = 0;
		}
	
		return startIndex;
	}
	
	protected void noBacktrackingCheck() {
		BacktrackingContext backtrackingContext = getSource().getBacktrackingContext();
		if (backtrackingContext != null && backtrackingContext.getProcessState() == ProcessState.GoingBack) {
			//we are going back, but we don't support it
			throw new RuntimeException(String.format("MatcherItem '%s' not supported when backtacking is active!", getName()));
		} 
	}
	
	@Override
	public void reset() {
		getResult().reset();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		result.dispose();
		result = null;
		
		if (source != null) {
			//we dispose it only if we are the "owner" of it!
			if (expressionTemplate != null) {
				source.dispose();
			}
			
			source = null;
		}
		
		all = null;
	}
}
