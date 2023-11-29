package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.openqa.selenium.WebDriver;

public class ScrapeEmailsCommand implements Command {

	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
        WebDriver driver = checker.getDriver();
        view.startLoading("Finding emails");
        List<String> emails = extractEmails(driver.getPageSource());
        view.endLoading();
        if(emails.size() > 0){
            view.printSuccess("Success!");
            for(String email : emails){
                view.printInfo(email);
            }
        }else{
            view.printError("No emails were found.");
        }
	}

    private List<String> extractEmails(String html){
        List<String> emails = new ArrayList<String>();
        Pattern p = Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(html);
        while(matcher.find()) {
            if(!emails.contains(matcher.group()))
                emails.add(matcher.group());
        }
        return emails;
    }

	public String getDescription(){
		return "Scrape emails from a given page. Use --all to spider across the same site or --limit to limit the number of pages.";
	}
}

