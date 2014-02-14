package aron.sinoai.templatemaniac.scripting.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContainer;


public class ScriptContainerBase extends ScriptingItemBase{
	//contains the not mapped elements; keep this anyhow, because otherwise 
	//not mapped elements might arrive in the explicitly mapped mixed lists
	@XmlAnyElement
	@XmlMixed
	private ArrayList<Object> others;
	
	@XmlElementRefs( {
		@XmlElementRef( name = "hql-data-source", type = HqlDataSource.class)
		,@XmlElementRef( name = "sql-data-source", type = SqlDataSource.class)
		,@XmlElementRef( name = "file-data-source", type = FileDataSource.class)
		,@XmlElementRef( name = "directory-data-source", type = DirectoryDataSource.class)
		,@XmlElementRef( name = "data-source-iterator", type = DataSourceIterator.class)
		,@XmlElementRef( name = "data-source-alias", type = DataSourceAlias.class)
		,@XmlElementRef( name = "context-data-source", type = ContextDataSource.class)
		,@XmlElementRef( name = "external-script", type = ExternalScript.class)
		,@XmlElementRef( name = "include-script", type = IncludeScript.class)
		,@XmlElementRef( name = "include-dynamic-script", type = IncludeDynamicScript.class)
		,@XmlElementRef( name = "dynamic-extension", type = DynamicExtension.class)
		,@XmlElementRef( name = "result-transformer", type = ResultTansformer.class)
		,@XmlElementRef( name = "formatter", type = Formatter.class)
		,@XmlElementRef( name = "message", type = Message.class)
		,@XmlElementRef( name = "reloadable-item", type = ReloadableItem.class)
		,@XmlElementRef( name = "template", type = ScriptingTemplate.class)
		,@XmlElementRef( name = "matcher-container", type = MatcherContainer.class )		
		,@XmlElementRef( name = "script-container", type = ScriptContainer.class)
		,@XmlElementRef( name = "generator-container", type = GeneratorContainer.class)})
	@XmlMixed
	private ArrayList<ScriptingItem> items = new ArrayList<ScriptingItem>();
	
	public ScriptContainerBase() {
	}

	public ArrayList<ScriptingItem> getItems() {
		return items;
	}

	@Override
	public void execute() {
		for (ScriptingItem item : items) {
			item.execute();
		}
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		for (ScriptingItem item : items) {
			item.compile(context, this);
		}
	}

	protected ArrayList<Object> getOthers() {
		return others;
	}

	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		
		for (ScriptingItem item : items) {
			item.visitAllFromTopToDown(visitor);
		}
	}
}
