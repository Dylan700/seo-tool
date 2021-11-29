package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;

public class MetaDescriptionCheck implements SEOCheck{

    @Override
    public CheckResult run(WebDriver driver) {
        // ensure only one h1 tag exists
        String metaDesc = "";
        try{
            metaDesc = driver.findElement(By.xpath("//meta[@name='description']")).getAttribute("content");
        }catch(Exception e){
            return new CheckResult(false, "Meta description not found");
        }

        if (metaDesc.length() < 120) {
	        return new CheckResult(false, String.format("Meta description is too short (%d)", metaDesc.length()));
        }
        
        if(metaDesc.length() > 162){
            return new CheckResult(false, String.format("Meta description is too long (%d)", metaDesc.length()));
        }

	    return new CheckResult(true);
    }
}
