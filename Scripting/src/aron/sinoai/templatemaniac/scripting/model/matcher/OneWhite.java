package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "one-white")
@XmlAccessorType(XmlAccessType.FIELD)
public class OneWhite extends CharInString {
	public OneWhite() {
		super();
		setStringItem(" \t\r\n");
	}
}
