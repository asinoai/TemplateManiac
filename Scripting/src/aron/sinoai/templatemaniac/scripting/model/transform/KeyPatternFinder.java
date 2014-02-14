package aron.sinoai.templatemaniac.scripting.model.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;


public class KeyPatternFinder implements ResultWrapper{
	
	static private final Logger LOGGER = Logger.getLogger(KeyPatternFinder.class);

	private List<String> inner;
	public List<HashMap<String, Pattern>> patterns = new ArrayList<HashMap<String, Pattern>>();
	private int deep = 5;
	
	
	public class Pattern{		
		int size;
		public List<String> elements = new ArrayList<String>();
		public List<Integer> positions = new ArrayList<Integer>();
		
		List<String> getElements(){
			return elements;
		}
		
		List<Integer> getPosition(){
			return positions;
		}
		
		public Pattern()
		{
			
		}
		public Pattern(Pattern pat)
		{
			for(String el : pat.getElements())
			{
				elements.add(el);
			}
			
			for(Integer pos : pat.getPosition())
			{
				positions.add(pos);
			}
		}
	}
	
	
	public static void main(String [ ] args){
				
		List<String> test = new ArrayList<String>();
		test.add("one");
		test.add("two");
		test.add("three");
		test.add("one");
		test.add("two");
		test.add("three");
		test.add("one");
		test.add("two");
		test.add("four");
		
		KeyPatternFinder keyPat = new KeyPatternFinder();
		keyPat.setInner(test);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setInner(Object inner) {
		
		try
		{
			this.inner = (List<String>) inner;
		}
		catch(ClassCastException ex)
		{
			LOGGER.fatal(ex);
		}
		
		HashMap<String, Pattern> words = new HashMap<String, Pattern>();
		patterns.add(words);
		getWords(words);
		//print(words);
				
		int i = 0;
		while(i< deep-1)
		{			
			HashMap<String, Pattern> pattern = patterns.get(i);			
			HashMap<String, Pattern> patternNew = new HashMap<String, Pattern>();
			patterns.add(patternNew);
			getPatterns((HashMap<String, Pattern>)words.clone(),(HashMap<String, Pattern>)pattern.clone(),-(i+1),patternNew);
			//print(patternNew);
			i++;
			
		}
	}
	
	private void getWords(HashMap<String, Pattern> words) {
				
		int position = 1;
		
		for(String word : inner)
		{
			String wordLow = word.toLowerCase();
			
			Pattern wordPosition = words.get(wordLow);
			
			if(wordPosition != null){
				List<Integer> pos = wordPosition.getPosition();
				pos.add(new Integer(position));
			}
			else
			{
				wordPosition = new Pattern();
				
				List<Integer> pos = wordPosition.getPosition();
				pos.add(new Integer(position));
				List<String> ele = wordPosition.getElements();
				ele.add(new String(wordLow));
				
				words.put(wordLow, wordPosition);
			}
			
			position++;
		}		
	}

	
	private void getPatterns(HashMap<String, Pattern> words, HashMap<String,Pattern> pattern, int size, HashMap<String,Pattern> patternNew) {
		
		for(String one : pattern.keySet()){
			
			for(String two : words.keySet()){
				
				if(one.compareTo(two)!= 0)
				{
					StringBuffer key = new StringBuffer(one + "_" + two); 
					Pattern onePost = pattern.get(one);
					List<Integer> onePosition = onePost.getPosition();
					Pattern twoPost = words.get(two);
					List<Integer> twoPosition = twoPost.getPosition();
					
					for(Integer onePos : onePosition){
						for(Integer twoPos : twoPosition){
							if((onePos-twoPos) == size){								
								Pattern pos = patternNew.get(key.toString());
								if(pos==null){
									pos = new Pattern();
									List<String> el = pos.getElements();
									List<String> element = onePost.getElements();
									for(String ele : element){
										el.add(ele);
									}
									el.add(two);
									List<Integer> posit = pos.getPosition();
									onePosition.remove(twoPos);
									posit.add(onePos);
									patternNew.put(new String(key.toString()), pos);
								}
								else{
									List<Integer> posit = pos.getPosition();
									posit.add(onePos);
								}
							}
						}
					}
				}
			}
		}
	}
			
	@SuppressWarnings("unused")
	private void print(HashMap<String,Pattern> pattern) {
		
		for(String key : pattern.keySet()){
			
			System.out.print(key + " : ");
								
			Pattern pos = pattern.get(key.toString());
			List<String> el = pos.getElements();
			List<Integer> posit = pos.getPosition();
			
			System.out.print(el.size() + " - ");
			
			for(Integer position : posit){
				System.out.print(position + ", ");
			}
			
			System.out.println();
		}		
	}

	public List<String> getInner() {
		return inner;
	}
	
}