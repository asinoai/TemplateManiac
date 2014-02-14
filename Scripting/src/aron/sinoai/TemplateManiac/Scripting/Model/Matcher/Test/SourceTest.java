package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aron.sinoai.templatemaniac.scripting.model.matcher.Source;

public class SourceTest {

	private Source source;
	private static String TEST_STRING = "This is a text.";
	private static int POSITION = 5;
	
    @Before 
    public void initialize() {
    	source = new Source(TEST_STRING);
    }
	
	@Test
	public void testSetPos() {
		assertTrue(source.getPos() == 0);
		
		source.setPos(POSITION);
		assertTrue(source.getPos() == POSITION);
	}

	@Test
	public void testGetPos() {
		assertTrue(source.getPos() == 0);
		
		source.setPos(POSITION);
		assertTrue(source.getPos() == POSITION);
	}

	@Test
	public void testIncPos() {
		source.incPos();
		assertTrue(source.getPos() == 1);
		
		source.setPos(POSITION);
		source.incPos();
		assertTrue(source.getPos() == POSITION + 1);
	}

	@Test
	public void testIncPosInt() {
		int i = source.getPos();
		source.incPos(3);
		assertTrue(source.getPos() == i+3);
	}

	@Test
	public void testFirstPos() {
		source.firstPos();
		assertTrue(source.getPos() == 0);
	}

	@Test
	public void testLastPos() {
		source.lastPos();
		assertTrue(source.getPos() == source.getTheString().length());
	}

	@Test
	public void testGetParseChar() {
		source.setPos(POSITION);
		assertTrue(TEST_STRING.charAt(POSITION) == source.getParseChar());
	}

	@Test
	public void testGetParseStringInt() {
		source.setPos(POSITION);
		assertEquals(TEST_STRING.substring(POSITION, POSITION + 10), source.getParseString(10));
	}

	@Test
	public void testGetParseString() {
		source.setPos(POSITION);
		assertEquals(TEST_STRING.substring(POSITION), source.getParseString());
	}

	@Test
	public void testIsEOParse() {
		source.setPos(POSITION);
		assertFalse(source.isEOParse());
		source.setPos(TEST_STRING.length());
		assertTrue(source.isEOParse());
	}

	@Test
	public void testIsAValidPosition() {
		assertFalse(source.isAValidPosition(-1));
		assertTrue(source.isAValidPosition(POSITION));
		assertFalse(source.isAValidPosition(TEST_STRING.length()));
	}

}
