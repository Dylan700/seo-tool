package seo.tool;

import seo.tool.console.ConsoleInput;
import seo.tool.console.ConsoleViewDefault;
import seo.tool.console.Terminal;

public class App {
    public static void main(String[] args) {

        Terminal term = new Terminal(new ConsoleViewDefault(), new ConsoleInput());
        term.run();
    }
}
