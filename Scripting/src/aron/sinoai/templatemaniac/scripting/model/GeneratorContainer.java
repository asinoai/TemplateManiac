package aron.sinoai.templatemaniac.scripting.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import aron.sinoai.templatemaniac.scripting.model.matcher.MatcherContainer;

@XmlRootElement(name = "generator-container")
public class GeneratorContainer extends GeneratorItemBase {

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
		,@XmlElementRef( name = "result-transformer", type = ResultTansformer.class)
		,@XmlElementRef( name = "data-source-iterator", type = DataSourceIterator.class)
		,@XmlElementRef( name = "data-source-alias", type = DataSourceAlias.class)
		,@XmlElementRef( name = "content", type = Content.class)
		,@XmlElementRef( name = "dynamic-content", type = DynamicContent.class)
		,@XmlElementRef( name = "file-generator", type = FileGenerator.class)
		,@XmlElementRef( name = "list-generator", type = ListGenerator.class)
		,@XmlElementRef( name = "set-generator", type = SetGenerator.class)
		,@XmlElementRef( name = "map-generator", type = MapGenerator.class)
		,@XmlElementRef( name = "formatter", type = Formatter.class)
		,@XmlElementRef( name = "message", type = Message.class)
		,@XmlElementRef( name = "matcher-container", type = MatcherContainer.class )		
		,@XmlElementRef( name = "script-container", type = ScriptContainer.class)
		,@XmlElementRef( name = "include-script", type = IncludeScript.class)
		,@XmlElementRef( name = "include-dynamic-script", type = IncludeDynamicScript.class)
		,@XmlElementRef( name = "generator-container", type = GeneratorContainer.class)})
	@XmlMixed
	private ArrayList<GeneratorItem> items = new ArrayList<GeneratorItem>();
	
	@Override
	public void execute() {
		for (GeneratorItem item : items) {
			item.execute();
		}
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		super.compile(context, parent);
		
		for (GeneratorItem item : items) {
			item.compile(context, this);
		}
	}

	public ArrayList<GeneratorItem> getItems() {
		return items;
	}

	protected ArrayList<Object> getOthers() {
		return others;
	}

	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		super.visitChildrenFromTopToDown(visitor);
		
		for (GeneratorItem item : items) {
			item.visitAllFromTopToDown(visitor);
		}
	}
}
