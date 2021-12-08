package seo.tool;

import seo.tool.console.ConsoleInput;
import seo.tool.console.ConsoleViewDefault;
import seo.tool.console.Terminal;
import seo.tool.console.command.CommandRegistry;
import seo.tool.console.command.GenerateSitemapCommand;

public class App {
    public static void main(String[] args) {

        CommandRegistry commands = new CommandRegistry();
        commands.add("generate sitemap", new GenerateSitemapCommand());

        Terminal term = new Terminal(new ConsoleViewDefault(), new ConsoleInput(), commands);
        term.run();
    }
}
