package aron.sinoai.templatemaniac.scripting.model;

import java.io.File;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.log4j.Logger;

@XmlRootElement(name = "directory-data-source")
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectoryDataSource extends DataSourceBase {
	static private final Logger LOGGER = Logger.getLogger(DirectoryDataSource.class);

	@XmlAttribute
	private String fileNamePattern;
	
	@XmlAttribute
	private boolean iterative = true;

	@XmlTransient
	private StringTemplate fileNameTemplate;
	
	@XmlTransient
	private ScriptingContext context;

	@XmlTransient
	private Object result;

	
	public DirectoryDataSource() {
	}
	
	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		fileNameTemplate = createStringTemplate(fileNamePattern, context, parent);
		
		this.context = context;

		if (getItems().size()==0 && iterative) {
			LOGGER.warn(String.format("The directory datasource '%s' has no children, consider using iterative='false'!", getName()));
		}
		
		
		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		prepareStringTemplateForDataSource(fileNameTemplate, getParentDataSource());

		final File file = new File(context.formatFileName(fileNameTemplate.toString()));
		
		final File[] fileList = file.listFiles();
		
		if (iterative) {
			if (fileList != null) {
				for(File fileItem : fileList) {
					result = fileItem;
					super.execute();
				}
			}
		} else {
			result = fileList;
			super.execute();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		
		context = null;
	}

	public void setIterative(boolean iterative) {
		this.iterative = iterative;
	}

	public boolean isIterative() {
		return iterative;
	}

}
