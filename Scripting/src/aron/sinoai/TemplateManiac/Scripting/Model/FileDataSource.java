package aron.sinoai.templatemaniac.scripting.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.log4j.Logger;

@XmlRootElement(name = "file-data-source")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileDataSource extends DataSourceBase {
	static private final Logger LOGGER = Logger.getLogger(FileDataSource.class);

	@XmlAttribute
	private String fileNamePattern;
	
	@XmlAttribute
	private boolean byLines;

	@XmlAttribute
	private String encoding;

	@XmlTransient
	private BufferedReader reader;

	@XmlTransient
	private StringTemplate fileNameTemplate;
	
	@XmlTransient
	private ScriptingContext context;

	@XmlTransient
	private String result;

	
	public FileDataSource() {
	}
	
	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		fileNameTemplate = createStringTemplate(fileNamePattern, context, parent);
		
		this.context = context;
		
		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		prepareStringTemplateForDataSource(fileNameTemplate, getParentDataSource());
		
		
		File file = new File(context.formatFileName(fileNameTemplate.toString()));
		try {
			if (encoding != null) {
				final InputStream stream = new FileInputStream(file);
				final InputStreamReader inputReader = new InputStreamReader(stream, encoding);
			
				reader = new BufferedReader(inputReader);
			} else {
				reader = new BufferedReader(new FileReader(file));
			}
			
			try {
				if (byLines) {
					while ( (result = reader.readLine()) != null ) {
						super.execute();
					}
				} else {
					StringBuilder buffer = new StringBuilder();
					
					String line;
					String separator = "";
					while ( (line = reader.readLine()) != null ) {
						buffer.append(separator);
						buffer.append(line);
						if (separator.isEmpty()) {
							separator = "\r\n";
						}
					}
					
					result = buffer.toString();
					
					super.execute();
				}
			} finally {
					reader.close();
			}				
		} catch (IOException e) {
			LOGGER.error("FileDataSource error!", e);
		}
	}

	public void setByLines(boolean byLines) {
		this.byLines = byLines;
	}

	public boolean isByLines() {
		return byLines;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		context = null;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return encoding;
	}
}
