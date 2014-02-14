package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult;
import aron.sinoai.templatemaniac.scripting.model.matcher.NoWhite;
import aron.sinoai.templatemaniac.scripting.model.matcher.OneWhite;
import aron.sinoai.templatemaniac.scripting.model.matcher.Source;


public class OneWhiteTest {
	private OneWhite oneWhite;
	private NoWhite noWhite;
	
	private static String TEST_STRING = "Here comes some text: ft$yh, \n" +
			"a8pple, 	^&water.";
	private static int[] WHITE_AT_POSITION = {TEST_STRING.indexOf(" "), TEST_STRING.indexOf("\t"), TEST_STRING.indexOf("\n")};
	private static char TEST_CHAR = 's';
	private static int NO_WHITE_AT_POSITION = TEST_STRING.indexOf(TEST_CHAR);
	
	private ScriptingContext context;
	
    @Before 
    public void initialize() {
    	
    	Source source = new Source(TEST_STRING);

    	oneWhite = new OneWhite();
    	oneWhite.setSource(source);
    	
    	noWhite = new NoWhite();
    	noWhite.setSource(source);
    	
    	context = TestManager.get().createScriptingContext();
    }	
	
	@Test
	public void testOneWhiteExecute() {
		
		oneWhite.compile(context, oneWhite);
		
		MatchingResult result = null;
		for (int i=0; i<WHITE_AT_POSITION.length; i++){
			oneWhite.getSource().setPos(WHITE_AT_POSITION[i]);
	    	
			oneWhite.execute();
			
	    	result = oneWhite.getResult();
			assertNotNull(result);
			assertNotNull(result.getCurrentPiece());
			assertTrue(result.getCurrentPiece().getStartPos() == WHITE_AT_POSITION[i]);
			assertTrue(result.getCurrentPiece().getEndPos() == WHITE_AT_POSITION[i] + 1);
			assertTrue(result.isMatch());
			assertEquals(String.valueOf(TEST_STRING.charAt(WHITE_AT_POSITION[i])), result.getValue());
		}
		
		oneWhite.getSource().setPos(NO_WHITE_AT_POSITION);
		oneWhite.execute();
		
		result = oneWhite.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());		
	}

	@Test
	public void testNoWhiteExecute() {
		noWhite.compile(context, noWhite);
		noWhite.getSource().setPos(NO_WHITE_AT_POSITION);
    	
		noWhite.execute();
		
    	MatchingResult result = noWhite.getResult();
		assertNotNull(result);
		assertNotNull(result.getCurrentPiece());
		assertTrue(result.getCurrentPiece().getStartPos() == NO_WHITE_AT_POSITION);
		assertTrue(result.getCurrentPiece().getEndPos() == NO_WHITE_AT_POSITION + 1);
		assertTrue(result.isMatch());
		assertEquals(String.valueOf(TEST_CHAR), result.getValue());

		noWhite.getSource().setPos(WHITE_AT_POSITION[1]);
    	
		noWhite.execute();
		result = noWhite.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());
	}

}
