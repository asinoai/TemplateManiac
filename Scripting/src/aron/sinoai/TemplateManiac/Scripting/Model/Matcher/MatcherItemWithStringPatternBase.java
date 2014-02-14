package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MatcherItemWithStringPatternBase extends MatcherItemBase {
	
	@XmlElement(name="the-string-pattern")
	private ArrayList<String> stringPatternItems;
	
	@XmlAttribute(name="theStringPattern")
	private String stringPatternItem;
	
	@XmlTransient StringTemplate theStringTemplate;

	public MatcherItemWithStringPatternBase() {
		super();
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		final String theStringPattern;
		if (stringPatternItem != null && stringPatternItems != null) {
			throw new RuntimeException(String.format("Matcher-item '%s': both 'theStringPattern' attribute and sub-node was specified! Use only one of them!", getName()));
		} else if (stringPatternItem == null && stringPatternItems == null) {
			throw new RuntimeException(String.format("Matcher-item '%s': neither the 'theStringPattern' attribute nor sub-node was specified!", getName()));
		} else {
			if (stringPatternItems != null) {
				theStringPattern = linesToString(stringPatternItems);
			} else if (stringPatternItem != null) {
				theStringPattern = stringPatternItem;
			} else {
				throw new RuntimeException("Should never happen!");
			}
		}

		theStringTemplate = createStringTemplate(theStringPattern, context, parent);
		
		super.compile(context, parent);
	}
	
	public StringTemplate getTheStringTemplate() {
		return theStringTemplate;
	}
	
	public String getTheString() {
		final StringTemplate theStringTemplate = getTheStringTemplate();
		prepareStringTemplate(theStringTemplate);
		final String theString = theStringTemplate.toString();
		return theString;
	}
	

}
