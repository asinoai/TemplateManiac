package aron.sinoai.templatemaniac.scripting.model;

public abstract class GeneratorTargetBase extends GeneratorContainer implements GeneratorTarget{

	@Override
	public GeneratorTargetBase getTarget() {
		return this;
	}

	@Override
	public GeneratorTarget getParentTarget() {
		return super.getTarget();
	}

}
