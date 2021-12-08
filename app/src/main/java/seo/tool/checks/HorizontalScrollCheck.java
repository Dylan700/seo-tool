package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.util.List;

public class HorizontalScrollCheck implements SEOCheck{

    @Override
    public CheckResult run(WebDriver driver) {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
        boolean hasHorizontalScroll = (boolean) js.executeScript("return document.documentElement.scrollWidth > document.documentElement.clientWidth");

		if(hasHorizontalScroll){
			return new CheckResult(false, "This page has a horizontal scroll bar!");    
		}else{
			return new CheckResult();
		}
    }
}
