package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import seo.tool.console.ConsoleView;

import org.openqa.selenium.By;

import java.util.List;

public class ImagesCheck implements SEOCheck{

    private ConsoleView view;

    public ImagesCheck(ConsoleView view){
        this.view = view;
    }

    @Override
    public CheckResult run(WebDriver driver) {
        // get all img tags
        List<WebElement> images = driver.findElements(By.tagName("img"));
        view.setProgress(0);

        // check all images have an alt tag
        for(WebElement img : images){
            view.setProgress(Math.max(1, (int) Math.ceil((double) images.indexOf(img) / images.size() * 100)));
            if(img.getAttribute("alt").isEmpty()){
                view.setProgress(100);
                return new CheckResult(false, String.format("Found an image with no alt tag: %s", img.getAttribute("src")));
            }
        }
        view.setProgress(100);
        return new CheckResult();
    }
}
