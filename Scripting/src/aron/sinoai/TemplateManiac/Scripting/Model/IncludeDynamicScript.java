package aron.sinoai.templatemaniac.scripting.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.antlr.stringtemplate.StringTemplate;

import aron.sinoai.templatemaniac.scripting.model.ScriptingContext.Defaults;

@XmlRootElement(name = "include-dynamic-script")
@XmlAccessorType(XmlAccessType.FIELD)
public class IncludeDynamicScript extends ScriptingItemBase implements GeneratorItem {

	@XmlAttribute
	private String contentPattern;

	@XmlTransient
	private ScriptingItem subItem;
	
	@XmlTransient
	private GeneratorTarget target;

	@XmlTransient
	private Defaults defaults;
	
	@XmlTransient
	private ScriptingContext context;
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (parent != null && parent instanceof GeneratorItem) {
			this.target = ((GeneratorItem)parent).getTarget();
		}
		
		defaults = context.getDefaults();
		this.context = context;

		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		if (subItem == null) {
			Defaults oldDefaults = context.getDefaults();
			try {
				context.setDefaults(defaults);
				StringTemplate template = createStringTemplate(contentPattern, context, this);
				prepareStringTemplate(template);
				String content = template.toString();
		
				subItem = context.fetchScriptingItem(content);
		
				subItem.compile(context, this);
			} finally {
				context.setDefaults(oldDefaults);
			}
		}
		
		subItem.execute();
	}

	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		if (subItem != null) {
			subItem.visitAllFromTopToDown(visitor);
		}
	}

	@Override
	public GeneratorTarget getTarget() {
		return target;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		target = null;
		context = null;
	}
	
}
