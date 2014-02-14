package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;

@XmlRootElement(name = "reg-exp")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegExp extends MatcherItemWithStringBase{
	
	@XmlTransient
	private Pattern pattern;
	
	@XmlTransient
	private ArrayList<String> groups;

	public RegExp(){
		super();
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		pattern = Pattern.compile(getTheString());
	}
	
	@Override
	protected boolean isAMatchOverride() {
		Source source = getSource();
		Matcher matcher = pattern.matcher(source.getTheString());

		boolean result = matcher.find(source.getPos());
		result = result && (matcher.start()==source.getPos());
		
		if (result) {
			source.setPos(matcher.end());
			
			int count = matcher.groupCount() + 1;
			if (count > 1) {
				groups = new ArrayList<String>(count);
				
				//using the reg-exp convention: group 0 is the entire match and the others are indexed from 1
				for(int i = 0; i < count; i++) {
					groups.add(matcher.group(i));
				}
			} else {
				groups = null;
			}
		}
		
		return result;
	}

	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
	}

	public ArrayList<String> getGroups() {
		return groups;
	}

}
