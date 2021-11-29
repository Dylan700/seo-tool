package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;

public class HeadingStructureCheck implements SEOCheck{

    private boolean showHidden = false;

    public HeadingStructureCheck(boolean showHidden){
	    this.showHidden = showHidden;
    }

    public HeadingStructureCheck(){
	    this.showHidden = false;
    }

    @Override
    public CheckResult run(WebDriver driver) {
        // get all headings and print the structure
        List<WebElement> headings = driver.findElements(By.xpath("//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6]"));

        String hierarchy = "\n";
        for (WebElement tag : headings) {
            if (tag.isDisplayed() == false && !showHidden) {
                continue;
            }
            hierarchy += String.format("%s - %s\n", tag.getTagName(), tag.getText());
        }
        return new CheckResult(true, "Heading Structure is: " + hierarchy);
    }
}
