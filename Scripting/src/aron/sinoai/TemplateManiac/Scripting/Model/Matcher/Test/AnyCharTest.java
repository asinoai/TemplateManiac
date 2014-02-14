package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.AnyChar;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult;
import aron.sinoai.templatemaniac.scripting.model.matcher.Source;


public class AnyCharTest {
	private AnyChar anyChar;
	
	private static String TEST_STRING = "Here comes some text: ft$yh, a8pple, ^&water.";
	private static char TEST_CHAR = 's';
	private static int POSITION = TEST_STRING.indexOf(TEST_CHAR);
	
	private ScriptingContext context;
	
    @Before 
    public void initialize() {
    	
    	Source source = new Source(TEST_STRING);

    	anyChar = new AnyChar();
    	anyChar.setSource(source);
    	
    	context = TestManager.get().createScriptingContext();
    }	
	
	@Test
	public void testAnyCharExecute() {
		
		anyChar.compile(context, anyChar);
		anyChar.getSource().setPos(POSITION);
    	
		anyChar.execute();
		
    	MatchingResult result = anyChar.getResult();
		assertNotNull(result);
		assertNotNull(result.getCurrentPiece());
		assertTrue(result.getCurrentPiece().getStartPos() == POSITION);
		assertTrue(result.getCurrentPiece().getEndPos() == POSITION + 1);
		assertTrue(result.isMatch());
		assertEquals(String.valueOf(TEST_CHAR), result.getValue());
		
		anyChar.getSource().setPos(TEST_STRING.length());
		anyChar.execute();
		
		result = anyChar.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());		
	}

}
