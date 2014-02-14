package aron.sinoai.templatemaniac.scripting.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "map-generator")
@XmlAccessorType(XmlAccessType.FIELD)
public class MapGenerator extends DataGeneratorBase {

	public static enum DuplicateHandling {
		No
		,KeepLast
		,MakeList
		,MakeSet
		,MergeMap
	}
	
	@XmlAttribute
	private DuplicateHandling duplicate = DuplicateHandling.KeepLast;
	
	@XmlTransient
	private Map<String, Object> result;

	public MapGenerator() {
	}
	
	@Override
	public void write(String key, String value) {
		putIntoResult(key, value);
	}

	@Override
	public void write(String key, Map<?, ?> value) {
		putIntoResult(key, value);
	}

	@Override
	public void write(String key, Collection<?> value) {
		putIntoResult(key, value);
	}
	
	@SuppressWarnings("unchecked")
	protected void putIntoResult(String key, Object value) {
		switch (duplicate) {
		case KeepLast: {
			result.put(key, value);
			break;
		}
		case No: {
			Object oldValue = result.put(key, value);
			if (oldValue != null) {
				throw new RuntimeException(String.format("MapGenerator '%s': duplicate key '%s'!", getName(), key));
			}
			break;
		}
		case MakeList:
		case MakeSet: {
			final Collection<Object> collection;

			Object collectionAsObject = result.get(key);
			if (collectionAsObject == null) {
				switch (duplicate) {
					case MakeList: {
						collection = new ArrayList<Object>();
						break;
					}
					case MakeSet: {
						collection = new HashSet<Object>();
						break;
					}
					default: {
						throw new RuntimeException("Not supported case!");
					}
				}
				
				result.put(key, collection);
			} else {
				collection = (Collection<Object>)collectionAsObject;
			}
			
			collection.add(value);
			break;
		}
		case MergeMap: {

			Object mapAsObject = result.get(key);
			if (mapAsObject == null) {
				result.put(key, value);
			} else {
				//merging elements from the new map into the existing one
				if (mapAsObject instanceof Map<?,?>) {
					Map<String, Object> existingMap = (Map<String, Object>)mapAsObject;
					Map<String, Object> newMap = (Map<String, Object>)value;
					for (String newKey : newMap.keySet()) {
						final Object newValueAsObject = newMap.get(newKey);
						if (newValueAsObject instanceof Collection<?>) {
							final Collection<Object> newValue = (Collection<Object>)newValueAsObject;
							Object existingValueAsObject = existingMap.get(newKey);
							if (existingValueAsObject == null) {
								existingMap.put(newKey, newValue);
							} else {
								final Collection<Object> existingValue = (Collection<Object>)existingValueAsObject;
								existingValue.addAll(newValue);
							}
						} else {
							throw new RuntimeException(String.format("MapGenerator '%s': values must be maps with lists or sets (use duplicate='MakeList' or 'MakeSet' in the deepest nested submap and duplicate='MergeMap' for the other nested maps), in case of duplicate='MergeMap'!", getName(), key));
						}
					}
				} else {
					throw new RuntimeException(String.format("MapGenerator '%s': values must be maps with lists (use duplicate='MakeList' in the deepest nested submap and duplicate='MergeMap' for the other nested maps), in case of duplicate='MergeMap'!", getName(), key));
				}
			}
			
			break;
		}
		default:
			throw new RuntimeException("Not implemented!");
		}
		
	}

	@Override
	public void compile(ScriptingContext context, ScriptingItem parent) {
		result = new TreeMap<String, Object>();
		
		super.compile(context, parent);
	}
	
	@Override
	protected void doExecute() {
		GeneratorTarget target = getParentTarget();
		if (target != null) {
			//making a copy
			Map<String, Object> copy = new TreeMap<String, Object>();
			copy.putAll(result);
			
			target.write(getKey(), copy);

			//reseting the result (freeing up resources)
			result.clear();
		}
	}

	@Override
	public Map<String, Object> getResult() {
		return result;
	}

	public void setDuplicate(DuplicateHandling duplicate) {
		this.duplicate = duplicate;
	}

	public DuplicateHandling getDuplicate() {
		return duplicate;
	}

	@Override
	protected void clearResult() {
		if (result != null) {
			result.clear();
		}
	}

}
