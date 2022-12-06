
package seo.tool.checks;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import seo.tool.console.ConsoleView;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadabilityCheck implements SEOCheck {

	private ConsoleView view;

	public ReadabilityCheck(ConsoleView view){
		this.view = view;
	}

	
	@Override
	public CheckResult run(WebDriver driver){
		view.startLoading("Calculating");
		// get all text from the page
		String text = driver.findElements(By.tagName("p")).stream().map(e -> e.getText().toLowerCase()).reduce("", String::concat);
		int wordCount = text.split(" ").length;
		int averageSentenceLength = getAverageSentenceLength(text);
		int averageSyllables = getAverageSyllables(text);
		view.endLoading();
		double readability = 206.835 - (1.015*(double)averageSentenceLength) - (84.6*(double)averageSyllables);
		view.printInfo(String.format("Average Sentence Length (%d), Average Number of Syllables (%d), Word Count (%d), Sentence Count (%d)", averageSentenceLength, averageSyllables, wordCount, text.split("(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s").length));
		return new CheckResult(readability >= 60, String.format("Text Readability is %.2f", readability));
	}

	// get the average length of a sentence, number of words divided by number of sentences
	private int getAverageSentenceLength(String text){
		String[] sentences = text.split("(?<!\\w\\.\\w.)(?<![A-Z][a-z]\\.)(?<=\\.|\\?)\\s");
		int lengthSum = 0;
		for(int i = 0; i < sentences.length; i++){
			lengthSum += sentences[i].split(" ").length;
		}

		return lengthSum/sentences.length;
	}

	// get the average number of syllables per word
	private int getAverageSyllables(String text){
		String[] words = text.split(" ");
		int sum = 0;
		for(int i = 0; i < words.length; i++){
			sum += countSyllables(words[i]);
		}
		return sum/words.length;
	}

	// count the number of syllables in a word
	private int countSyllables(String word){
		List<String> vowels = new ArrayList<String>(5);		
		vowels.add("a");
		vowels.add("e");
		vowels.add("i");
		vowels.add("o");
		vowels.add("u");
		String[] letters = word.toLowerCase().split("");
		String prev = "";
		int count = 0;

		if(word.length() <= 3){
			return 1;
		}

		for(int i = 0; i < word.length(); i++){
			// don't count double vowels
			if(vowels.contains(letters[i]) && !vowels.contains(prev)){
				count++;
			}
			prev = letters[i];
		}

		// certain endings can be ignored
		if((word.toLowerCase().endsWith("es") || word.toLowerCase().endsWith("ed") || word.toLowerCase().endsWith("e")) && !word.toLowerCase().endsWith("le")){
			count--;
		}
		return count > 0 ? count : 1;
	}

}
