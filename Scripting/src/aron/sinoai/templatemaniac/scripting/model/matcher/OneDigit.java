package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "one-digit")
@XmlAccessorType(XmlAccessType.FIELD)
public class OneDigit extends CharInString {
	public OneDigit() {
		super();
		setStringItem("01234567890");
	}
}
