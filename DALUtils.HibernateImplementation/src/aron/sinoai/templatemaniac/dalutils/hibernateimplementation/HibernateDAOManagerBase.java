package aron.sinoai.templatemaniac.dalutils.hibernateimplementation;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.transform.Transformers;

import aron.sinoai.templatemaniac.dalutils.Connection;
import aron.sinoai.templatemaniac.dalutils.DAOManagerBase;

public class HibernateDAOManagerBase implements DAOManagerBase {
	private HibernateConnection connection;

	public HibernateDAOManagerBase(String configurationFile,
			Properties extraProperties) {
		try {
			AnnotationConfiguration configuration = new AnnotationConfiguration();

			if (extraProperties != null) {
				configuration.addProperties(extraProperties);
			}

			configuration.configure(configurationFile);
			connection = new HibernateConnection(configuration);
		} catch (Throwable t) {
			System.out.println(t);
		}
	}

	public HibernateDAOManagerBase(String configurationFile,
			java.sql.Connection externalConnection, String schema) {
		try {
			AnnotationConfiguration configuration = new AnnotationConfiguration();
			
			configuration.configure(configurationFile);
			configuration.setProperty("hibernate.default_schema", schema);
			connection = new HibernateConnection(configuration, externalConnection);
		} catch (Throwable t) {
			System.out.println(t);
		}
	}
	
	public Session getSession() {
		return connection.getCurrentSession();
	}

	public StatelessSession createStatelessSession() {
		return connection.createStatelessSession();
	}
	
	public Connection getConnection() {
		return connection;
	}

	public void saveOrUpdate(Object entity) {
		final Session session = getSession();

		session.saveOrUpdate(entity);
	}

	public void save(Object entity) {
		final Session session = getSession();

		session.save(entity);
	}

	public void update(Object entity) {
		final Session session = getSession();

		session.update(entity);
	}

	public void delete(Object entity) {
		final Session session = getSession();

		session.delete(entity);
	}

	public void deleteAll(Class<?> entityClass) {
		String command = "delete from " + entityClass.getName();

		execute(command);
	}

	public Object getById(Class<?> type, Serializable id, boolean shouldLock) {
		final Session session = getSession();

		Object entity = null;
		try {
			if (shouldLock) {
				entity = session.get(type, id, LockMode.UPGRADE);
			} else {
				entity = session.get(type, id);
			}
		} catch (ObjectNotFoundException e) {
		}

		return entity;
	}

	public void evict(Object o) {
		final Session session = getSession();

		session.evict(o);
	}

	public void flush() {
		final Session session = getSession();

		session.flush();
	}

	public Object beginTransaction() {
		final Session session = getSession();

		return session.beginTransaction();
	}

	public void commitTransaction(Object handle) {
		Transaction transaction = (Transaction) handle;

		transaction.commit();
	}

	public void rollbackTransaction(Object handle) {
		Transaction transaction = (Transaction) handle;

		transaction.rollback();
	}

	public int execute(String command) {
		final Session session = getSession();

		Query query = session.createQuery(command);

		int result = query.executeUpdate();

		return result;
	}

	public int executeSQL(String command) {
		final Session session = getSession();

		SQLQuery query = session.createSQLQuery(command);

		int result = query.executeUpdate();

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Object> list(String command) {
		final Session session = getSession();

		Query myQuery = session.createQuery(command);

		return myQuery.list();
	}

	@SuppressWarnings("unchecked")
	public List<Object> listSQL(String command) {
		final Session session = getSession();

		Query myQuery = session.createSQLQuery(command).
		  setResultTransformer( Transformers.ALIAS_TO_ENTITY_MAP );

		return myQuery.list();
	}

	@Override
	public void clear() {
		final Session session = getSession();
		
		session.clear();
	}
}
