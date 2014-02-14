package aron.sinoai.templatemaniac.scripting.model;

public interface IterableDataSource extends DataSource {
	int getCurrentIndex();
	public boolean isEmpty();
	public boolean isAtBeginning();
	public boolean isAtEnd();
	
}
