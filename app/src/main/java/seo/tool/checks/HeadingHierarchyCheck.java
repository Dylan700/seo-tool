package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;

public class HeadingHierarchyCheck implements SEOCheck{

    @Override
    public CheckResult run(WebDriver driver) {
        // ensure only one h1 tag exists
        List<WebElement> h1 = driver.findElements(By.tagName("h1"));
        if (h1.size() != 1) {
            return new CheckResult(false, "There should be only one h1 tag");
        }

        // ensure at least one h2 tag exists
        List<WebElement> h2 = driver.findElements(By.tagName("h2"));
        if (h2.size() == 0) {
            return new CheckResult(false, "There should be at least one h2 tag");
        }

        // ensure heading tags are correct organized correctly
        List<WebElement> headings = driver.findElements(By.xpath("//*[self::h1 or self::h2 or self::h3 or self::h4 or self::h5 or self::h6]"));

        int previousNestLevel = 0;
        String hierarchy = "\n";
        for (WebElement tag : headings) {
            if (tag.isDisplayed() == false) {
                continue;
            }
            hierarchy += String.format("%s - %s\n", tag.getTagName(), tag.getText());
            int currentNestLevel = Integer.parseInt(tag.getTagName().substring(1));

            if (previousNestLevel == 0) {
                previousNestLevel = currentNestLevel;
            }
            if (!(previousNestLevel+1 == currentNestLevel || previousNestLevel == currentNestLevel || previousNestLevel > currentNestLevel)) {
                return new CheckResult(false, String.format("Heading tags are not nested correctly! Current heading was h%d, but previous heading was h%d. Here is a breakdown of the heirarchy up to this point: %s", currentNestLevel, previousNestLevel, hierarchy));
            }
            previousNestLevel = currentNestLevel;
        }
        return new CheckResult(true);
    }
}
