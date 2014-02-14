package aron.sinoai.templatemaniac.scripting.model.dbextension.mock;

import java.io.Serializable;
import java.util.List;

import aron.sinoai.templatemaniac.dalutils.Connection;
import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;

public abstract class MockDAOManagerBase implements DAOManagerBase {
	public MockDAOManagerBase() {
	}
	
	@Override
	public Connection getConnection() {
		return null;
	}

	@Override
	public Object getById(Class<?> type, Serializable id, boolean shouldLock) {
		return null;
	}

	@Override
	public void saveOrUpdate(Object instance) {
	}

	@Override
	public void save(Object instance) {
	}

	@Override
	public void update(Object instance) {
	}

	@Override
	public void delete(Object instance) {
	}

	@Override
	public List<Object> listSQL(String query) {
		return null;
	}

	@Override
	public int execute(String command) {
		return 0;
	}

	@Override
	public int executeSQL(String command) {
		return 0;
	}

	@Override
	public Object beginTransaction() {
		return null;
	}

	@Override
	public void commitTransaction(Object handle) {
	}

	@Override
	public void rollbackTransaction(Object handle) {
	}

	@Override
	public void evict(Object o) {
	}

	@Override
	public void flush() {
	}

	@Override
	public void clear() {
	}

}
