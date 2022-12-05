package seo.tool;

import seo.tool.console.ConsoleInput;
import seo.tool.console.ConsoleViewDefault;
import seo.tool.console.Terminal;
import seo.tool.console.command.CommandRegistry;
import seo.tool.console.command.*;

public class App {
    public static void main(String[] args) {

        CommandRegistry commands = new CommandRegistry();
        commands.add("generate sitemap", new GenerateSitemapCommand());
	commands.add("reload", new ReloadPageCommand());
	commands.add("checkall", new CheckAllCommand());
	commands.add("checks", new CheckInfoCommand());
	commands.add("check", new RunCheckCommand());
	commands.add("load", new LoadURLCommand());
	commands.add("url", new GetURLCommand());

        Terminal term = new Terminal(new ConsoleViewDefault(), new ConsoleInput(), commands);
        term.run();
    }
}
