package aron.sinoai.templatemaniac.scripting.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public abstract class XmlFormatter {

	public static String prittyPrintXML(String xmlString) throws Exception {
		String result = xmlString;

		if (!result.isEmpty()) {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		    /* - Specifies that the parsers created by this factory must
			 *	eliminate whitespace in element content (sometimes known loosely
			 *	as 'ignorable whitespace') when parsing XML documents
			 *		
			 *	- You must have a DTD or XML schema and validation enabled (TODO: create DTD/XML schema)
			 */
			// documentBuilderFactory.setIgnoringElementContentWhitespace(true);

			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

			InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
			Document document = documentBuilder.parse(inputStream);

			OutputStream outputStream = new ByteArrayOutputStream();
			XmlFormatter.serialize(document, outputStream);

			result = outputStream.toString();
		}

		return result;
	}

	public static void serialize(Document doc, OutputStream out) throws Exception {
		TransformerFactory tfactory = TransformerFactory.newInstance();
		Transformer serializer;
		try {
			serializer = tfactory.newTransformer();
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "8");

			DOMSource xmlSource = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(out);
			serializer.transform(xmlSource, outputTarget);
		} catch (TransformerException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}