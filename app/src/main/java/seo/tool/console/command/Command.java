package seo.tool.console.command;

import seo.tool.console.InputSystem;
import seo.tool.console.ConsoleView;
import seo.tool.checks.SEOChecker;

public interface Command {
    void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker);
    String getDescription();
}
