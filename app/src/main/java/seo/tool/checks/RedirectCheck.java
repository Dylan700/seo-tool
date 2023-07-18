package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;

import seo.tool.console.ConsoleView;

import org.openqa.selenium.By;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

public class RedirectCheck implements SEOCheck{

    private ConsoleView view;
    private List<String> redirectVariations = new ArrayList<String>(4);

    public RedirectCheck(ConsoleView view){
        this.view = view;
        redirectVariations.add("http://");
        redirectVariations.add("http://www.");
        redirectVariations.add("https://");
        redirectVariations.add("https://www.");
    }

    @Override
    public CheckResult run(WebDriver driver) {
        String originalUrl = driver.getCurrentUrl();

        // get the domain of the url
        String domain = originalUrl.substring(originalUrl.indexOf("//") + 2, originalUrl.indexOf("/", originalUrl.indexOf("//") + 2));
        
        // remove www. from the domain if needed
        if(domain.startsWith("www.")){
            domain = domain.substring(4);
        }

        view.startLoading("Getting redirect URL");
        driver.get("http://" + domain); 
        String redirectUrl = driver.getCurrentUrl();
        view.endLoading();

        // let's check the different variations and ensure they all redirect to the same url
        view.printInfo(String.format("Checking different variations of the domain to ensure they all redirect to the same URL (%s)", redirectUrl));
        for(String redirectVariation: redirectVariations){
            String targetUrl = redirectVariation + domain;
            view.startLoading("Checking " + targetUrl);
            driver.get(targetUrl);
            String url = driver.getCurrentUrl();
            if(url.equals(redirectUrl)){
                view.printSuccess(targetUrl + " redirects successfully.");
                continue;
            }else{
                view.endLoading();
                return new CheckResult(false, String.format("URL \"%s\" does not redirect to the same URL as the other variations: %s", targetUrl, redirectUrl));
            }
        }

        view.endLoading();
        return new CheckResult(true, "Redirect variations look ok. You may need to check other uncommon variations unique to your site (such as .au domains). You may also need to check the redirect type; it should be 301.");
    }
}
