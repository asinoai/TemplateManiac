package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class MatcherItemWithStringBase extends MatcherItemBase {
	
	@XmlElement(name="the-string")
	private ArrayList<String> stringItems;
	
	@XmlAttribute(name="theString")
	private String stringItem;
	
	@XmlTransient String theString;

	public MatcherItemWithStringBase() {
		super();
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (stringItem != null && stringItems != null) {
			throw new RuntimeException(String.format("Matcher-item '%s': both 'theString' attribute and sub-node was specified! Use only one of them!", getName()));
		} else if (stringItem == null && stringItems == null) {
			throw new RuntimeException(String.format("Matcher-item '%s': neither the 'theString' attribute nor sub-node was specified!", getName()));
		} else {
			if (stringItems != null) {
				theString = linesToString(stringItems);
			} else if (stringItem != null) {
				theString = stringItem;
			} 
		}
		
		super.compile(context, parent);
	}

	public String getTheString() {
		return theString;
	}

	public void setTheString(String theString) {
		this.theString = theString;
	}

	public void setStringItem(String stringItem) {
		this.stringItem = stringItem;
	}

	public String getStringItem() {
		return stringItem;
	}

	public void setStringItems(ArrayList<String> stringItems) {
		this.stringItems = stringItems;
	}

	public ArrayList<String> getStringItems() {
		return stringItems;
	}
	
	
	
}
