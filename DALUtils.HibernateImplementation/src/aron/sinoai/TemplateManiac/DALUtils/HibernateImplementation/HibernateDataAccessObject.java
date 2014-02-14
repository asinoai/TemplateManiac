package aron.sinoai.templatemaniac.dalutils.hibernateimplementation;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import aron.sinoai.templatemaniac.dalutils.DataAccessObject;


public class HibernateDataAccessObject<T, TIDDataType extends Serializable>
		implements DataAccessObject<T, TIDDataType> {
	private final HibernateDAOManagerBase manager;
	private final Class<T> entityClass;

	public HibernateDataAccessObject(HibernateDAOManagerBase manager,
			Class<T> entityClass) {
		this.manager = manager;
		this.entityClass = entityClass;
	}

	protected Session getSession() {
		return manager.getSession();
	}

	public StatelessSession createStatelessSession() {
		return manager.createStatelessSession();
	}
	
	@SuppressWarnings("unchecked")
	public T getById(TIDDataType id, boolean shouldLock) {
		return (T) manager.getById(entityClass, id, shouldLock);
	}

	public void save(T entity) {
		manager.save(entity);
	}

	public void update(T entity) {
		manager.update(entity);
	}

	public void saveOrUpdate(T entity) {
		manager.saveOrUpdate(entity);
	}

	public void delete(T entity) {
		manager.delete(entity);
	}

	public void deleteAll() {
		manager.deleteAll(entityClass);
	}

	public void deleteById(TIDDataType id) {
		delete(getById(id, false));
	}

	@SuppressWarnings("unchecked")
	public List<T> getByCriterion(List<Criterion> criterionList) {
		final Session session = getSession();

		Criteria criteria = session.createCriteria(entityClass);

		if (criterionList != null) {
			for (Criterion criterion : criterionList) {
				criteria.add(criterion);
			}
		}

		return (List<T>)criteria.list();
	}

	public List<T> getAll() {
		return getByCriterion(null);
	}

	@SuppressWarnings("unchecked")
	public List<T> getByExample(T entity) {
		final Session session = getSession();
		Criteria criteria = session.createCriteria(entityClass);
		criteria.add(Example.create(entity));
		return (List<T>) criteria.list();
	}

	@Override
	public void evict(T entity) {
		manager.evict(entity);
	}
	
	@Override
	public void flush() {
		manager.flush();
	}
}
