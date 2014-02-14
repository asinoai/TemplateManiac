package aron.sinoai.templatemaniac.dalutils;

public interface Connection {

	void forceOpenCurrentSession();
	void closeCurrentSession();
	
    void recreateSchema();
    void upgradeSchema();

}
