package aron.sinoai.templatemaniac.scripting.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.log4j.Logger;

import bsh.Interpreter;
import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;
import aron.sinoai.templatemaniac.runtime.RuntimeManager;
import aron.sinoai.templatemaniac.scripting.model.dbextension.DatabaseContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.BacktrackingContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherItem;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.PatternTemplateKey;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.TemplateKey;
import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContext.TemplateKeyBase;
import aron.sinoai.templatemaniac.scripting.model.transform.ResultWrapperFactory;

public class ScriptingContext {
	static private final Logger LOGGER = Logger.getLogger(ScriptingContext.class);
	
	static public class Defaults {
		private String database;
		private String groupFile;
		private String bscFile;
		private BacktrackingContext matcherBacktrackingContext;
		private File currentScriptFile;
		
		public Defaults() {
		}

		public Defaults(String database, String groupFile, String bscFile, BacktrackingContext matcherBacktrackingContext, File currentScriptFile) {
			this.setDatabase(database);
			this.setGroupFile(groupFile);
			this.setMatcherBacktrackingContext(matcherBacktrackingContext);
			this.setCurrentScriptFile(currentScriptFile);
			this.setBscFile(bscFile);
		}

		public Defaults(Defaults right) {
			this.setDatabase(right.getDatabase());
			this.setGroupFile(right.getGroupFile());
			this.setMatcherBacktrackingContext(right.getMatcherBacktrackingContext());
			this.setCurrentScriptFile(right.getCurrentScriptFile());
			this.setBscFile(bscFile);
		}

		@Override
		public Defaults clone() {
			return new Defaults(this);
		}

		public void setDatabase(final String database) {
			this.database = database;
		}

		public String getDatabase() {
			return database;
		}

		public void setGroupFile(String groupFile) {
			this.groupFile = groupFile;
		}

		public String getGroupFile() {
			return groupFile;
		}

		public void setMatcherBacktrackingContext(
				BacktrackingContext matcherBacktrackingContext) {
			this.matcherBacktrackingContext = matcherBacktrackingContext;
		}

		public BacktrackingContext getMatcherBacktrackingContext() {
			return matcherBacktrackingContext;
		}

		public void setCurrentScriptFile(File currentScriptFile) {
			this.currentScriptFile = currentScriptFile;
		}

		public File getCurrentScriptFile() {
			return currentScriptFile;
		}

		public String getBscFile() {
			return bscFile;
		}

		public void setBscFile(String bscFile) {
			this.bscFile = bscFile;
		}
	}

	static public enum PrefixAddingMode {
		None
		,Short
		,Long
	}
	
	private static final PrefixAddingMode PREFIX_ADDING_MODE = PrefixAddingMode.None;
	
	private RuntimeManager runtimeManager;
	private Unmarshaller jaxbUnmarshaller;
	private Marshaller jaxbMarshaller;
	private File targetFolder;
	private Map<String, DataSource> all = new HashMap<String, DataSource>();
	private Map<String, StringTemplateGroup> stgs = new HashMap<String, StringTemplateGroup>();
	private Map<String, Interpreter> bscInterpreters = new HashMap<String, Interpreter>();
	
	private Map<String, ScriptingTemplate> templates = new HashMap<String, ScriptingTemplate>();
	private Map<String, byte[]> rawTemplates = new HashMap<String, byte[]>();
	
	
	//the default data-source is useful to have access to the all collection
	private DataSource defaultDataSource = new DataSource() {
		
		@Override
		public DataSource getParentDataSource() {
			return defaultDataSource;
		}

		//we will use this trough introspection
		public Map<String, DataSource> getAll() {
			return all;
		}

		@Override
		public Object getResult() {
			return null;
		}
	};
	
	private ResultWrapperFactory wrapperFactory = new ResultWrapperFactory();
	private DatabaseContext databaseContext = new DatabaseContext();
	private MatcherContext matcherContext = new MatcherContext();
	private Defaults defaults = new Defaults();
	private Map<TemplateKeyBase, Integer> recursivityLevelOfTemplates = new HashMap<TemplateKeyBase, Integer>();
	private List<TargetResource> targerResoucesToClose = new ArrayList<TargetResource>();
	private int currentSequenceNumber = 0;
	private int errorLevel = 0;

	public static final int LOW_MEMORY_TRESHOLD = 100000000;

	public ScriptingContext(RuntimeManager runtimeManager, Marshaller jaxbMarshaller, Unmarshaller jaxbUnmarshaller, File targetFolder) {
		this.runtimeManager = runtimeManager;
		this.jaxbMarshaller = jaxbMarshaller;
		this.jaxbUnmarshaller = jaxbUnmarshaller;
		this.targetFolder = targetFolder;
	}

