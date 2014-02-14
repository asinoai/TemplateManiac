package aron.sinoai.templatemaniac.scripting.model.matcher.antrlextension;

import aron.sinoai.templatemaniac.scripting.common.ScriptExtensionFactoryBase;


public class AntlrParserFactory extends ScriptExtensionFactoryBase<AntlrParser> {
	public AntlrParserFactory(){
		super();
	}

	@Override
	protected void createMapping() {
		registerClass("Sample", SampleAntrlMatcher.class);
	}
	

}
