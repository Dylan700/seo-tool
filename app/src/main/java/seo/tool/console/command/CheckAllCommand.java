package seo.tool.console.command;

import seo.tool.console.InputSystem;
import seo.tool.console.ConsoleView;
import seo.tool.checks.SEOChecker;
import seo.tool.checks.CheckResult;

public class CheckAllCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
		for(String check: checker.getChecks()){
			runCheck(check, checker, view);
		}
	}

	private void runCheck(String check, SEOChecker checker, ConsoleView view){
		if(!checker.hasCheck(check)){
		    view.printError("No check with name \"" + check + "\" found.");
		    view.printInfo("Type \"checks\" to get a list of available checks.");
		    return;
		}

		view.printInfo("Checking " + check + "...");
		CheckResult result = checker.check(check);

		if(result.isSuccessful()){
		    if(result.getMessage() != null){
			view.printSuccess(result.getMessage());
		    }else{
			view.printSuccess("Check for "+check+" passed.");
		    }
		}else{
		    view.printError("Check for "+check+" failed.");
			if(result.getMessage() != null){
		    	view.printError(result.getMessage());
			}
		}
	}

	public String getDescription(){
		return "Run all checks.";
	}
}

