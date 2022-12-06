package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;
import org.openqa.selenium.By;

public class ShowMetaCommand implements Command {
	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
		String description = null;
		String title = null;
		try{
			description = checker.getDriver().findElement(By.xpath("//meta[@name='description']")).getAttribute("content");
			title = checker.getDriver().findElement(By.xpath("(//meta[@name='title']) | (//meta[@property='og:title'])[1]")).getAttribute("content");
		}catch(Exception e){
			view.printError("Unable to find meta information.");
			view.printError("Seems the URL does not contain both a meta title and a description.");
		}
		view.printInfo(String.format("Meta title: %s", title));
		view.printInfo(String.format("Meta description: %s", description));
	}

	public String getDescription(){
		return "Display meta information for a given page.";
	}
}
