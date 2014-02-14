package aron.sinoai.templatemaniac.scripting.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.PathGroupLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.StringTemplateGroupLoader;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import bsh.EvalError;
import bsh.Interpreter;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ScriptingItemBase implements ScriptingItem {

	private static final String DATASOURCE_TAG = "ds";

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String groupFile;
	
	@XmlTransient
	private DataSource dataSource;
	
	@XmlTransient
	private String currentPrefix;
	
	public ScriptingItemBase() {
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (parent != null) {
			dataSource = parent.getDataSource();
		} else {
			dataSource = context.getDefaultDataSource();
		}
	}
	
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}
	
	protected StringTemplate createStringTemplate(String pattern, ScriptingContext context, ScriptingItem parent) {
		StringTemplate result = null;
		
		String groupFile = this.groupFile;
		if (groupFile == null) {
			groupFile = context.getDefaults().getGroupFile();
		}
		
		if (groupFile != null) {
			StringTemplateGroup templateGroup = context.getStgs().get(groupFile);
			if (templateGroup == null) {
				try {
					Reader reader = new BufferedReader(new FileReader(context.formatFileNameForScripts(groupFile)));
					try {
						templateGroup = new StringTemplateGroup(reader, DefaultTemplateLexer.class);				
					} finally {
						reader.close();
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				
				context.getStgs().put(groupFile, templateGroup);
			}
			
			result = new StringTemplate(templateGroup, pattern);
		} else {
			result = new StringTemplate(pattern);
		}
		
		result.registerRenderer(String.class, new BasicFormatRenderer());
		
		return result;
	}

	private void prepareInterpreter(Interpreter interpreter) throws EvalError {
		interpreter.set(DATASOURCE_TAG, getDataSource());
	}

	protected Interpreter fetchBashInterpreter(ScriptingContext context) {
		final String bscFile = context.getDefaults().getBscFile();
		
		Interpreter interpreter;
		
		if (bscFile != null) {
			interpreter = context.getBscInterpreters().get(bscFile);
			if (interpreter == null) {
				interpreter = new Interpreter();
				try {
					interpreter.source(context.formatFileNameForScripts(bscFile));
					context.getBscInterpreters().put(bscFile, interpreter);
				} catch (IOException | EvalError e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			interpreter = new Interpreter();
		}
		return interpreter;
	}
	
	protected void executeBashScript(final String expression, final Interpreter interpreter) {
		
		try {
			prepareInterpreter(interpreter);
			interpreter.eval(expression);
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
	}

	
	protected Object evalBashScript(final String expression, final Interpreter interpreter) {
		
		final Object result;
		try {
			prepareInterpreter(interpreter);
			interpreter.eval(expression);
			result = interpreter.get("result");
		} catch (EvalError e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	protected void prepareStringTemplate(StringTemplate template) {
		prepareStringTemplateForDataSource(template, getDataSource());
	}

	protected void prepareStringTemplateForDataSource(StringTemplate template, DataSource datasource) {
		template.reset();
		template.setAttribute(DATASOURCE_TAG, datasource);
		template.setAttribute("ns", currentPrefix == null ? "" : currentPrefix);
	}
	
	public static void runScript(String fileName, ScriptingContext context) {
		ScriptContainer data = loadScript(fileName, context);
		
		if (data != null) {
			try {
				context.changeCurrentScriptFile(fileName);
				data.compile(context, null);
				data.execute();
			} finally {
				data.deepDispose();
			}
		}
	}
		
	public static ScriptContainer loadScript(String fileName, ScriptingContext context) {
		ScriptContainer result = null;
		
		if (fileName != null) {
			File file = new File(context.formatFileNameForScripts(fileName));
			
			if (file.exists()) {
				if (context.getTargetFolder() == null) {
					context.setTargetFolder(file.getParentFile());
				}
				
				StringTemplateGroupLoader stgLoader = new PathGroupLoader(context.getTargetFolder().getAbsolutePath(), null);
				StringTemplateGroup.registerGroupLoader(stgLoader);

				
				try {
					result = (ScriptContainer) context.getJaxbUnmarshaller().unmarshal(file);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			} else {
				throw new RuntimeException(String.format("Error opening the script file (\"%s\")!", file.getAbsoluteFile()));
			}
		}
		
		return result;
	}
	
	public static String linesToString(List<String> lines) {
		StringBuilder buffer = new StringBuilder();
		String separator = "";
		for (String item: lines) {
			buffer.append(separator);
			buffer.append(item);
			
			if  (separator.isEmpty()) {
				separator = "\n";
			}
		}
		return buffer.toString();
	}

	public static String linesToString4Windows(List<String> lines) {
		StringBuilder buffer = new StringBuilder();
		String separator = "";
		for (String item: lines) {
			buffer.append(separator);
			buffer.append(item);
			
			if  (separator.isEmpty()) {
				separator = "\r\n";
			}
		}
		return buffer.toString();
	}

	public void setGroupFile(String groupFile) {
		this.groupFile = groupFile;
	}

	public String getGroupFile() {
		return groupFile;
	}
	
	@Override
	public void visitAllFromTopToDown(ScriptingItemVisitor visitor) {
		visitor.invoke(this);
		
		visitChildrenFromTopToDown(visitor);
	}
	
	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
	}

	public void setCurrentPrefix(String currentPrefix) {
		this.currentPrefix = currentPrefix;
	}

	public String getCurrentPrefix() {
		return currentPrefix;
	}
	
	@Override
	public void dispose() {
		dataSource = null;
	}
	
	public void deepDispose() {
		visitAllFromTopToDown(new ScriptingItemVisitor() {
			
			@Override
			public void invoke(ScriptingItem item) {
				item.dispose();
			}
		});
	}
	
}
