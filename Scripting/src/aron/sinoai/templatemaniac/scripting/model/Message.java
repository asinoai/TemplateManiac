package aron.sinoai.templatemaniac.scripting.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.log4j.Logger;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message extends ScriptingItemBase implements GeneratorItem {
	static private final Logger LOGGER = Logger.getLogger(Message.class);

	static enum Kind {
		Debug
		,Info
		,Warning
		,Error
		,Fatal
	}
	
	@XmlAttribute
	private Kind kind = Kind.Info;
	
	@XmlAttribute
	private String textPattern;
	
	@XmlTransient
	private StringTemplate textTemplate;
	
	@XmlTransient
	private GeneratorTarget target;
	
	@XmlTransient
	private ScriptingContext context;

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		textTemplate = createStringTemplate(textPattern, context, parent);

		if (parent != null && parent instanceof GeneratorItem) {
			this.target = ((GeneratorItem)parent).getTarget();
		}
		
		this.context = context;
		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		prepareStringTemplate(textTemplate);
		
		final String text = textTemplate.toString();
		switch (kind) {
		case Debug:
			LOGGER.debug(text);
			break;
		case Info:
			LOGGER.info(text);
			break;
		case Warning:
			LOGGER.warn(text);
			break;
		case Error:
			context.setErrorLevel(1);
			LOGGER.error(text);
			break;
		case Fatal:
			context.setErrorLevel(2);
			LOGGER.fatal(text);
			throw new RuntimeException(text);

		default:
			throw new RuntimeException("Not supported case!");
		}
	}

	public void setTextPattern(String textPattern) {
		this.textPattern = textPattern;
	}

	public String getTextPattern() {
		return textPattern;
	}

	@Override
	public GeneratorTarget getTarget() {
		return target;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		target = null;
		context = null;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public Kind getKind() {
		return kind;
	}

	public void setContext(ScriptingContext context) {
		this.context = context;
	}

	public ScriptingContext getContext() {
		return context;
	}

}
