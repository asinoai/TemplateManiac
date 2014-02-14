package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "matcher-container")
public class MatcherContainer extends MatcherContainmentBase {

	@XmlAttribute
	private boolean useBacktracking = false;

	@XmlTransient
	private BacktrackingContext backtrackingContext;
	
	@XmlTransient
	private ScriptingContext context;

	public MatcherContainer()
	{
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		this.context = context;
		
		if (useBacktracking && context.getDefaults().getMatcherBacktrackingContext() == null) {
			Defaults oldDefaults = context.getDefaults();
			try {
				//using a new backtracking context
				backtrackingContext = new BacktrackingContext();
				
				Defaults newDefaults = new Defaults(oldDefaults);
				newDefaults.setMatcherBacktrackingContext(backtrackingContext);
				context.setDefaults(newDefaults);
		
				super.compile(context, parent);
			} finally {
				context.setDefaults(oldDefaults);
			}
		} else {
			super.compile(context, parent);
		}
	}
	
	@Override
	public void execute() {
		if (backtrackingContext != null) {
			Source source = getSource();
			BacktrackingContext oldContext = source.getBacktrackingContext();
			try {
				//adjusting the backtracking context for the sub elements
				source.setBacktrackingContext(backtrackingContext);
				super.execute();
			} finally {
				source.setBacktrackingContext(oldContext);
			}
		} else {
			super.execute();
		}
	}

	@Override
	protected boolean isAMatchOverride() {
		return getMatchPiece().isAMatch();
	}

	public void setUseBacktracking(boolean useBacktracking) {
		this.useBacktracking = useBacktracking;
	}

	public boolean isUseBacktracking() {
		return useBacktracking;
	}

	public BacktrackingContext getBacktrackingContext() {
		return backtrackingContext;
	}
	
	public void setContext(ScriptingContext context) {
		this.context = context;
	}

	public ScriptingContext getContext() {
		return context;
	}
	
	@Override
	public void reset() {
		super.reset();

		if (backtrackingContext != null) {
			backtrackingContext.reset();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		
		if (backtrackingContext != null) {
			backtrackingContext.dispose();
			backtrackingContext = null;
		}
		
		context = null;
	}
}
