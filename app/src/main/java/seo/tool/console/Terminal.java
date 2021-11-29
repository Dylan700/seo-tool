package seo.tool.console;

import java.util.List;

import seo.tool.checks.CheckResult;
import seo.tool.checks.SEOChecker;

public class Terminal {
    
    private ConsoleView view;
    private InputSystem input;
    private SEOChecker checker;

    public Terminal(ConsoleView view, InputSystem input) {
        this.view = view;
        this.input = input;
        this.checker = new SEOChecker();
    }

    public void run(){
        view.printWelcome();
        while(true){
            view.prompt();
            String command = input.getInput();
            if(!processCommand(command)){
                input.close();
                break;
            }
        }
	checker.quit();
    }

    /**
     * returns false if the user wants to quit
     * @return
     */
    public boolean processCommand(String command){
        if(command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit")){
            return false;
        }else if(command.equalsIgnoreCase("check all")){
            List<String> checks = checker.getChecks();
            for(String check : checks){
                runCheck(check);
            }
        }else if(command.equalsIgnoreCase("checks")){
            view.printChecks(checker.getChecks());
        }else if(command.toLowerCase().startsWith("load ")){
            view.printInfo("Loading url...");
            String url = command.substring(5);
            if(checker.load(url)){
                view.printInfo("URL loaded successfully.");
            }else{
                view.printError("Unable to load url.");
            }
        }else if(command.equalsIgnoreCase("url")){
            view.printInfo("Current url: " + checker.getURL());
        }else if(command.toLowerCase().startsWith("check ")){
            String check = command.substring(6);
            runCheck(check);
        }else{
            view.printError("Command not found.");
        }
        return true;
    }

    private void runCheck(String check){
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
                view.printInfo("Check for "+check+" passed.");
            }
        }else{
            view.printError("Check for "+check+" failed.");
            view.printError(result.getMessage());
        }
    }

}
