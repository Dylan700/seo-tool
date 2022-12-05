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

    public Terminal(ConsoleView view, InputSystem input, CommandRegistry commands) {
        this.view = view;
        this.input = input;
        this.checker = new SEOChecker(view);
        this.commands = commands;
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
        } else if(commands.get(command.toLowerCase().split(" ")[0]) != null){
            commands.get(command.toLowerCase().split(" ")[0]).execute(command.split(" "), input, view, checker);
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
