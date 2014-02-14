package aron.sinoai.templatemaniac.scripting.model;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

public abstract class QueryDataSource extends DataSourceBase implements IterableDataSource {
	static private final Logger LOGGER = Logger.getLogger(QueryDataSource.class);

	@XmlAttribute
	private String queryPattern;
	
	@XmlAttribute
	private boolean iterative = true;
	
	@XmlAttribute
	private String database;

	@XmlTransient
	private Object result;
	
	@XmlTransient
	private int currentIndex = -1;
	
	@XmlTransient
	private Iterator<Object> iterator;

	@XmlTransient
	private StringTemplate templateQueryPattern;

	@XmlTransient
	private DAOManagerBase parserDatabase;

	@XmlTransient
	private ScriptingContext context;
	
	@XmlTransient
	private Defaults defaults;

	
	public void setQueryPattern(String queryPattern) {
		this.queryPattern = queryPattern;
	}

	public String getQueryPattern() {
		return queryPattern;
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		templateQueryPattern = createStringTemplate(queryPattern, context, parent);
		
		if (getItems().size()==0 && iterative) {
			LOGGER.warn(String.format("The query datasource '%s' has no children, consider using iterative='false'!", getName()));
		}
		
		this.context = context;
		this.defaults = context.getDefaults();
		
		super.compile(context, parent);
	}

	abstract protected List<Object> query(DAOManagerBase dao, String queryText);

	@Override
	public void execute() {
		if (parserDatabase == null) {
			Defaults oldDefaults = context.getDefaults();
			try {
				context.setDefaults(defaults);
				parserDatabase = context.fetchDatabase(database);
			} finally {
				context.setDefaults(oldDefaults);
			}
		}

		prepareStringTemplate(templateQueryPattern);
		String queryString = templateQueryPattern.toString();
		final List<Object> list; 
		try {
			list = query(parserDatabase, queryString);
		} catch (RuntimeException e) {
			LOGGER.error(String.format("QueryDatasouce '%s' : Error executing the query: '%s'!", getName(), queryString));
			throw e;
		}


		if (isIterative()) {
			currentIndex = -1;

			if (!list.isEmpty()) {
				iterator = list.iterator();
				while (iterator.hasNext()) {
					currentIndex++;
					result = iterator.next();
					super.execute();
				}
			}
			
			//increment the index, since we want to have the item-count at the end of the iteration
			currentIndex++;
			iterator = null;
		} else {
			currentIndex = 0;
			result = list;
			super.execute();
		}
	}

	public Object getResult() {
		return result;
	}

	public void setIterative(boolean iterative) {
		this.iterative = iterative;
	}

	public boolean isIterative() {
		return iterative;
	}

	public void setDatabase(final String database) {
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	@SuppressWarnings("unchecked")
	public boolean isEmpty() {
		return iterative ? (currentIndex == 0) : ((List<Object>)result).isEmpty();
	}

	public boolean isAtBeginning() {
		return !iterative || (currentIndex == 0);
	}
	
	public boolean isAtEnd() {
		return (iterator == null) || !iterator.hasNext();
	}

	@Override
	public void dispose() {
		context = null;
		defaults = null;
		parserDatabase = null;
		
		super.dispose();
	}

	public void setContext(ScriptingContext context) {
		this.context = context;
	}

	public ScriptingContext getContext() {
		return context;
	}

	public void setDefaults(Defaults defaults) {
		this.defaults = defaults;
	}

	public Defaults getDefaults() {
		return defaults;
	}
}
