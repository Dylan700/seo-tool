package seo.tool.checks;

import seo.tool.console.ConsoleView;

import org.openqa.selenium.WebDriver;

import com.google.api.services.pagespeedonline.v5.PagespeedInsights;
import com.google.api.services.pagespeedonline.v5.PagespeedInsights.Pagespeedapi.Runpagespeed;
import com.google.api.services.pagespeedonline.v5.PagespeedInsightsRequest;
import com.google.api.services.pagespeedonline.v5.model.PagespeedApiPagespeedResponseV5;
import com.google.api.services.pagespeedonline.v5.model.LighthouseResultV5;
import com.google.api.services.pagespeedonline.v5.model.PagespeedApiLoadingExperienceV5;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.io.IOException;

import java.math.BigDecimal;

public class SpeedCheck implements SEOCheck{

    private ConsoleView view;
    private PagespeedInsights psi;


    public SpeedCheck(ConsoleView view) throws GeneralSecurityException, IOException {
        this.view = view;
        PagespeedInsights.Builder builder = new PagespeedInsights.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), setHttpTimeout());
        psi = builder.setApplicationName("The SEO Tool").build();
    }

    @Override
    public CheckResult run(WebDriver driver) {
        view.startLoading("Checking page speed");
        try{
            Runpagespeed run = psi.pagespeedapi().runpagespeed(driver.getCurrentUrl()).setKey(null).setStrategy("desktop");
            PagespeedApiPagespeedResponseV5 res = run.execute();
            PagespeedApiLoadingExperienceV5 loadingExperience = res.getLoadingExperience();
            LighthouseResultV5 lighthouse = res.getLighthouseResult();
            double performanceScore = ((BigDecimal)lighthouse.getCategories().getPerformance().getScore()).doubleValue();
            view.endLoading();
            view.printInfo(String.format("Overall Performance: %.0f%%", ( performanceScore * 100)));
            view.printInfo(String.format("Loading Experience: %s", loadingExperience.getOverallCategory()));
            return new CheckResult(performanceScore >= 0.90);
        }catch(IOException e){
            view.endLoading();
            return new CheckResult(false, "Unable to check page speed: " + e.getMessage());
        }
    }

    private HttpRequestInitializer setHttpTimeout() {
		return new HttpRequestInitializer() {
		  @Override
		  public void initialize(HttpRequest httpRequest) throws IOException {
			httpRequest.setConnectTimeout(3 * 60000);  // 3 minutes connect timeout
			httpRequest.setReadTimeout(3 * 60000);  // 3 minutes read timeout
		  }
		};
	}
}
