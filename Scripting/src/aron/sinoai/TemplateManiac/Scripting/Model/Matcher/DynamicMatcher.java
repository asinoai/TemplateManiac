package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.PatternTemplateKey;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.TemplateKeyBase;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "dynamic-matcher")
@XmlAccessorType(XmlAccessType.FIELD)
public class DynamicMatcher extends MatcherReferenceBase {

	static private final Logger LOGGER = Logger.getLogger(DynamicMatcher.class);
	
	static public enum EvaluateKind {
		None
		,FirstTime
		,EachTime
	}

	@XmlAttribute
	private String templatePattern;

	@XmlAttribute
	private EvaluateKind evaluate = EvaluateKind.FirstTime;

	@XmlTransient
	private Defaults defaults;
	
	@XmlTransient
	private String templateContent;

	public DynamicMatcher() {
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		defaults = context.getDefaults();
		
		super.compile(context, parent);
	}
	
	protected void doInitMatchPiece(TemplateKeyBase key) {
		ScriptingContext context = getContext();
		if (context.getRecursivityLevel(key) <= MatcherContext.DEFAULT_MAX_RECURSIVITY_LEVEL) {

			//this code is called on execution; we need to use the context-defaults, saved on compiling;
			Defaults oldDefaults = context.getDefaults();
			try {
				context.setDefaults(defaults);
				PatternTemplateKey patternKey = (PatternTemplateKey) key;
				
				//we don't take it from the cache, in case we evaluate it each time
				ScriptingItem newPiece = (evaluate == EvaluateKind.EachTime) ? null : context.findPatternTemplate(patternKey);

				if (newPiece == null) {
					StringTemplate template = createStringTemplate(templatePattern, context, this);
					prepareStringTemplate(template);
					String newContent = template.toString();
				
					if (evaluate == EvaluateKind.EachTime) {
						//we need the initialization also next time (actually each time)
						setMatchPieceInitialized(false);

						MatcherItem piece = getMatchPiece();
						if (piece == null || (piece != null && !newContent.equals(templateContent)) ) {
							//we need a new piece (i.e. there is none or not the same as last time)
							
							if (piece != null) {
								//we evict and dispose the old one, if any
								context.deepEvict(piece);
								piece.deepDispose();
							}

							newPiece = context.fetchScriptingItem(newContent);
							templateContent = newContent;
						}
						
						
						if (piece != null) { 
							if (!newContent.equals(templateContent)) {
								
							}
						} else {
							newPiece = context.fetchScriptingItem(newContent);
						}
					} else {
						newPiece = context.registerAndFetchPatternTemplate(patternKey, newContent);
					}
				}
				
				if (newPiece != null) {
					if (!(newPiece instanceof MatcherItem)) {
						throw new RuntimeException(String.format("MatchPatternReference '%s': the '%s' is not a matcher item!", getName(), templatePattern));
					}
					
					setMatchPiece((MatcherItem)newPiece);
					
					getMatchPiece().setCurrentPrefix(getCurrentPrefix());
			
					getMatchPiece().compile(context, this);
				}
			} finally {
				context.setDefaults(oldDefaults);
			}
		} else {
			//no real use for this, but preserved for debugging
			//LOGGER.warn(String.format("MatchPatternReference '%s': the template '%s' exceeded the recursivity level!", getName(), templatePattern));
		}
	}

	public PatternTemplateKey createTemplateKey() {
		PatternTemplateKey result = new PatternTemplateKey(getGroupFile(), templatePattern);
		return result;
	}

	public void setTemplatePattern(String templatePattern) {
		this.templatePattern = templatePattern;
	}

	public String getTemplatePattern() {
		return templatePattern;
	}

	protected void setDefaults(Defaults defaults) {
		this.defaults = defaults;
	}

	protected Defaults getDefaults() {
		return defaults;
	}

	public void setEvaluate(EvaluateKind evaluate) {
		this.evaluate = evaluate;
	}

	public EvaluateKind getEvaluate() {
		return evaluate;
	}


}
