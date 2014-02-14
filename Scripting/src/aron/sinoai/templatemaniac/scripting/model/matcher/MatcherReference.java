package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingTemplate;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.TemplateKey;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.TemplateKeyBase;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "matcher-reference")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatcherReference extends MatcherReferenceBase {

	static private final Logger LOGGER = Logger.getLogger(MatcherReference.class);

	@XmlAttribute
	private String templateName;

	@XmlAttribute(name="ns")
	private String prefix;
	

	public MatcherReference() {
	}
	
	protected void doInitMatchPiece(TemplateKeyBase key) {
		ScriptingContext context = getContext();
		if (context.getTemplates().containsKey(templateName))
		{
			TemplateKey simpleKey = (TemplateKey) key;
			ScriptingItem item = context.fetchFromTemplate(simpleKey);
			if (item != null) {
				if (!(item instanceof MatcherItem)) {
					throw new RuntimeException(String.format("MatchReference '%s': the '%s' is not a matcher item!", getName(), templateName));
				}
				
				//we need to set and propagate the correct prefix for the fetched item
				setMatchPiece((MatcherItem)item);
				getMatchPiece().setCurrentPrefix(getCurrentPrefix());
		
				if (prefix == null) {
					prefix = String.format("%s_", templateName);
				}
				
				context.applyPrefix(getMatchPiece(), prefix);		
				
				//we need to use the original defaults of the template
				ScriptingTemplate template = context.getTemplates().get(templateName);
				Defaults oldDefaults = context.getDefaults();
				try {
					context.setDefaults(template.getDefaults());
					
					getMatchPiece().compile(context, this);
				} finally {
					context.setDefaults(oldDefaults);
				}
			} else {
				//no real use for this; but preserved for debugging
				//LOGGER.warn(String.format("MatchReference '%s': the template '%s' exceeded the recursivity level!", getName(), templateName));
			}
		} else {
			throw new RuntimeException(String.format("MatchReference '%s': the template '%s' is not found!", getName(), templateName));
		}
	}
	
	protected TemplateKey createTemplateKey() {
		TemplateKey result = new TemplateKey(templateName);
		return result;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateName() {
		return templateName;
	}


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public String getPrefix() {
		return prefix;
	}

	//just an alias, which is shorter
	public String getNs() {
		return prefix;
	}

	
}
