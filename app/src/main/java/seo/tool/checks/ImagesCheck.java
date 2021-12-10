package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.List;

public class ImagesCheck implements SEOCheck{

    @Override
    public CheckResult run(WebDriver driver) {
        // get all img tags
        List<WebElement> images = driver.findElements(By.tagName("img"));

        // check all images have an alt tag
        for(WebElement img : images){
            if(img.getAttribute("alt").isEmpty()){
                return new CheckResult(false, String.format("Found an image with no alt tag: %s", img.getAttribute("src")));
            }
        }
        
        return new CheckResult();
    }
}
