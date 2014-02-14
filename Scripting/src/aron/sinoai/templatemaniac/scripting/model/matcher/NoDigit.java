package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "no-digit")
@XmlAccessorType(XmlAccessType.FIELD)
public class NoDigit extends CharNotInString {
	public NoDigit() {
		super();
		setStringItem("1234567890");
	}
}
