package aron.sinoai.templatemaniac.scripting.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

@XmlRootElement(name = "data-source-iterator")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataSourceIterator extends DataSourceBase implements IterableDataSource {
	@XmlAttribute
	private String dsNamesPattern;
	
	@XmlTransient
	private Object result;
	
	@XmlTransient
	private int currentIndex = -1;
	
	@XmlTransient
	private Iterator<Object> iterator;

	@XmlTransient
	private StringTemplate templateDsNamesPattern;

	@XmlTransient
	private ScriptingContext context;

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		templateDsNamesPattern = createStringTemplate(dsNamesPattern, context, parent);
		this.context = context;
		
		super.compile(context, parent);
	}

	@Override
	public void dispose() {
		context = null;
		
		super.dispose();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		prepareStringTemplate(templateDsNamesPattern);
		String[] dsNames = templateDsNamesPattern.toString().split(" ");
		
		currentIndex = -1;
		for (String dsName: dsNames) {
			DataSource dataSource = context.getAll().get(dsName);
			if (dataSource == null) {
				throw new RuntimeException(String.format("DataSourceIterator (%s): Error, datasouce '%s' not found (multiple datasource names should be separated by space)!", getName(), dsName));
			}
			Object iterable = dataSource.getResult();
			
			final Collection<Object> collection; 
			if (iterable instanceof Map<?,?>) {
				collection = ((Map<Object, Object>)iterable).keySet();
			} else {
				collection = (Collection<Object>)iterable;
			}
	
			if (!collection.isEmpty()) {
				iterator = collection.iterator();
				while (iterator.hasNext()) {
					currentIndex++;
					result = iterator.next();
					super.execute();
				}
			}
		}
		
		//increment the index, since we want to have the item-count at the end of the iteration
		currentIndex++;
		iterator = null;
	}

	public Object getResult() {
		return result;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public boolean isEmpty() {
		return (currentIndex == 0);
	}

	public boolean isAtBeginning() {
		return (currentIndex == 0);
	}
	
	public boolean isAtEnd() {
		return (iterator == null) || !iterator.hasNext();
	}

}
