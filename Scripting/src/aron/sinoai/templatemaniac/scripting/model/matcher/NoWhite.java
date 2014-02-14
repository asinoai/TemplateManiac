package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "no-white")
@XmlAccessorType(XmlAccessType.FIELD)
public class NoWhite extends CharNotInString {
	public NoWhite() {
		super();
		setStringItem(" \t\r\n");
	}
}
