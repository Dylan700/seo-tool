package seo.tool.checks;

import org.openqa.selenium.WebDriver;

/**
 * This interface represents a strategy to perform an SEO check on a website using selenium.
 */
public interface SEOCheck {

    /**
     * Perform the SEO check
     * @param selenium
     * @return whether the check was successful
     */
    public CheckResult run(WebDriver driver);
}