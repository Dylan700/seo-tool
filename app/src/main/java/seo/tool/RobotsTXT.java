package seo.tool;

import java.util.List;
import java.util.regex.Pattern;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class RobotsTXT {
    
    private String url;
    private String domain;
    private List<String> disallows = new ArrayList<String>();
    private String robotsTxt;

    public RobotsTXT(String url){
        this.url = url;
    }

    private boolean download(){
        try{
            // get the domain from the url
            this.domain = url.substring(url.indexOf("//") + 2, url.indexOf("/", url.indexOf("//") + 2));

            URI uri = new URI("https://" + this.domain + "/robots.txt");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();
            String content = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            this.robotsTxt = content;
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean parse(){
        if(robotsTxt == null){
            if(!download()){
                return false;
            }
        }

        String[] lines = this.robotsTxt.split("\n");
        List<String> botNames = new ArrayList<String>(1);
        botNames.add("*");
        String currentBotName = "*";
        for(String line : lines){
            if(line.startsWith("User-agent:")){
                currentBotName = line.substring(11).trim();
            }
            if(line.startsWith("Disallow:") && botNames.contains(currentBotName)){
                String path = line.substring(line.indexOf(":") + 1).trim();
                if(path.startsWith("/")){
                    this.disallows.add(path);
                }
            }
        }
        return true;
    }

    public boolean isAllowed(String url){
        if(url.contains(this.domain)){
            String path = url.substring(url.indexOf("/", url.indexOf("//") + 2));
            for(String disallow : this.disallows){
                if(disallow.contains("*")){
                    Pattern p = Pattern.compile(disallow.replace("*", ".*").replace("/", "\\/").replace("?", "\\?"));
                    if(p.matcher(path).lookingAt()){
                        return false;
                    }
                }else if(path.startsWith(disallow)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
