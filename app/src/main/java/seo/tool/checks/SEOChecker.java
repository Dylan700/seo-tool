package seo.tool.checks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SEOChecker {

    private WebDriver driver;
    private Map<String, SEOCheck> checks = new HashMap<String, SEOCheck>(0);

    public SEOChecker(){
    	String webdriverPath = System.getenv().get("WEBDRIVER");
	if(webdriverPath == null){
		System.out.println("Web driver path is not set. Please set the Environment Variable as 'WEBDRIVER=/path/to/driver'");
		System.exit(1);
	}

        System.setProperty("webdriver.gecko.driver", webdriverPath);
        System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        options.setLogLevel(FirefoxDriverLogLevel.FATAL);
        driver = new FirefoxDriver(options);
        addChecks();
    }

    private void addChecks(){
        checks.put("heading order", new HeadingHierarchyCheck());
        checks.put("heading structure", new HeadingStructureCheck(true));
	    checks.put("horizontal scroll", new HorizontalScrollCheck());
        checks.put("meta description", new MetaDescriptionCheck());
        checks.put("meta title", new MetaTitleCheck());
        checks.put("platform", new PlatformCheck());
        checks.put("ssl", new SSLCheck());
        checks.put("internal linking", new InternalLinkingCheck());
	checks.put("images", new ImagesCheck());
	checks.put("broken links", new BrokenLinksCheck());
    }

    /**
     * Load the given url into the checker
     * @return true or false if the url was loaded
     */
    public boolean load(String url){
        try {
            driver.get(url);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * Run a check against the website and return the success
     * @param check
     * @return whether the check was successful
     */
    public CheckResult check(String check){
        try{
            if(checks.containsKey(check)){
                return checks.get(check).run(driver);
            }
            return new CheckResult(false);
        }catch(Exception e){
                return new CheckResult(false, String.format("An error occurred! %s", e.getMessage()));
        }
    }

    public void refresh(){
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("window.location.reload(true)");
    }

    public boolean hasURL(){
        return driver.getCurrentUrl() != null;
    }

    public String getURL(){
        return driver.getCurrentUrl();
    }

    public List<String> getChecks(){
        return new ArrayList<String>(checks.keySet());
    }

    public boolean hasCheck(String check){
        return checks.containsKey(check);
    }

    public void quit(){
        driver.quit();
    }

    public WebDriver getDriver(){
        return driver;
    }
}
