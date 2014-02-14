package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.PatternTemplateKey;

@SuppressWarnings("unused")
@XmlRootElement(name = "word-by-word")
@XmlAccessorType(XmlAccessType.FIELD)
public class WordByWord extends MatcherItemWithStringBase {
	
	private static final Pattern WHITE_SPACES = Pattern.compile("[ \\n\\r\\t]+");

	@XmlTransient
	private String[] words;
	
	public WordByWord() {
		super();
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		words = getTheString().split(" ");
	}

	protected boolean isAMatchOverride() {
		Source source = getSource();
		String theString = getTheString();

		boolean isMatch = source.isAValidPosition(source.getPos() + theString.length() - 1);
		
		if (isMatch) {
			final String sourceString = source.getTheString();
			
			Matcher matcher = WHITE_SPACES.matcher(sourceString);
			
			int position = source.getPos();
			int i = 0;
			
			final int length = sourceString.length();
			while (isMatch && i < words.length - 1 && position < length) {
				isMatch = matcher.find(position);
				
				if (isMatch) {
					String token = sourceString.substring(position, matcher.start());
					
					isMatch = token.equals(words[i]);
					if (isMatch) {
						position = matcher.end();
					}
				}
				
				i++;
			}
			
			isMatch = isMatch && (i == words.length - 1);
			
			if (isMatch) { 
				String lastWord = words[i];
				isMatch = source.isAValidPosition(position + lastWord.length() - 1)
					&& (lastWord.equals(sourceString.substring(position, position + lastWord.length()))) ;

				if (isMatch) {
					source.setPos(position + lastWord.length());
				}
			}
			
				
		}
		
		return isMatch;
	}
}
