package seo.tool.console;

import java.util.List;

public class ConsoleViewDefault implements ConsoleView {

    @Override
    public void printInfo(String message) {
        System.out.printf("%s[i]%s %s\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET, message);
    }

    @Override
    public void printWelcome(){
        System.out.printf("Welcome to the SEO Tool\n\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET);
    }

    @Override
    public void printError(String message) {
        System.out.printf("%s[x]%s %s\n", ConsoleView.ANSI_RED, ConsoleView.ANSI_RESET, message);
    }

    @Override
    public void prompt(){
        System.out.printf("%s>%s ", ConsoleView.ANSI_YELLOW, ConsoleView.ANSI_RESET);
    }

    @Override
    public void printChecks(List<String> checks){
        System.out.printf("%s[i]%s %d checks found.\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET, checks.size());
        for(int i = 0; i < checks.size(); i++){
            System.out.printf("\t%d. %s\n", i+1, checks.get(i));
        }

    }
    
}
