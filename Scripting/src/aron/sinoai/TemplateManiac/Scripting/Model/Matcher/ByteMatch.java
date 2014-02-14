package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.xerces.impl.dv.util.Base64;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;

@XmlRootElement(name = "byte-match")
@XmlAccessorType(XmlAccessType.FIELD)
public class ByteMatch extends MatcherItemWithStringBase {
	
	public ByteMatch() {
		super();
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		setTheString(new String(Base64.decode(getTheString())));
	}
	
	protected boolean isAMatchOverride() {
		Source source = getSource();
		String theString = getTheString();
		boolean isMatch = source.isAValidPosition(source.getPos() + theString.length() - 1)
			&& (theString.equals(source.getParseString(theString.length())));
		if (isMatch) {
			source.incPos(theString.length());
		}
		
		return isMatch;
	}
}
