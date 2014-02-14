package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItemVisitor;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.TemplateKeyBase;

public abstract class MatcherReferenceBase extends MatcherItemBase {

	@XmlTransient
	protected MatcherItem matchPiece;
	
	@XmlTransient
	private boolean matchPieceInitialized = false;
	
	@XmlTransient
	TemplateKeyBase key;

	@XmlTransient
	private
	ScriptingContext context;
	

	
	public MatcherReferenceBase() {
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		this.setContext(context);
		
		super.compile(context, parent);
	}

	
	@Override
	public void execute() {
		initMatchPiece();
		
		getContext().incrementRecursivityLevel(key);
		try {
			super.execute();
		} finally {
			getContext().decrementRecursivityLevel(key);
		}
	}

	protected void initMatchPiece() {
		if (!matchPieceInitialized) {
			matchPieceInitialized = true;
			key = createTemplateKey();
			doInitMatchPiece(key);
		}
	}
	
	protected abstract TemplateKeyBase createTemplateKey();
	protected abstract void doInitMatchPiece(TemplateKeyBase key);

	public void setMatchPiece(MatcherItem matchPiece) {
		this.matchPiece = matchPiece;
	}

	public MatcherItem getMatchPiece() {
		return matchPiece;
	}

	protected void setMatchPieceInitialized(boolean matchPieceInitialized) {
		this.matchPieceInitialized = matchPieceInitialized;
	}

	protected boolean isMatchPieceInitialized() {
		return matchPieceInitialized;
	}

	protected void setContext(ScriptingContext context) {
		this.context = context;
	}

	protected ScriptingContext getContext() {
		return context;
	}

	@Override
	protected boolean isAMatchOverride() {
		return (matchPiece != null && matchPiece.isAMatch());
	}

	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		
		if (matchPiece != null) {
			matchPiece.visitAllFromTopToDown(visitor);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		context = null;
	}
}
