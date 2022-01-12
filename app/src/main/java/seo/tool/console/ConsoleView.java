package seo.tool.console;

import java.util.List;

public interface ConsoleView {
    
    // static constants for ANSI codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_HIDE_CURSOR = "\u001B[?25l";
    public static final String ANSI_SHOW_CURSOR = "\u001B[?25h";
    public static final String ANSI_CLEAR_LINE = "\33[2K\r";

    public void printInfo(String message);

    public void printError(String message);

    public void printWelcome();

    public void prompt();

    public void printChecks(List<String> checks);

    public void setProgress(int progress);

}
