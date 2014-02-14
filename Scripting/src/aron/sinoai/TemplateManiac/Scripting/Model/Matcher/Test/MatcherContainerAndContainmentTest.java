package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import aron.sinoai.templatemaniac.scripting.model.DataSource;
import aron.sinoai.templatemaniac.scripting.model.ScriptContainer;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItemBase;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContainer;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.TemplateKey;

public class MatcherContainerAndContainmentTest {

	private static final String TESTMAP_NAME = "testMatchersMap";
	private ScriptingContext context;
	private ScriptContainer script;
	
    @Before 
    public void initialize() {
    	
    	context = TestManager.get().createScriptingContext();
    	script = ScriptingItemBase.loadScript(
    			new File(TestManager.TESTSCRIPTS_FOLDER, "MatcherTest.script.xml").getPath(), context);
    	script.compile(context, script);
    }	
	
	
	@Test
	public void testExecute() {
		
		script.execute();
		
		DataSource data = context.getAll().get(TESTMAP_NAME);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> matchersMap = (Map<String, Object>)data.getResult();
		
		for (String matcherTemplateName : matchersMap.keySet()) {
	
				MatcherContainer matcher = (MatcherContainer)context.fetchFromTemplate(new TemplateKey(matcherTemplateName));
				matcher.compile(context, ((ScriptingItem)data));
				
				matcher.execute();
				
				MatchingResult result = matcher.getResult();
				
				assertNotNull(result);
				
				if (matcherTemplateName.endsWith("_ok")){
					assertTrue(result.isMatch());
					assertEquals(matchersMap.get(matcherTemplateName), result.getValue());
				}
				else if (matcherTemplateName.endsWith("_failure")){
					assertFalse(result.isMatch());
				}
		}
	}

}
