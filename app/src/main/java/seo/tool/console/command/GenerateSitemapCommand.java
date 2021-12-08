package seo.tool.console.command;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;

import seo.tool.RobotsTXT;
import seo.tool.XMLBuilder;
import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.ArrayList;

public class GenerateSitemapCommand implements Command {

    private List<String> pages = new ArrayList<String>(0);
    private String startingPage;
    private ConsoleView view;
    private InputSystem input;
    private WebDriver driver;
    private RobotsTXT robots;

    @Override
    public void execute(InputSystem input, ConsoleView view, WebDriver driver){
        this.view = view;
        this.input = input;
        this.driver = driver;
        startingPage = driver.getCurrentUrl();
        pages.add(startingPage);
        view.printInfo("Parsing the robots.txt file...");
        robots = new RobotsTXT(startingPage);
        
        if(robots.parse()){
            view.printInfo("robots.txt file parsed successfully.");
        }else{
            view.printError("robots.txt file could not be parsed... ignoring.");
        }

        view.printInfo("Finding all pages...");
        generateSitemap(startingPage);
        view.printInfo(String.format("%s pages found.", pages.size()));
        view.printInfo("Generating sitemap.xml...");
        String sitemap = generateSitemapXML();
        view.printInfo("Sitemap.xml generated successfully.");
        view.printInfo(sitemap);
    }

    private String generateSitemapXML(){
        XMLBuilder xml = new XMLBuilder("urlset");
        xml.addAttribute("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
        for(String page : pages){
            xml.addElement("url").addElement("loc").addText(page).up().up();
        }
        return xml.build();
    }

    // this is a recursive function that will use the current page to start generating a sitemap
    private void generateSitemap(String currentPage) {
        // get all <a> tags from the current page and get their hrefs
        List<WebElement> links = driver.findElements(By.tagName("a"));
        List<String> hrefs = new ArrayList<String>(0);
        try{
            hrefs = links.stream().map(link -> link.getAttribute("href")).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }catch(StaleElementReferenceException e){
            view.printError("Stale element reference exception... ignoring page " + currentPage);
        }

        // for each href add it to the sitemap list
        for (String href : hrefs) {
            // if the href is a valid url, navigate to it and generate the sitemap
            if (isValidUrl(href)) {
                view.printInfo("Found Page: " + href);
                pages.add(href);
                driver.navigate().to(href);
                generateSitemap(href);
            }
        }
    }

    // a url is valid if it is from the same domain as the starting page or relative, and it is not a duplicate
    private boolean isValidUrl(String url){
        if(pages.contains(url) || url == null || !robots.isAllowed(url)){
            return false;
        }

        if(url.contains("#")){
            return false;
        }

        // check if the url is an image or pdf
        Pattern p = Pattern.compile("(jpg|jpeg|gif|png|pdf)((\\?+.*)?$|$)");
        Matcher m = p.matcher(url);
        if(m.find()){
            return false;
        }

        String domain = startingPage.substring(startingPage.indexOf("//") + 2, startingPage.indexOf("/", startingPage.indexOf("//") + 2));
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url.substring(url.indexOf("//") + 2, url.indexOf("/", url.indexOf("//") + 2)).equals(domain);
        } else if(url.startsWith("/")){
            return true;
        }
        return false;
    }
}
