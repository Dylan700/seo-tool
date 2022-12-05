package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

public class ReloadPageCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
		checker.refresh();
		view.printInfo("Page refreshed.");
		view.printInfo(checker.getURL());
	}

	public String getDescription(){
		return "Reload the current URL/page.";
	}
}
