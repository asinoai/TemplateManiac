package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.CharInString;
import aron.sinoai.templatemaniac.scripting.model.matcher.CharNotInString;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatchingResult;
import aron.sinoai.templatemaniac.scripting.model.matcher.Source;


public class CharInStringTest {

	private CharInString charInString;
	private CharNotInString charNotInString;
	
	private static int AT_POSITION = 22;
	private static String TEST_STRING = "Here comes some text: ft$yh, apple, ^&water.";
	private static String TEST_INSTRING = "rift";
	private static String TEST_NOT_INSTRING = "easy";
	
	private ScriptingContext context;
	
    @Before 
    public void initialize() {
    	charInString = new CharInString();
    	charInString.setSource(new Source(TEST_STRING));
    	charInString.getSource().setPos(AT_POSITION);
    	
    	charNotInString = new CharNotInString();
    	charNotInString.setSource(new Source(TEST_STRING));
    	charNotInString.getSource().setPos(AT_POSITION);
    	
    	context = TestManager.get().createScriptingContext();
    }	
	
	@Test
	public void testCompile() {
		charInString.setStringItem(TEST_INSTRING);
		charInString.compile(context, charInString);
		
		assertNotNull(charInString.getTheString());
		assertEquals(TEST_INSTRING, charInString.getTheString());
		
		assertNotNull(charInString.getSource());
		assertEquals(TEST_STRING, charInString.getSource().getTheString());
		
		assertNotNull(charInString.getResult());
		assertNotNull(charInString.getResult().getSource());
	}

	@Test
	public void testCharInStringExecuteWithContained() {
		//test contained char
    	charInString.setStringItem(TEST_INSTRING);
    	charInString.compile(context, charInString);
		
    	charInString.execute();
		
    	MatchingResult result = charInString.getResult();
		assertNotNull(result);
		assertNotNull(result.getCurrentPiece());
		assertTrue(result.getCurrentPiece().getStartPos() == AT_POSITION);
		assertTrue(result.getCurrentPiece().getEndPos() == AT_POSITION + 1);
		assertTrue(result.isMatch());
	}

	@Test
	public void testCharInStringExecuteWithNotContained() {
		//test not contained char
    	charInString.setStringItem(TEST_NOT_INSTRING);
    	charInString.compile(context, charInString);
    	
		charInString.execute();
		MatchingResult result = charInString.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());
	}
	
	@Test
	public void testCharNotInStringExecuteWithContained() {
    	charNotInString.setStringItem(TEST_INSTRING);
    	charNotInString.compile(context, charNotInString);
    	
    	charNotInString.execute();
		MatchingResult result = charNotInString.getResult();
		assertNotNull(result);
		assertFalse(result.isMatch());
	}

	@Test
	public void testCharNotInStringExecuteWithNotContained() {
		charNotInString.setStringItem(TEST_NOT_INSTRING);
		charNotInString.compile(context, charNotInString);
		
		charNotInString.execute();
		
    	MatchingResult result = charNotInString.getResult();
		assertNotNull(result);
		assertNotNull(result.getCurrentPiece());
		assertTrue(result.getCurrentPiece().getStartPos() == AT_POSITION);
		assertTrue(result.getCurrentPiece().getEndPos() == AT_POSITION + 1);
		assertTrue(result.isMatch());
	}	
	
}
