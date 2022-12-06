package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;

import seo.tool.console.InputSystem;
import seo.tool.console.ConsoleView;

import java.util.List;

public class KeywordDensityCheck implements SEOCheck {

	private ConsoleView view;
	private InputSystem input;

	public KeywordDensityCheck(ConsoleView view, InputSystem input){
		this.view = view;
		this.input = input;
	}

	
	@Override
	public CheckResult run(WebDriver driver){
		view.startLoading("Getting page text");
		// get all text from the page
		String text = driver.findElement(By.tagName("body")).getText().toLowerCase();
		view.endLoading();
		view.printInfo("Enter the Keyword");
		view.prompt();
		String keyword = input.getInput();
		view.startLoading("Calculating Density");
		int count = text.split(keyword.toLowerCase()).length-1;
		int words = text.split(" ").length-1;
		double density = ((double)count/(double)words);
		view.endLoading();
		view.printInfo("Keyword appeared " + count + " times, out of " + words + " words.");
		return new CheckResult((density > 0.004 && density < 0.025), String.format("Keyword Density: %.3f", density));
	}

}
