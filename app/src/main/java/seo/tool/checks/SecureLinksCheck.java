package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;

import seo.tool.console.ConsoleView;

import org.openqa.selenium.By;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SecureLinksCheck implements SEOCheck{

    private ConsoleView view;

    public SecureLinksCheck(ConsoleView view){
        this.view = view;
    }

    @Override
    public CheckResult run(WebDriver driver) {
    	// get all links on the page
        List<WebElement> links = driver.findElements(By.tagName("a"));
        // set progress to 0
        view.setProgress(0);

	for(WebElement link : links) {
	    try{
		    // get the href attribute of the link
		    String href = link.getAttribute("href");
		    
		    // set progress
		    int linkIndex = links.indexOf(link);
		    view.setProgress(Math.max(1, (int) Math.ceil((double) linkIndex / links.size() * 100)), href);

		    // if the href is not null, check if it is secure
            if(href == null){
                continue;
            }
		    if(href.toLowerCase().startsWith("http") && !href.toLowerCase().contains("https")){
                view.setProgress(100);
				return new CheckResult(false, "Insecure link found: " + href);
            }
	    }catch(StaleElementReferenceException e){
		    // ignore if any elements are removed from the DOM
		    continue;
	    }
	}
        view.setProgress(100);
        return new CheckResult(true, "No insecure links found");
    }
}
