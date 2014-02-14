package aron.sinoai.templatemaniac.scripting.model.dbextension.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;
import aron.sinoai.templatemaniac.scripting.model.dbextension.ScriptDatabase;

public class CustomDAODatabase implements ScriptDatabase {

	private DAOManagerBase database = new MockDAOManagerBase()
	{
		@Override
		public List<Object> list(String query) {
			final List<Object> result;
			
			if (query.equals("from MainData")) {
				result = new ArrayList<Object>();
				{
					MainData item = new MainData();
					item.setName("customDAO");
					
					result.add(item);
				}
			} else if (query.equals("from Model")) {
				result = new ArrayList<Object>();
				{
					Model item = new Model();
					item.setName("MainData");
					
					result.add(item);
				}
				{
					Model item = new Model();
					item.setName("Model");
					
					result.add(item);
				}
			} else {
				result = null;
			}
			
	 		return result;
		}
	};

	public CustomDAODatabase() {
		super();
	}
	

	@Override
	public DAOManagerBase getDAOManager() {
		return database;
	}


	@Override
	public void init(File folder) {
	}

}
