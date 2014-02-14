package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.EndOfParse;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult;
import aron.sinoai.templatemaniac.scripting.model.matcher.Source;


public class EndOfParseTest {
	private EndOfParse endOfParse;
	
	private static String TEST_STRING = "Here comes some text: ft$yh, a8pple, ^&water.";
	private static int POSITION = TEST_STRING.indexOf("some");
	
	private ScriptingContext context;
	
    @Before 
    public void initialize() {
    	
    	Source source = new Source(TEST_STRING);

    	endOfParse = new EndOfParse();
    	endOfParse.setSource(source);
    	
    	context = TestManager.get().createScriptingContext();
    }	
	
	@Test
	public void testEndOfParseExecute() {
		
		endOfParse.compile(context, endOfParse);
		endOfParse.getSource().setPos(TEST_STRING.length());
    	
		endOfParse.execute();
		
    	MatchingResult result = endOfParse.getResult();
		assertNotNull(result);
		assertNotNull(result.getCurrentPiece());
		assertTrue(result.getCurrentPiece().getStartPos() == TEST_STRING.length());
		assertTrue(result.getCurrentPiece().getEndPos() == TEST_STRING.length());
		assertTrue(result.isMatch());
		assertEquals("",result.getValue());
		
		endOfParse.getSource().setPos(POSITION);
		endOfParse.execute();
		
		result = endOfParse.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());		
	}

}
