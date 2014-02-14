package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.HashMap;
import java.util.Map;

import aron.sinoai.templatemaniac.scripting.model.matcher.antrlextension.AntlrParserFactory;

public class MatcherContext {

	public static class TemplateKeyBase {
	}
	
	public static class PatternTemplateKey extends TemplateKeyBase {
		private String groupFile;
		private String templateExpression;
		
		public PatternTemplateKey(String groupFile, String templateExpression) {
			this.groupFile = groupFile;
			this.templateExpression = templateExpression;
		}
		
		@Override
		public boolean equals(Object object) {
			boolean result = object != null && object instanceof PatternTemplateKey; 
			
			if (result) {
				PatternTemplateKey right = (PatternTemplateKey) object;
				
				result = (groupFile != null && groupFile.equals(right.groupFile) || (groupFile == null && right.groupFile == null)) &&
						 (templateExpression != null && templateExpression.equals(right.templateExpression) || (templateExpression == null && right.templateExpression == null));
			}
			
			return result;
		}
		
		@Override
		public int hashCode() {
			return (groupFile == null ? 0 : groupFile.hashCode()) ^
				   (templateExpression == null ? 0 : templateExpression.hashCode());
		}
	}
	
	public static class TemplateKey extends TemplateKeyBase {
		private String templateName;
		
		public TemplateKey(String templateName) {
			this.templateName = templateName;
		}
		
		@Override
		public boolean equals(Object object) {
			boolean result = object != null && object instanceof TemplateKey; 
			
			if (result) {
				TemplateKey right = (TemplateKey) object;
				
				result = (templateName != null && templateName.equals(right.templateName) || (templateName == null && right.templateName == null));
			}
			
			return result;
		}
		
		@Override
		public int hashCode() {
			return (templateName == null ? 0 : templateName.hashCode());
		}

		public void setTemplateName(String templateName) {
			this.templateName = templateName;
		}

		public String getTemplateName() {
			return templateName;
		}
	}

	
	public static final int DEFAULT_MAX_RECURSIVITY_LEVEL = 5;
	
	private Map<PatternTemplateKey, String> patternTemplates = new HashMap<PatternTemplateKey, String>();

	private AntlrParserFactory factory = new AntlrParserFactory();
	
	public MatcherContext() {
	}

	public String findPatternTemplateContent(PatternTemplateKey id) {
		final String content = patternTemplates.get(id);
		return content;	
	}

	public void putPatternTemplateContent(PatternTemplateKey id, String content) {
		patternTemplates.put(id, content);
	}

	public AntlrParserFactory getFactory() {
		return factory;
	}
}
