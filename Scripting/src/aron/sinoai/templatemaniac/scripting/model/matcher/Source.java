package aron.sinoai.templatemaniac.scripting.model.matcher;

public class Source implements Disposable {
	
	private String theString;
	private int pos;
	private BacktrackingContext backtrackingContext;

	public Source() 
	{ 
	}

	public Source(String theString) {
		this.theString = theString;
		firstPos();
	}

	public void setTheString(String theString) {
		this.theString = theString;
	}

	public String getTheString() {
		return theString;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getPos() {
		return pos;
	}

	public void incPos() {
		pos++;
	}

	public void incPos(int delta) {
		pos += delta;
	}

	public void firstPos() {
		this.pos = 0;
	}

	public void lastPos() {
		this.pos = theString.length();
	}
	
	public char getParseChar() {
		return theString.charAt(pos);
	}
	
	public String getParseString(int length) {
		return theString.substring(pos, pos + length);
	}

	public String getParseString() {
		return theString.substring(pos);
	}
	
	public boolean isEOParse() {
		return ( pos >= theString.length()  );
	}
	
	public boolean isAValidPosition(int pos) {
		return ( (pos >= 0) && ( pos < theString.length() ) ); 
	}

	public void setBacktrackingContext(BacktrackingContext backtrackingContext) {
		this.backtrackingContext = backtrackingContext;
	}

	public BacktrackingContext getBacktrackingContext() {
		return backtrackingContext;
	}

	public void reset() {
		theString = null;

		//the backtracking context is reseted by the matcher container
	}
	
	public void dispose() {
		theString = null;

		//the backtracking context is disposed by the matcher container
		backtrackingContext = null;
	}

}
