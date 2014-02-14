package aron.sinoai.templatemaniac.scripting.model.transform;

import aron.sinoai.templatemaniac.scripting.common.ScriptExtensionFactoryBase;

public class ResultWrapperFactory extends ScriptExtensionFactoryBase<ResultWrapper> {

	public enum WrapperKind {
		KeyPatternFinder
	}
	
	public ResultWrapperFactory() {
		super();
	}

	@Override
	protected void createMapping() {
		registerClass("KeyPatternFinder", KeyPatternFinder.class);	
	}
	
}
