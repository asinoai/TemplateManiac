package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.scripting.model.Message;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;

@SuppressWarnings("unused")
@XmlRootElement(name = "matcher-timer")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatcherTimer extends MatcherContainmentBase {
	static private final Logger LOGGER = Logger.getLogger(MatcherTimer.class);

	@XmlTransient
	ScriptingContext context;
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		this.context = context;
		
		super.compile(context, parent);
	}
	
	protected boolean isAMatchOverride() {
		long startTime = System.currentTimeMillis();
		
		boolean isMatch = getMatchPiece().isAMatch();

		long elapsed = (System.currentTimeMillis() - startTime); //msecs
		long elapsedSeconds = elapsed / 1000;
		long elapsedRemainingMSeconds = elapsed % 1000;
		
		LOGGER.info(String.format("Finished running matcher-timer '%s' (in %d second(s) and %d millisecond(s))", getDisplayName(), elapsedSeconds, elapsedRemainingMSeconds));

		return isMatch;
	}

	public MatcherTimer() {
		super();
	}
	
	@Override
	public void dispose() {
		context = null;
		
		super.dispose();
	}
	
}
