package seo.tool.console.command;

import seo.tool.console.ConsoleView;
import seo.tool.console.InputSystem;
import seo.tool.checks.SEOChecker;

import com.google.api.services.searchconsole.v1.SearchConsole;
import com.google.api.services.searchconsole.v1.SearchConsole.UrlTestingTools;
import com.google.api.services.searchconsole.v1.model.RunMobileFriendlyTestRequest;
import com.google.api.services.searchconsole.v1.SearchConsole.UrlTestingTools.MobileFriendlyTest.Run;
import com.google.api.services.searchconsole.v1.model.RunMobileFriendlyTestResponse;
import com.google.api.services.searchconsole.v1.SearchConsoleRequestInitializer;
import com.google.api.services.searchconsole.v1.model.MobileFriendlyIssue;
import com.google.api.services.searchconsole.v1.model.ResourceIssue;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils.Null;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;

import io.github.cdimascio.dotenv.Dotenv;

public class GoogleConsoleCommand implements Command {

	private SearchConsole sc;
	private CommandRegistry commands;

	public GoogleConsoleCommand() throws GeneralSecurityException, IOException {
		Dotenv dotenv = Dotenv.load();
		String key = dotenv.get("GOOGLE_API_KEY");
		SearchConsole.Builder builder = new SearchConsole.Builder(GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory(), setHttpTimeout());
		builder.setApplicationName("The SEO Tool");
		builder.setSearchConsoleRequestInitializer(new SearchConsoleRequestInitializer(key));
		sc = builder.build();

		this.commands = new CommandRegistry();
		commands.add("mobile", new TestMobileFriendliness());

	}

	public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker) {
		if(args.length < 2){
			view.printInfo("Please see below for a list of commands: ");
			for(Map.Entry<String, Command> commandEntry: commands.getAll()){
				view.printInfo(String.format("gconsole %s - %s", commandEntry.getKey(), commandEntry.getValue().getDescription()));
			}
		}else{
			commands.get(args[1]).execute(args, input, view, checker);
		}
	}

	private class TestMobileFriendliness implements Command {
		public void execute(String[] args, InputSystem input, ConsoleView view, SEOChecker checker){
			view.printInfo("Mobile test for " + checker.getURL());
			view.startLoading("Generating request");
			RunMobileFriendlyTestRequest req = new RunMobileFriendlyTestRequest().setUrl(checker.getURL()).setRequestScreenshot(false);
			try{
				Run run = sc.urlTestingTools().mobileFriendlyTest().run(req);
				view.endLoading();
				view.startLoading("Testing");
				RunMobileFriendlyTestResponse res = run.execute();
				view.endLoading();
				if(res.getMobileFriendlyIssues() == null){
					view.printSuccess("Page is mobile friendly :)");
				}else{
					view.printError("Page is not mobile friendly :(");
					if(res.getMobileFriendlyIssues() != null){
						view.printInfo("Please see the violated mobile friendly rules below:");
						for(MobileFriendlyIssue issue: res.getMobileFriendlyIssues()){
							view.printError(issue.getRule());
						}
					}
				}
			}catch(IOException e){
				view.endLoading();
				view.printError("Unable to make request. " + e.getMessage());
			}
		}

		public String getDescription(){
			return "Test whether the current URL is deemed mobile friendly by Google.";
		}
	}

	public String getDescription(){
		return "Interact with Google Search Console by configuring setup, and querying data.";
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
