package aron.sinoai.templatemaniac.scripting.model.dbextension.mock;

import aron.sinoai.templatemaniac.scripting.util.FormatHelper;

public class MainData {
	private String name;
	private String nameSpace;
	
	public MainData() {
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getNameCammelCase() {
		return FormatHelper.toUpperCammelCase(name);
	}


	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public String getNameSpace() {
		return nameSpace;
	}
}
