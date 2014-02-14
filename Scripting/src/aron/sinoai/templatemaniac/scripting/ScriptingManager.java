package aron.sinoai.templatemaniac.scripting;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.runtime.RuntimeManager;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItemBase;


public class ScriptingManager {
	static private final Logger LOGGER = Logger.getLogger(ScriptingManager.class);
	
	private RuntimeManager runtimeManager;
	private Marshaller jaxbMarshaller;
	private Unmarshaller jaxbUnmarshaller;
	private int errorLevel = 0;

	public ScriptingManager() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("aron.sinoai.templatemaniac.scripting.model");
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			LOGGER.fatal(e);
		}
	}

	public void setRuntimeManager(RuntimeManager runtimeManager) {
		this.runtimeManager = runtimeManager;
	}

	public RuntimeManager getRuntimeManager() {
		return runtimeManager;
	}

	public void open(String fileName) {
		if (fileName != null) {
			long startTime = System.currentTimeMillis();
			LOGGER.info(String.format("Starting to run the script file '%s'", fileName));
			
			ScriptingContext context = new ScriptingContext(runtimeManager, jaxbMarshaller, jaxbUnmarshaller, null);
			try {
				context.setErrorLevel(errorLevel);
				ScriptingItemBase.runScript(fileName, context);
			} finally {
				setErrorLevel(context.getErrorLevel());
				context.close();
			}

			long elapsed = ((System.currentTimeMillis() - startTime) / 1000); //seconds
			long elapsedMinutes = elapsed / 60;
			long elapsedRemainingSeconds = elapsed % 60;
			LOGGER.info(String.format("Finished running the script file '%s' (in %d minute(s) and %d second(s))", fileName, elapsedMinutes, elapsedRemainingSeconds));
		}
	}

	public void resetErrorLevel(int errorLevel) {
		this.errorLevel = errorLevel;
	}

	/***
	 * 
	 * Setting the errorLevel in case it is bigger than the current one
	 */
	protected void setErrorLevel(int errorLevel) {
		if (errorLevel > this.errorLevel) {
			this.errorLevel = errorLevel;
		}
	}
	public int getErrorLevel() {
		return errorLevel;
	}

}
