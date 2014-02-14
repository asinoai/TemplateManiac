package aron.sinoai.templatemaniac.scripting.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContainer;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "template")
@XmlAccessorType(XmlAccessType.FIELD)
public class ScriptingTemplate extends ScriptingItemBase {
	//contains the not mapped elements; keep this anyhow, because otherwise 
	//not mapped elements might arrive in the explicitly mapped mixed lists
	@XmlAnyElement
	@XmlMixed
	private ArrayList<Object> others;
	
	@XmlElementRefs( {
		@XmlElementRef( name= "matcher-container", type = MatcherContainer.class )
		,@XmlElementRef( name = "script-container", type = ScriptContainer.class) } )
	@XmlMixed
	private ScriptingItem item;
	
	@XmlAttribute
	private int maxRecursivityLevel = 5; 
	
	@XmlTransient
	private Defaults defaults;
	
	public ScriptingTemplate() {
		super();
	}
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		setDefaults(context.getDefaults().clone());
		
		//since this is a template, no need for compilation, just to register
		String name = getName();
		if (name == null) {
			throw new RuntimeException("Scripting template must have a name!");
		}
		
		context.registerToTemplates(this, getName());
	}

	@Override
	public void execute() {
		//does nothing, it's just a template
	}

	public void setItem(ScriptingItem item) {
		this.item = item;
	}

	public ScriptingItem getItem() {
		return item;
	}

	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		
		item.visitAllFromTopToDown(visitor);
	}

	public void setDefaults(Defaults defaults) {
		this.defaults = defaults;
	}

	public Defaults getDefaults() {
		return defaults;
	}

	public void setMaxRecursivityLevel(int maxRecursivityLevel) {
		this.maxRecursivityLevel = maxRecursivityLevel;
	}

	public int getMaxRecursivityLevel() {
		return maxRecursivityLevel;
	}

	
}
