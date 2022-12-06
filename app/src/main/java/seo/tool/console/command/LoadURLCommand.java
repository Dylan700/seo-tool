package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

public class LoadURLCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
		if(args.length != 2){
			view.printInfo("Usage: load <url>");
			return;
		}
		String url = args[1];
		view.startLoading("Loading");
		if(checker.load(url)){
			view.endLoading();
			view.printSuccess("URL loaded successfully.");
		}else{
			view.endLoading();
			view.printError("Unable to load URL.");
		}
	}

	public String getDescription(){
		return "Load the a new URL into the system.";
	}
}
