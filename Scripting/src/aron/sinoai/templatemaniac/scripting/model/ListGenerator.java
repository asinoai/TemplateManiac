package aron.sinoai.templatemaniac.scripting.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "list-generator")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListGenerator extends DataGeneratorBase {

	@XmlAttribute
	private boolean splitByNewLines = true;

	@XmlTransient
	private List<Object> result;
	
	public ListGenerator() {
	}
	
	@Override
	public void write(String key, String value) {
		if (splitByNewLines) {
			String[] lines = value.split("\\r\\n");
			for(String line : lines) {
				result.add(line);
			}
		} else {
			result.add(value);
		}
	}
	
	@Override
	public void write(String key, Collection<?> value) {
		result.add(value);
	}

	@Override
	public void write(String key, Map<?, ?> value) {
		result.add(value);
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		result = new ArrayList<Object>();
		
		super.compile(context, parent);
	}
	
	@Override
	protected void doExecute() {
		GeneratorTarget target = getParentTarget();
		if (target != null) {
			//making a copy
			List<Object> copy = new ArrayList<Object>();
			copy.addAll(result);
			
			target.write(getKey(), copy);
			
			result.clear();
		}
	}
	public void setResult(List<Object> result) {
		this.result = result;
	}

	public List<Object> getResult() {
		return result;
	}

	@Override
	protected void clearResult() {
		if (result != null) {
			result.clear();
		}
	}

}
