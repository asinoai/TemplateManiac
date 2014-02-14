package aron.sinoai.templatemaniac.main;

public class Main {
	public static void main(String[] args) {
		
		MainManager mainManager = MainManager.get();
		
		mainManager.setCommandLineArguments(args);
		mainManager.startUp();
		System.exit(mainManager.getScriptingManager().getErrorLevel());
	}

}
