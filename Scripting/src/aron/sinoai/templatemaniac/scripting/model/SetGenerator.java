package aron.sinoai.templatemaniac.scripting.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "set-generator")
@XmlAccessorType(XmlAccessType.FIELD)
public class SetGenerator extends DataGeneratorBase {

	static public enum DataKind {
		HashSet
		,TreeSet
	};
	
	
	@XmlAttribute
	private DataKind kind = DataKind.HashSet;

	@XmlAttribute
	private boolean splitByNewLines = true;
	
	@XmlTransient
	private Set<Object> result;
	
	public SetGenerator() {
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
		result = createSet();
		
		super.compile(context, parent);
	}
	
	@Override
	protected void doExecute() {
		GeneratorTarget target = getParentTarget();
		if (target != null) {
			//making a copy
			Set<Object> copy = createSet();
			copy.addAll(result);
			
			target.write(getKey(), copy);
			
			//reseting the result (freeing up resources)
			result.clear();
		}
	}

	private Set<Object> createSet() {
		
		final Set<Object> set;
		
		switch (kind) {
		case HashSet:
		{
			set = new HashSet<Object>();
			break;
		}
		case TreeSet:
		{
			set = new TreeSet<Object>();
			break;
		}
		default:
			throw new RuntimeException("Not supported case!");
		}
		return set;
	}
	public void setResult(Set<Object> result) {
		this.result = result;
	}

	public Set<Object> getResult() {
		return result;
	}

	@Override
	protected void clearResult() {
		if (result != null) {
			result.clear();
		}
	}

	public void setKind(DataKind kind) {
		this.kind = kind;
	}

	public DataKind getKind() {
		return kind;
	}


}
