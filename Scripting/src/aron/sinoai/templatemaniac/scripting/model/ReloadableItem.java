package aron.sinoai.templatemaniac.scripting.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.scripting.model.DataSource;
import aron.sinoai.templatemaniac.scripting.model.GeneratorItem;
import aron.sinoai.templatemaniac.scripting.model.GeneratorTarget;
import aron.sinoai.templatemaniac.scripting.model.ScriptContainer;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItemBase;

@XmlRootElement(name = "reloadable-item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReloadableItem extends ScriptingItemBase implements GeneratorItem, DataSource {
	static private final Logger LOGGER = Logger.getLogger(ReloadableItem.class);

	@XmlElement(name = "script-container")
	private ScriptContainer scriptContainer;
	
	@XmlTransient
	private GeneratorTarget target;

	@XmlTransient
	private Map<String, DataSource> all;

	@XmlTransient
	private ScriptingContext context;

	@XmlTransient
	ScriptingContext localContext;

	@XmlTransient
	ScriptContainer localScriptContainer;

	
	public ReloadableItem() {
		super();
	}

	@Override
	public void compile(final ScriptingContext context, ScriptingItem parent) {
		if (parent != null && parent instanceof GeneratorItem) {
			this.target = ((GeneratorItem)parent).getTarget();
		}
		
		context.registerToAll(this, getName());
		
		this.context = context;

		super.compile(context, parent);
	}

	@Override
	public void execute() {
		boolean memoryShortageException;
		int retries = 10;
		do {
			memoryShortageException = false;
			
			if (localScriptContainer == null) {
				localContext = new ScriptingContext(context);
				localContext.setDefaults(context.getDefaults().clone());

				Marshaller jaxbMarshaller = context.getJaxbMarshaller();
				Unmarshaller jaxbUnmarshaller = context.getJaxbUnmarshaller();
	
				//copying (sharing) the all map 
				localContext.getAll().putAll(context.getAll());
				//and templates
				localContext.getTemplates().putAll(context.getTemplates());
				
				//linking the all to the local context
				all = localContext.getAll();
				
				
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				try {
					jaxbMarshaller.marshal(scriptContainer, buffer);
					localScriptContainer = (ScriptContainer) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(buffer.toByteArray()));
				} catch (JAXBException e) {
					throw new RuntimeException(e);
				}
	
				localScriptContainer.compile(localContext, this);
			}
			
			try {
				localScriptContainer.execute();
			} catch (ScriptingMemoryShortage e) {
				memoryShortageException = true;
			}
			
			final Runtime runtime = Runtime.getRuntime();
			long memory = runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory();
			if (memoryShortageException || memory < ScriptingContext.LOW_MEMORY_TRESHOLD) {
				//we flush (dereference) the localContext and the localScriptContainer to gain memory resources
				localContext.close();
				localContext = null;
	
				localScriptContainer.deepDispose();
				localScriptContainer = null;
				
				all = null;
				
				System.gc();
			}
			
			retries--;
		} while (memoryShortageException && retries >= 0);
		
		if (memoryShortageException) {
			LOGGER.error("Retry limit execeeded!");
		}
	}
	
	@Override
	public Object getResult() {
		DataSource parentDataSource = getParentDataSource();
		return parentDataSource == null ? null : parentDataSource.getResult();
	}

	@Override
	public DataSource getDataSource() {
		return this;
	}

	@Override
	public DataSource getParentDataSource() {
		return super.getDataSource();
	}

	public Map<String, DataSource> getAll() {
		return all;
	}

	public void setScriptContainer(ScriptContainer scriptContainer) {
		this.scriptContainer = scriptContainer;
	}

	public ScriptContainer getScriptContainer() {
		return scriptContainer;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		all = null;
	}

	@Override
	public GeneratorTarget getTarget() {
		return target;
	}
	
}
