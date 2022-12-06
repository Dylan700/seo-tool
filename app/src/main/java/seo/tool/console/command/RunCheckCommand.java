package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;
import seo.tool.checks.CheckResult;

import java.util.Arrays;
import java.util.stream.Collectors;

public class RunCheckCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
		String check = Arrays.asList(args).stream().skip(1).collect(Collectors.joining(" "));
		runCheck(check, checker, view);
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
			view.printInfo(result.getMessage());
		    }else{
			view.printSuccess("Check for "+check+" passed.");
		    }
		}else{
		    view.printError("Check for "+check+" failed.");
		    view.printError(result.getMessage());
		}
	}

	public String getDescription(){
		return "Run a single check.";
	}
}


