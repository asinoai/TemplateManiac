package aron.sinoai.templatemaniac.dalutils.hibernateimplementation;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import aron.sinoai.templatemaniac.dalutils.Connection;



public class HibernateConnection implements Connection {

    private final Configuration configuration;
    private final SessionFactory sessionFactory;
    private final java.sql.Connection externalConnection;

    private Session currentSession = null; 

    public HibernateConnection(Configuration configuration) {
        this.configuration = configuration;
        sessionFactory = configuration.buildSessionFactory();
        externalConnection = null;
    }

    public HibernateConnection(Configuration configuration, java.sql.Connection connection) {
        this.configuration = configuration;
        sessionFactory = configuration.buildSessionFactory();
        externalConnection = connection;
    }
    
    private Session createSession() {
    	Session session;
    	if (externalConnection != null){
    		session = sessionFactory.openSession(externalConnection);
    	}
    	else session = sessionFactory.openSession();
        
        return session;
    }

    //implementing the interface Connection  
    
    public Session getCurrentSession() {
        if (currentSession == null) {
            currentSession = createSession();
        }

        return currentSession;
    }

    public StatelessSession createStatelessSession() {
    	StatelessSession session;
	    if (externalConnection != null){
			session = sessionFactory.openStatelessSession(externalConnection);
		}
		else session = sessionFactory.openStatelessSession();
        
        return session;
    }
    
    public void recreateSchema()
    {
        SchemaExport exporter = new SchemaExport(configuration);
        //create script also includes drop statements, so no need to drop it
        exporter.create(false, true);
    }
    
    public void upgradeSchema()
    {
        SchemaUpdate updater = new SchemaUpdate(configuration);
        updater.execute(true, true);
    }

    public void closeCurrentSession() {
        if (currentSession != null) {
            currentSession.flush();
            currentSession.close();
            currentSession.disconnect();
            
            currentSession = null;
        }
	}

	@Override
	public void forceOpenCurrentSession() {
		getCurrentSession();
	}
    
}
