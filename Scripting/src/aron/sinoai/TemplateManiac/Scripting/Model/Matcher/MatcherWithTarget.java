package aron.sinoai.templatemaniac.scripting.model.matcher;

import java.util.Collection;
import java.util.Map;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import aron.sinoai.templatemaniac.scripting.model.GeneratorTarget;
import aron.sinoai.templatemaniac.scripting.model.GeneratorTargetBase;
import aron.sinoai.templatemaniac.scripting.model.ListGenerator;
import aron.sinoai.templatemaniac.scripting.model.MapGenerator;
import aron.sinoai.templatemaniac.scripting.model.ScriptingContext;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItem;
import aron.sinoai.templatemaniac.scripting.model.ScriptingItemVisitor;

@XmlRootElement(name = "matcher-with-target")
public class MatcherWithTarget extends MatcherContainmentBase implements GeneratorTarget {
	
	
	@XmlElementRefs( {
		@XmlElementRef( name = "list-generator", type = ListGenerator.class )
		,@XmlElementRef( name = "map-generator", type = MapGenerator.class )} )
	@XmlMixed
	private GeneratorTargetBase innerTarget;
	
	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		if (innerTarget == null) {
			ListGenerator generator = new ListGenerator();
			generator.setInheritTarget(false);
			generator.setClearOnExecution(true);
			
			innerTarget = generator;
		}
		
		innerTarget.compile(context, parent);
		super.compile(context, parent);
	}
	
	@Override
	public void execute() {
		innerTarget.execute();
		super.execute();
	}
	
	@Override
	public void visitChildrenFromTopToDown(ScriptingItemVisitor visitor) {
		innerTarget.visitAllFromTopToDown(visitor);
		super.visitChildrenFromTopToDown(visitor);
	}

	@Override
	protected boolean isAMatchOverride() {
		return getMatchPiece().isAMatch();
	}

	@Override
	public GeneratorTarget getParentTarget() {
		return super.getTarget();
	}

	@Override
	public GeneratorTarget getTarget() {
		return this;
	}
	
	@Override
	public void write(String key, String value) {
		innerTarget.write(key, value);
	}

	@Override
	public void write(String key, Map<?, ?> value) {
		innerTarget.write(key, value);
	}

	@Override
	public void write(String key, Collection<?> value) {
		innerTarget.write(key, value);
	}

	public void setInnerTarget(GeneratorTargetBase innerTarget) {
		this.innerTarget = innerTarget;
	}

	public GeneratorTargetBase getInnerTarget() {
		return innerTarget;
	}

}
