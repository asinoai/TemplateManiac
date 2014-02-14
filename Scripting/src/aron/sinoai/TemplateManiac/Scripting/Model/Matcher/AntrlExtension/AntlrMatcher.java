package aron.sinoai.templatemaniac.scripting.model.matcher.antrlextension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherItemBase;

@XmlRootElement(name = "antlr-matcher")
@XmlAccessorType(XmlAccessType.FIELD)
public class AntlrMatcher extends MatcherItemBase{

	@XmlAttribute
	private String kind;
	
	@XmlTransient
	private AntlrParser antlrParser;

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		antlrParser = context.getMatcherContext().getFactory().createInstance(kind);
	}
	
	@Override
	protected boolean isAMatchOverride() {
		
		return antlrParser.isAMatch();
	}

}
