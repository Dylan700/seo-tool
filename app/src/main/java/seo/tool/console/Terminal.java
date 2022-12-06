package seo.tool.console;

import java.util.List;
import java.util.Map;

import seo.tool.checks.CheckResult;
import seo.tool.checks.SEOChecker;
import seo.tool.console.command.CommandRegistry;
import seo.tool.console.command.Command;

public class Terminal {
    
    private ConsoleView view;
    private InputSystem input;
    private SEOChecker checker;
    private CommandRegistry commands;

    private boolean hasQuit = false;

    public Terminal(ConsoleView view, InputSystem input, CommandRegistry commands) {
        this.view = view;
        this.input = input;
        this.checker = new SEOChecker(view, input);
        this.commands = commands;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            quit();
        }));
    }

    public void run(){
        view.printWelcome();
        view.startLoading("Initializing");
        checker.init();
        view.endLoading();
        while(true){
            view.prompt();
            String command = input.getInput();
            if(!processCommand(command)){
                break;
            }
        }
        quit();

    }

    private synchronized void quit(){
        if(hasQuit){
            return;
        }
        hasQuit = true;
        view.endLoading();
        view.startLoading("Exiting");
        checker.quit();
        view.endLoading();
        view.printInfo("Goodbye!");
        input.close();
    }

    /**
     * returns false if the user wants to quit
     * @return
     */
    public boolean processCommand(String command){
        if(command.equalsIgnoreCase("quit") || command.equalsIgnoreCase("exit")){
            return false;
        } else if(commands.get(command.toLowerCase().split(" ")[0]) != null){
		try{
                	commands.get(command.toLowerCase().split(" ")[0]).execute(command.split(" "), input, view, checker);
		}catch(Exception e){
			view.setProgress(100);
			view.endLoading();
			view.printError(String.format("Unable to execute command: %s", e.getMessage()));
		}
            return true;
	}else if(command.equalsIgnoreCase("help")){
		for(Map.Entry<String, Command> commandEntry: commands.getAll()){
			view.printInfo(String.format("%s - %s", commandEntry.getKey(), commandEntry.getValue().getDescription()));
		}
        }else{
            view.printError("Command not found.");
        }
        return true;
    }

}
