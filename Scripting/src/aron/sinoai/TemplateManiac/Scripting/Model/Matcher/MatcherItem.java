package aron.sinoai.templatemaniac.scripting.model.matcher;

import aron.sinoai.templatemaniac.scripting.model.GeneratorItem;

public interface MatcherItem extends GeneratorItem {

	Source getSource();
	
	boolean isAMatch();
	
	MatcherScriptContainer getOnMatch();
	void setOnMatch(MatcherScriptContainer script);
	MatcherScriptContainer getOnAfterMatch();
	void setOnAfterMatch(MatcherScriptContainer script);
	MatcherScriptContainer getOnPartialMatch();
	void setOnPartialMatch(MatcherScriptContainer script);
	
	
	MatchingResult getResult();

	void reset();
}