	public ScriptingContext(ScriptingContext right) {
		this.runtimeManager = right.runtimeManager;
		this.jaxbMarshaller = right.jaxbMarshaller;
		this.jaxbUnmarshaller = right.jaxbUnmarshaller;
		this.targetFolder = right.targetFolder;
	}

	public DAOManagerBase fetchDatabase(String database) {
		if (database == null) {
			database = defaults.getDatabase();
		}
		
		final DAOManagerBase result = databaseContext.getDatabase(targetFolder, database).getDAOManager();
		
		return result;
	}

	public void setAll(Map<String, DataSource> all) {
		this.all = all;
	}

	public Map<String, DataSource> getAll() {
		return all;
	}

	public void setJaxbUnmarshaller(Unmarshaller jaxbUnmarshaller) {
		this.jaxbUnmarshaller = jaxbUnmarshaller;
	}

	public Unmarshaller getJaxbUnmarshaller() {
		return jaxbUnmarshaller;
	}

	public void setJaxbMarshaller(Marshaller jaxbMarshaller) {
		this.jaxbMarshaller = jaxbMarshaller;
	}

	public Marshaller getJaxbMarshaller() {
		return jaxbMarshaller;
	}

	public Map<String, StringTemplateGroup> getStgs() {
		return stgs;
	}
	
	public Map<String, Interpreter> getBscInterpreters() {
		return bscInterpreters;
	}

	public void registerToAll(DataSource dataSource, String name) {
		if (name != null && !name.isEmpty()) {
			DataSource existingEntry = all.get(name);
			if (existingEntry == null || existingEntry instanceof DataSourceAlias) {
				all.put(name, dataSource);
			} else {
				throw new RuntimeException(String.format("Datasource with the name '%s' already defined (this is allowed only in case of data-source-aliases)!", name));
			}
		}

	}

	public DataSource getDefaultDataSource() {
		return defaultDataSource;
	}

	public ResultWrapperFactory getWrapperFactory() {
		return wrapperFactory;
	}

	public String formatFileName(String fileName) {
		File result = new File(fileName);
		
		if (!result.isAbsolute() && targetFolder != null) {
			result = new File(targetFolder, fileName);
		}
		
		return result.getAbsolutePath();
	}

	public File getCurrentScriptFolder() {
		File result = null;
		
		final File currentScriptFile = defaults.getCurrentScriptFile();
		if (currentScriptFile != null) {
			result = currentScriptFile.getParentFile();
		}
		
		return result;
	}
	
	public String formatFileNameForScripts(String fileName) {
		File result = new File(fileName);
		
		final File scriptFolder = getCurrentScriptFolder();
		if (!result.isAbsolute() && scriptFolder != null) {
			result = new File(scriptFolder, fileName);
		}
		
		return result.getAbsolutePath();
	}

	public void setTargetFolder(File targetFolder) {
		this.targetFolder = targetFolder;
	}

	public File getTargetFolder() {
		return targetFolder;
	}

	public void setTemplates(Map<String, ScriptingTemplate> templates) {
		this.templates = templates;
	}

	public Map<String, ScriptingTemplate> getTemplates() {
		return templates;
	}
	
	public void registerToTemplates(ScriptingTemplate template, String name) {
		if (name != null && !name.isEmpty()) {
			if (!templates.containsKey(name)) {
				templates.put(name, template);
			} else {
				byte[] raw = makeRawTemplate(template);
				ScriptingTemplate existingTemplate = templates.get(name);
				byte[] rawExisting = getRawTemplate(existingTemplate);
				
				if (!Arrays.equals(raw, rawExisting)) {
					throw new RuntimeException(String.format("Template with the name '%s' already defined!", name));
				}
			}
		}
	}
	
	public int getRecursivityLevel(TemplateKeyBase templateKey) {
		Integer value = recursivityLevelOfTemplates.get(templateKey);
		
		return value == null ? 0 : value;
	}

	protected void setRecursivityLevel(TemplateKeyBase templateKey, int value) {
		if (value == 0) {
			recursivityLevelOfTemplates.remove(templateKey);
		} else {
			recursivityLevelOfTemplates.put(templateKey, value);
		}
	}
	
	public void incrementRecursivityLevel(TemplateKeyBase templateKey) {
		setRecursivityLevel(templateKey, getRecursivityLevel(templateKey) + 1);
	}
	
	public void decrementRecursivityLevel(TemplateKeyBase templateKey) {
		setRecursivityLevel(templateKey, getRecursivityLevel(templateKey) - 1);
	}
	
