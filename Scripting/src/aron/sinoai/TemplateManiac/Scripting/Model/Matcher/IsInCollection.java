package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.DataSource;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;

@XmlRootElement(name = "is-in-collection")
@XmlAccessorType(XmlAccessType.FIELD)
public class IsInCollection extends MatcherItemWithStringBase {

	public static enum SourceType {
		DataSourceName
		,Content
	}
	
	@XmlAttribute
	private
	SourceType sourceType = SourceType.DataSourceName;
	
	@XmlAttribute
	private boolean ignoreCase = false;
	
	@XmlAttribute
	private boolean untilEndOfParsing = false;
	
	@XmlTransient
	Collection<String> collection;
	
	public IsInCollection() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		switch (sourceType) {
		case DataSourceName:
			DataSource ds = context.getAll().get(getTheString());
			
			if (ds == null) {
				throw new RuntimeException(String.format("IsInCollection '%s': the theString '%s' must be valid ds (datasouce)!", getName(), getTheString()));
			}
			
			Object result = ds.getResult();
			if (result instanceof Map) { 
				Map<String, ?> map = (Map<String, ?>)result;
				collection = map.keySet();
			} else {
				collection = (Collection<String>)result;
			}
			break;

		case Content:
			collection = getStringItems();
			break;

		default:
			throw new RuntimeException("Not supported case!");
		}
	}
	
	protected boolean isAMatchOverride() {
		Source source = getSource();
		
		final boolean result;
		
		if (untilEndOfParsing)
		{
			String toSearch = source.getParseString();
			result = collection.contains(toSearch);
			if (result)
			{
				source.lastPos();
			}
		}
		else
		{
			int maxLength = -1;
			for(String item: collection) {
				int currentLength = item.length();
				
				boolean isMatch = source.isAValidPosition(source.getPos() + currentLength - 1);
				
				if (isMatch) {
					if (ignoreCase) {
						isMatch = item.equalsIgnoreCase(source.getParseString(currentLength));
					} else {
						isMatch = item.equals(source.getParseString(currentLength));
					}
				}
				
				if (isMatch && maxLength < currentLength) {
					maxLength = currentLength;
				}
			}		
	
			result = maxLength != -1;
			if (result) {
				source.incPos(maxLength);
			}
		}
		
		return result;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public SourceType getSourceType() {
		return sourceType;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}
	

	public void setUntilEndOfParsing(boolean untilEndOfParsing) {
		this.untilEndOfParsing = untilEndOfParsing;
	}

	public boolean isUntilEndOfParsing() {
		return untilEndOfParsing;
	}
	
}
