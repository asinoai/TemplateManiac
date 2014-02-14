package aron.sinoai.templatemaniac.dalutils;

import java.util.List;

public interface DataAccessObject<T, TIDDataType> 
{
    T getById(TIDDataType id, boolean shouldLock);

    List<T> getAll();

    List<T> getByExample(T exampleInstance);


    void saveOrUpdate(T entity);
    void save(T entity);
    void update(T entity);

    void delete(T entity);
    void deleteById(TIDDataType id);
    void deleteAll();

    void evict(T entity);

	void flush();
}
