package aron.sinoai.templatemaniac.dalutils;

import java.io.Serializable;
import java.util.List;

public interface DAOManagerBase 
{

    Connection getConnection();
    
	Object getById(Class<?> type, Serializable id, boolean shouldLock); 
    void saveOrUpdate(Object instance);
    void save(Object instance);
    void update(Object instance);
    void delete(Object instance);
    
    List<Object> list(String query);
	List<Object> listSQL(String query);
    int execute(String command);
    int executeSQL(String command);

    Object beginTransaction();
    void commitTransaction(Object handle);
    void rollbackTransaction(Object handle);

	void evict(Object o);
	void flush();
	void clear();
    
    
}