	public ScriptingItem fetchFromTemplate(TemplateKey templateKey) {
		final ScriptingItem result;

		String templateName = templateKey.getTemplateName();
		ScriptingTemplate template = templates.get(templateName);
		
		if (template != null && getRecursivityLevel(templateKey) <= template.getMaxRecursivityLevel()) {
			try {
				//using the marshall/unmarshall to clone objects
				 result = (ScriptingItem)jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(getRawTemplate(template)));
			} catch (JAXBException e) {
				throw new RuntimeException(e);
			}
		} else {
			result = null;
		}
		
		return result;
	}

	public ScriptingItem findPatternTemplate(PatternTemplateKey templateKey) {
		final ScriptingItem result;
		
		final String content = matcherContext.findPatternTemplateContent(templateKey);
		if (content != null) {
			result = fetchScriptingItem(content);
		} else {
			result = null;
		}
		
		return result;
	}
	
	public MatcherContext getMatcherContext() {
		return matcherContext;
	}

	public ScriptingItem fetchScriptingItem(final String content) {
		final ScriptingItem result;
		try {
			//we just unmarshall it from the content
			result = (ScriptingItem)jaxbUnmarshaller.unmarshal(new StringReader(content));
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public ScriptingItem registerAndFetchPatternTemplate(PatternTemplateKey id, String content) {
		matcherContext.putPatternTemplateContent(id, content);
		return fetchScriptingItem(content);
	}
	

	private byte[] getRawTemplate(ScriptingTemplate template) {
		String name = template.getName();
		byte[] raw = rawTemplates.get(name);
		if (raw == null) {
			raw = makeRawTemplate(template); 
			rawTemplates.put(name, raw);
		}
		return raw;
	}

	private byte[] makeRawScriptingItem(ScriptingItem item) {
		byte[] raw;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			jaxbMarshaller.marshal(item, buffer);
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		raw = buffer.toByteArray();
		return raw;
	}

	private byte[] makeRawTemplate(ScriptingTemplate template) {
		return makeRawScriptingItem(template.getItem());
	}

	public void applyPrefix(final ScriptingItem item, final String prefix) {
		
		if (PREFIX_ADDING_MODE != PrefixAddingMode.None) { 
			String currentPrefix = item.getCurrentPrefix();
			final String fullPrefix = (currentPrefix == null) ? prefix : currentPrefix + prefix; 
			
			item.visitAllFromTopToDown( new ScriptingItemVisitor() {
				
				@Override
				public void invoke(ScriptingItem piece) {
					String name = piece.getName();
					
					if (name != null) {
						switch (PREFIX_ADDING_MODE) {
						case Short: {
							piece.setName(name);
							break;
						}
						case Long: {
							piece.setName(fullPrefix + name);
							break;
						}
						default:
							throw new RuntimeException("Unsupported case!");
						}
					}
					
					piece.setCurrentPrefix(fullPrefix);
				}
			});
		}
	}

	public void setDefaults(Defaults defaults) {
		this.defaults = defaults;
	}

	public Defaults getDefaults() {
		return defaults;
	}

	public TargetResource getFileResource(File file, String encoding, boolean noLock) {
		return new FileResource(file, encoding, noLock);
	}

	public void autocloseTargetResourceAtTheEnd(TargetResource targetResource) {
		targerResoucesToClose.add(targetResource);
	}
	
	public void close() {
		for (TargetResource item : targerResoucesToClose) {
			item.close();
		}
		
		all.clear();
		templates.clear();
		rawTemplates.clear();
		stgs.clear();
		bscInterpreters.clear();
	}

    public void changeCurrentScriptFile(String fileName) {
		defaults.setCurrentScriptFile(new File(formatFileNameForScripts(fileName)));
	}
	public void deepEvict(MatcherItem piece) {
		piece.visitAllFromTopToDown(new ScriptingItemVisitor() {
			
			@Override
			public void invoke(ScriptingItem item) {
				if (item instanceof DataSource && item.getName() != null) {
					all.remove(item.getName());
				}
			}
		});
	}
	
	public int getNextSequenceNumber() {
		currentSequenceNumber++;
		return currentSequenceNumber;
	}

	public int getCurrentSequenceNumber() {
		
		return currentSequenceNumber;
	}
	
	public void resetErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	/***
	 * 
	 * Setting the errorLevel in case it is bigger than the current one
	 */
	public void setErrorLevel(int errorLevel) {
		if (errorLevel > this.errorLevel) {
			this.errorLevel = errorLevel;
		}
	}

	public int getErrorLevel() {
		return errorLevel;
	}

	public void setRuntimeManager(RuntimeManager runtimeManager) {
		this.runtimeManager = runtimeManager;
	}

	public RuntimeManager getRuntimeManager() {
		return runtimeManager;
	}
}
