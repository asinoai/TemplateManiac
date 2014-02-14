package aron.sinoai.templatemaniac.scripting.model.dbextension.mock;

import aron.sinoai.templatemaniac.scripting.util.FormatHelper;

public class Model {

	private String name;
	
	public Model() {
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

}
