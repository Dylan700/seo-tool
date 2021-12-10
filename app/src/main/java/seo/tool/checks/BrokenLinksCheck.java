package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class BrokenLinksCheck implements SEOCheck{

    private static enum HTTPCode {
        BAD_REQUEST(400, true),
        NOT_FOUND(404, true),
        FORBIDDEN(403, true),
        GONE(410, true),
        REQUEST_TIMEOUT(408, true),
        SERVICE_UNAVAILABLE(503, true);

        public final int code;
        public final boolean isError;

        private HTTPCode(int code, boolean isError) {
            this.code = code;
            this.isError = isError;
        }
    };

    @Override
    public CheckResult run(WebDriver driver) {
    	// get all links on the page
        List<WebElement> links = driver.findElements(By.tagName("a"));

        for(WebElement link : links) {
            // get the href attribute of the link
            String href = link.getAttribute("href");

            // if the href is not null, check if it is broken
            if(href != null && (href.toLowerCase().contains("http") || href.startsWith("/"))){
            String url = (href.startsWith("/")) ? driver.getCurrentUrl() + href : href;
            if(isBroken(url)){
                return new CheckResult(false, "Broken link found: " + href);
            }
        }
    }

        return new CheckResult(true, "No broken links found");
    }

    // check if a url is redirects to an error page or is simply not found
    private boolean isBroken(String url){
        // send a http request to the url and get the response code
        int responseCode = 0;
        try {
	    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            responseCode = connection.getResponseCode();
        } catch (Exception e) {
            return true;
        }

        for(HTTPCode status : HTTPCode.values()) {
            if(status.code == responseCode) {
                return status.isError;
            }
        }

        return false;
    }
}
