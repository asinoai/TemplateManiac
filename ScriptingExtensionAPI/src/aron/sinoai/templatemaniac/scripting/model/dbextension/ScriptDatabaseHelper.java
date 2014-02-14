package aron.sinoai.templatemaniac.scripting.model.dbextension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public abstract class ScriptDatabaseHelper {
	
	public static interface DataFetchClosure {
		public void invoke(Object dataChunk)  throws JAXBException;
	}
	
	public static void loadData(Unmarshaller jaxbUnmarshaller, File file, DataFetchClosure onDataFetch) throws JAXBException {
        
		Pattern pattern =  Pattern.compile("^(.*)\\.([^\\.]*)?$") ;
        Matcher matcher =  pattern.matcher(file.toString());

		
        if (matcher.find()) {
        	
        	String fileNameBase = matcher.group(1);
			String extension = matcher.group(2);
			
			int i = 0;
			while (true) {
				final StringBuilder currentFileName = new StringBuilder(); ;											
				
				if (i==0) {
					currentFileName.append(fileNameBase);
					currentFileName.append(".");
					currentFileName.append(extension);
					
				} else {
					currentFileName.append(fileNameBase);
					currentFileName.append(".part");
					currentFileName.append(String.format("%03d", i));
					currentFileName.append(".");
					currentFileName.append(extension);
				}
				
				i++;
				
				File currentFile = new File(currentFileName.toString());
				
				if ( currentFile.exists() ) {
					try {
						// create a parser
						SAXParserFactory  factory = SAXParserFactory.newInstance();
						factory.setNamespaceAware(true);
						factory.setXIncludeAware(true);
						SAXParser parser = factory.newSAXParser();
						XMLReader reader = parser.getXMLReader();
					
						final InputSource inputSource = new InputSource(new FileReader(currentFile));
						final String path = currentFile.getAbsolutePath();
						inputSource.setSystemId(path);

						final SAXSource source = new SAXSource( reader, inputSource );
						
						final Object dataChunk = jaxbUnmarshaller.unmarshal(source);
						onDataFetch.invoke(dataChunk);
						
					} catch (SAXException e) {
						throw new RuntimeException(e);
					} catch (ParserConfigurationException e) {
						throw new RuntimeException(e);
					} catch (FileNotFoundException e) {
						throw new RuntimeException(e);
					}
					
				} else {
					break;
				}
				
			}
			
        }
	}
}
