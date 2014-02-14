package aron.sinoai.templatemaniac.scripting.model;

import aron.sinoai.templatemaniac.scripting.model.matcher.Disposable;

public interface ScriptingItem extends Disposable {
	String getName();
	void setName(String name);
	
	String getCurrentPrefix();
	void setCurrentPrefix(String prefix);
	
	DataSource getDataSource();
	String getGroupFile();

	void compile(ScriptingContext context, ScriptingItem parent);
	void execute();
	
	void visitChildrenFromTopToDown(ScriptingItemVisitor visitor);
	void visitAllFromTopToDown(ScriptingItemVisitor visitor);
	
	void deepDispose();
}
