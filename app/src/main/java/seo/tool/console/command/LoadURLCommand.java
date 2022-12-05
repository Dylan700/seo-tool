package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

public class LoadURLCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
		view.printInfo("Enter a URL");
		view.prompt();
		String url = input.getInput();
		view.startLoading("Loading");
		if(checker.load(url)){
			view.endLoading();
			view.printInfo("URL loaded successfully.");
		}else{
			view.endLoading();
			view.printError("Unable to load url.");
		}
	}

	public String getDescription(){
		return "Load the a new URL into the system.";
	}
}
