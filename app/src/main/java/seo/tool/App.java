package seo.tool;

import seo.tool.console.ConsoleInput;
import seo.tool.console.ConsoleView;
import seo.tool.console.ConsoleViewDefault;
import seo.tool.console.Terminal;
import seo.tool.console.command.*;

public class App {
    public static void main(String[] args) throws Exception {

		ConsoleView view = new ConsoleViewDefault();

        CommandRegistry commands = new CommandRegistry();
		commands.add("generate-sitemap", new GenerateSitemapCommand());
		commands.add("reload", new ReloadPageCommand());
		commands.add("checkall", new CheckAllCommand());
		commands.add("checks", new CheckInfoCommand());
		commands.add("check", new RunCheckCommand());
		commands.add("load", new LoadURLCommand());
		commands.add("url", new GetURLCommand());
		commands.add("show-meta", new ShowMetaCommand());
		
		try{
			commands.add("gconsole", new GoogleConsoleCommand());
		}catch(Exception e){
			view.printError("Unable to load Google Console commands." + e.getMessage());
		}

		Terminal term = new Terminal(view, new ConsoleInput(), commands);
        term.run();
    }
}
