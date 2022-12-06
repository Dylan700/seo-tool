package seo.tool.console.command;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;

import seo.tool.RobotsTXT;
import seo.tool.XMLBuilder;
import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

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
    private String sitemap;

    @Override
    public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
        this.view = view;
        this.input = input;
        this.driver = checker.getDriver();
        
        if(driver.getCurrentUrl().equals(startingPage) && sitemap != null){
            view.printInfo("Sitemap already exists for this website. Do you want to overwrite it? (y/n)");
            view.prompt();
            if(input.getInput().equals("n")){
                saveSitemap(sitemap);
                return;
            }else{
                view.printInfo("Ok, let's recreate the sitemap then!");
            }
        }
    
        sitemap = null;
        robots = null;
        startingPage = driver.getCurrentUrl();
        pages.clear();
        pages.add(startingPage);
        view.startLoading("Parsing the robots.txt file");
        robots = new RobotsTXT(startingPage);
        
        if(robots.parse()){
	    view.endLoading();
            view.printSuccess("robots.txt file parsed successfully.");
        }else{
	    view.endLoading();
            view.printError("robots.txt file could not be parsed... ignoring.");
        }

        view.startLoading("Finding all pages");
        generateSitemap(startingPage);
	view.endLoading();
        view.printInfo(String.format("%s pages found.", pages.size()));
        sitemap = generateSitemapXML();
        view.printSuccess("Sitemap.xml generated successfully.");
        saveSitemap(sitemap);
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
	view.startLoading("Finding pages");
        // get all <a> tags from the current page and get their hrefs
        List<WebElement> links = driver.findElements(By.tagName("a"));
        List<String> hrefs = new ArrayList<String>(0);
        try{
            hrefs = links.stream().map(link -> link.getAttribute("href")).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        }catch(StaleElementReferenceException e){
	    view.endLoading();
            view.printError("Stale element reference exception... ignoring page " + currentPage);
        }

        // for each href add it to the sitemap list
        for (String href : hrefs) {
            // if the href is a valid url, navigate to it and generate the sitemap
            if (isValidUrl(href)) {
		view.endLoading();
                view.printInfo("Found Page: " + href);
                pages.add(href);
		view.startLoading("Finding pages");
                driver.navigate().to(href);
		view.endLoading();
                generateSitemap(href);
		view.startLoading("Finding pages");
            }
        }
	view.endLoading();
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

    private void saveSitemap(String sitemap){
        while(true){
            view.printInfo("Please enter the path to save the file. (e.g. /home/user/sitemap.xml or /home/user/)");
            view.prompt();
            String path = input.getInput();
            view.startLoading("Saving sitemap.xml");
            if(XMLBuilder.save(path, sitemap)){
		view.endLoading();
                view.printSuccess("Sitemap.xml saved successfully.");
                break;
            }else{
		view.endLoading();
                view.printError("Unable to save xml! Would you like to try again? or just output to console? (y/n)");
                view.prompt();
                String response = input.getInput();
                if("yes".contains(response.toLowerCase())){
                    continue;
                }else{
                    view.printInfo("Sitemap.xml Dump:");
                    view.printInfo(sitemap);
                    break;
                }
            }
        }
    }

    public String getDescription(){
	    return "Generate a sitemap for the currently loaded URL.";
    }
}
