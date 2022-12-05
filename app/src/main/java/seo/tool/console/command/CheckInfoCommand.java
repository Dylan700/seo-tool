package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

public class CheckInfoCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
            view.printChecks(checker.getChecks());
	}

	public String getDescription(){
		return "Get a list of checks in the system.";
	}
}
