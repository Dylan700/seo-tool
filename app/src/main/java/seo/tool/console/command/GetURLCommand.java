package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

public class GetURLCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
		view.printInfo("Current url: " + checker.getURL());
	}

	public String getDescription(){
		return "Get the current url loaded in the system.";
	}
}
