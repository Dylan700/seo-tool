package seo.tool.console.command;

import seo.tool.console.InputSystem;

import org.openqa.selenium.WebDriver;

import seo.tool.console.ConsoleView;

public interface Command {
    void execute(InputSystem input, ConsoleView view, WebDriver driver);
}
