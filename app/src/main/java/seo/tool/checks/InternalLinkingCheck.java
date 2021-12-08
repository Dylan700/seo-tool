package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.NoSuchElementException;

import java.util.List;

public class InternalLinkingCheck implements SEOCheck {

    @Override
    public CheckResult run(WebDriver driver) {
        // get all the internal links from the page
        List<WebElement> links = driver.findElements(By.tagName("a"));
        // filter the links by the domain of the current page or a relative path
        List<WebElement> internalLinks = links.stream()
                .filter(link -> link.getAttribute("href") != null ? (link.getAttribute("href").startsWith(driver.getCurrentUrl()) || link.getAttribute("href").startsWith("/")) : false)
                .collect(java.util.stream.Collectors.toList());
        
        // check that all internal links don't have the rel=nofollow attribute 
        for(WebElement link : internalLinks) {
            try{
                if(link.getAttribute("rel").contains("nofollow")) {
                    return new CheckResult(false, "Internal link " + link.getAttribute("href") + " has rel=nofollow. This is not good for internal linking.");
                }
            }catch(StaleElementReferenceException e){
                internalLinks.remove(link);
                continue;
            }
        }

        // check that we have a reasonable number of links based on the amount of content on the page
        int textLength = driver.getPageSource().length();
        int linksRatio = 1;
        int textRatio = 5000;

        if(internalLinks.size() < ((textLength / textRatio) * linksRatio)) {
            return new CheckResult(false, "Not enough internal links are being used. " + internalLinks.size() + " internal links found, but at least " + (textLength / textRatio) * linksRatio + " links should be on the page.");
        }

        // check most of the links are deep (not just top level pages)
        int deepLinks = 0;
        int linkDepthThreshold = 2;
        int ratio = 3; // 1/3 of the links should be deep.
        for(WebElement link : internalLinks) {
            try{
                    String href = link.getAttribute("href");
                    href = href.replace(driver.getCurrentUrl(), "");
                    if(href.split("/").length >= linkDepthThreshold) {
                        deepLinks++;
                    }
            }catch(StaleElementReferenceException e){
                internalLinks.remove(link);
               continue;
            }
        }

        if(deepLinks < (internalLinks.size() / ratio)) {
            return new CheckResult(false, "Not enough deep links are being used. " + deepLinks + " deep links found, but at least " + (internalLinks.size() / 2) + " links should be on the page.");
        }
        
        return new CheckResult(true, "Internal linking is good.");


    }
    
}
