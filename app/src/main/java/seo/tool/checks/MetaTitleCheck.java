package seo.tool.checks;

import org.openqa.selenium.WebDriver;

import java.util.Locale;

import org.apache.commons.exec.util.StringUtils;
import org.openqa.selenium.By;

import org.apache.commons.text.similarity.FuzzyScore;

public class MetaTitleCheck implements SEOCheck{

    @Override
    public CheckResult run(WebDriver driver) {
        String metaTitle = "";
        try {
            metaTitle = driver.findElement(By.xpath("(//meta[@name='title']) | (//meta[@property='og:title'])[1]")).getAttribute("content");
        }catch(Exception e){
            return new CheckResult(false, "Meta title not found");
        }

        String h1 = driver.findElement(By.xpath("//h1")).getText();
        double similarity = calculateSimilarity(metaTitle, h1);

        if(metaTitle.length() > 70){
            return new CheckResult(false, String.format("Meta title is too long (%s)", metaTitle.length()));
        }else if(metaTitle.length() < 30){
            return new CheckResult(false, String.format("Meta title is too short (%s)", metaTitle.length()));
        }

        if(similarity >= 0.35){
            return new CheckResult(true, String.format("The meta title is similar to the h1 title with %.2f%% similarity.", similarity * 100));
        }else{
            return new CheckResult(false, String.format("Meta title should be more similar to h1. Similarity score was %.2f%%\n\n\th1 = %s\n\tmeta title = %s", similarity * 100, h1, metaTitle));
        }
    }

    // calculate the similarity of the two titles as a percentage using fuzzy score and comparison of words
    private double calculateSimilarity(String s1, String s2) {
        double percent = 0;
        String term = s1;
        String query = s2;
        
        if(query.length() > term.length()) {
            term = s2;
            query = s1;
        }

        Locale locale = new Locale("en", "US");
        FuzzyScore fs = new FuzzyScore(locale);
        double score = fs.fuzzyScore(term, query);
        double maxScore = fs.fuzzyScore(term, term);

        // convert fuzzy score to percent based on term length
        double rawPercent = score / maxScore;
        percent = Math.min(1, rawPercent);

        // split each string into words and count the number of words that are similar by 0.8 or more
        String[] queryWords = query.toLowerCase().split(" ");
        String[] termWords = term.toLowerCase().split(" ");
        int similarWords = 0;
        for(String queryWord : queryWords){
            for(String termWord : termWords){
                double wordScore = fs.fuzzyScore(termWord, queryWord);
                double wordMaxScore = fs.fuzzyScore(termWord, termWord);
                double wordPercent = wordScore / wordMaxScore;
                if(wordPercent >= 0.8){
                    similarWords++;
                }
            }
        }

        double similarWordPercent = similarWords / (double)termWords.length;
        double bestPercent = Math.max(percent, similarWordPercent);

        return bestPercent;
    }
}
