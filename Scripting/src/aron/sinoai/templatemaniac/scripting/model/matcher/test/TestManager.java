package aron.sinoai.templatemaniac.scripting.model.matcher.test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;

public class TestManager {

	private static final TestManager instance;
	private static final Logger LOGGER = Logger.getLogger(CharInStringTest.class);
	
	public static String TESTSCRIPTS_FOLDER = "scripts/test";

	static {
		instance = new TestManager();
	}

	private TestManager() {
	}

	public static TestManager get() {
		return instance;
	}
	
	public ScriptingContext createScriptingContext(){

    	Marshaller jaxbMarshaller;
    	Unmarshaller jaxbUnmarshaller;	
    	ScriptingContext context = null;
    	
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance("aron.sinoai.templatemaniac.scripting.model");
			jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
	    	context = new ScriptingContext(null, jaxbMarshaller, jaxbUnmarshaller, null);
	    	
		} catch (JAXBException e) {
			LOGGER.fatal(e);
		}	
		return context;
	}
}

