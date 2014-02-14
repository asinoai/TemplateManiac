package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult;
import aron.sinoai.templatemaniac.scripting.model.matcher.NoDigit;
import aron.sinoai.templatemaniac.scripting.model.matcher.OneDigit;
import aron.sinoai.templatemaniac.scripting.model.matcher.Source;


public class OneDigitTest {
	private OneDigit oneDigit;
	private NoDigit noDigit;
	
	private static String TEST_STRING = "Here comes some text: ft$yh, a8pple, ^&water.";
	private static char TEST_CHAR = 'f';
	private static char TEST_DIGIT = '8';
	private static int NO_DIGIT_AT_POSITION = TEST_STRING.indexOf(TEST_CHAR);
	private static int DIGIT_AT_POSITION = TEST_STRING.indexOf(TEST_DIGIT);
	
	private ScriptingContext context;
	
    @Before 
    public void initialize() {
    	
    	Source source = new Source(TEST_STRING);

    	oneDigit = new OneDigit();
    	oneDigit.setSource(source);
    	
    	noDigit = new NoDigit();
    	noDigit.setSource(source);
    	
    	context = TestManager.get().createScriptingContext();
    }	
	
	@Test
	public void testOneDigitExecute() {
		
    	oneDigit.compile(context, oneDigit);
    	oneDigit.getSource().setPos(DIGIT_AT_POSITION);
    	
    	oneDigit.execute();
		
    	MatchingResult result = oneDigit.getResult();
		assertNotNull(result);
		assertNotNull(result.getCurrentPiece());
		assertTrue(result.getCurrentPiece().getStartPos() == DIGIT_AT_POSITION);
		assertTrue(result.getCurrentPiece().getEndPos() == DIGIT_AT_POSITION + 1);
		assertTrue(result.isMatch());
		assertEquals(String.valueOf(TEST_DIGIT), result.getValue());
		
		oneDigit.getSource().setPos(NO_DIGIT_AT_POSITION);
		oneDigit.execute();
		
		result = oneDigit.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());		
	}

	@Test
	public void testNoDigitExecute() {
		noDigit.compile(context, noDigit);
		noDigit.getSource().setPos(NO_DIGIT_AT_POSITION);
    	
		noDigit.execute();
		
    	MatchingResult result = noDigit.getResult();
		assertNotNull(result);
		assertNotNull(result.getCurrentPiece());
		assertTrue(result.getCurrentPiece().getStartPos() == NO_DIGIT_AT_POSITION);
		assertTrue(result.getCurrentPiece().getEndPos() == NO_DIGIT_AT_POSITION + 1);
		assertTrue(result.isMatch());
		assertEquals(String.valueOf(TEST_CHAR), result.getValue());

		noDigit.getSource().setPos(DIGIT_AT_POSITION);
    	
		noDigit.execute();
		result = noDigit.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());
	}
}
