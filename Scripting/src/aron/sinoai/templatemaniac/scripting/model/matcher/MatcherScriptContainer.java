package aron.sinoai.templatemaniac.scripting.model.matcher;

import javax.xml.bind.annotation.XmlTransient;

import aron.sinoai.templatemaniac.scripting.model.GeneratorItem;
import aron.sinoai.templatemaniac.scripting.model.GeneratorTarget;
import aron.sinoai.templatemaniac.scripting.model.ScriptContainerBase;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;

public class MatcherScriptContainer extends ScriptContainerBase implements GeneratorItem {
	
	@XmlTransient
	private GeneratorTarget target;

	public MatcherScriptContainer() {
	}
	
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (parent != null && parent instanceof GeneratorItem) {
			if (parent instanceof GeneratorTarget) {
				//it is more natural to inherit the parent target in case of generator target
				this.target = ((GeneratorTarget)parent).getParentTarget();
			} else {
				this.target = ((GeneratorItem)parent).getTarget();
			}
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
