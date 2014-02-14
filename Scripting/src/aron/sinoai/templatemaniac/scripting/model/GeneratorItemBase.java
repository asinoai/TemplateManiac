package aron.sinoai.templatemaniac.scripting.model;

import javax.xml.bind.annotation.XmlTransient;

public abstract class GeneratorItemBase extends ScriptingItemBase implements GeneratorItem {

	@XmlTransient
	private GeneratorTarget target;
	
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (parent != null && parent instanceof GeneratorItem) {
			this.target = ((GeneratorItem)parent).getTarget();
		}

		super.compile(context, parent);
	}

	@Override
	public GeneratorTarget getTarget() {
		return target;
	}

	@Override
	public void dispose() {
		super.dispose();
		
		target = null;
	}
}
