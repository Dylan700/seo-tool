package seo.tool.checks;

import org.openqa.selenium.WebDriver;

/**
 * Check the current technology platform of the site. E.g. wordpress, custom, neto, etc...
 */
public class PlatformCheck implements SEOCheck{

    @Override
    public CheckResult run(WebDriver driver) {

        String siteType = "Custom";

        // check if the site is wordpress
        if (driver.getPageSource().toLowerCase().contains("wordpress")) {
            siteType = "Wordpress";
        }

        // check if the site is a neto site by checking for the cdn.neto.com.au network request
        if (driver.getPageSource().contains("cdn.neto.com.au")) {
            siteType = "Neto";
        }

        return new CheckResult(true, "Appears to be a "+ siteType +" site.");
    }
}
