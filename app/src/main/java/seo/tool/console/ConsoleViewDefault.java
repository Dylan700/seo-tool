package seo.tool.console;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ConsoleViewDefault implements ConsoleView {

    private static String welcomeBanner = ConsoleView.ANSI_YELLOW+
"""
   ________  ________   _____ __________     __________  ____  __    
  /_  __/ / / / ____/  / ___// ____/ __ \\   /_  __/ __ \\/ __ \\/ / 
   / / / /_/ / __/     \\__ \\/ __/ / / / /    / / / / / / / / / /   
  / / / __  / /___    ___/ / /___/ /_/ /    / / / /_/ / /_/ / /___   
 /_/ /_/ /_/_____/   /____/_____/\\____/    /_/  \\____/\\____/_____/
"""
+ConsoleView.ANSI_RESET;

    private boolean hasProgress = false;
    private int currentProgress = 0;
    private Thread loadingThread;

    private int progressStatusLength = 0;

    @Override
    public void printInfo(String message) {
        resetProgressLine();
        System.out.printf("%s[i]%s %s\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET, message);
        printProgress();
    }

    @Override
    public void printWelcome(){
        resetProgressLine();
	    System.out.printf("%s\n\n", welcomeBanner);
        System.out.printf("%s### WELCOME TO THE SEO TOOL ###%s\n\n", ConsoleView.ANSI_BG_YELLOW, ConsoleView.ANSI_RESET);
    }

    @Override
    public void printError(String message) {
        resetProgressLine();
        endLoading();
        System.out.printf("%s[x]%s %s\n", ConsoleView.ANSI_RED, ConsoleView.ANSI_RESET, message);
        printProgress();
    }

    @Override
    public void printSuccess(String message){
        resetProgressLine();
        endLoading();
        System.out.printf("%s[s]%s %s\n", ConsoleView.ANSI_GREEN, ConsoleView.ANSI_RESET, message);
        printProgress();
    }

    @Override
    public void prompt(){
        resetProgressLine();
        System.out.printf("%sseotool>%s ", ConsoleView.ANSI_YELLOW, ConsoleView.ANSI_RESET);
    }

    @Override
    public void printChecks(List<String> checks){
        resetProgressLine();
        endLoading();
        System.out.printf("%s[i]%s %d checks found.\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET, checks.size());
        for(int i = 0; i < checks.size(); i++){
            System.out.printf("\t%d. %s\n", i+1, checks.get(i));
        }
        printProgress();
    }

    private void startProgress(){
        hasProgress = true;
        System.out.print(ConsoleView.ANSI_HIDE_CURSOR);
    }

    private void endProgress(){
        hasProgress = false;
        System.out.print(ConsoleView.ANSI_SHOW_CURSOR);
    }

    private void resetProgressLine(){
        if(hasProgress){
            System.out.print("\r");
        }
    }

    private void printProgress(){
        if(hasProgress){
            setProgress(currentProgress);
        }
    }

    @Override
    public void setProgress(int progress, String status){
        setProgress(progress);
        
        if(status == null){
            return;
        }

        if(progress < 100){
            System.out.printf(" | %s", status);
        }
        for(int i = 0; i < progressStatusLength-status.length(); i++){
            System.out.print(" ");
        }
        progressStatusLength = status.length();
    }

    @Override
    // set the progress bar to a specific value
    public void setProgress(int progress){
        progress = Math.max(0, progress);
        progress = Math.min(100, progress);
        currentProgress = progress;
        if(!hasProgress){
            startProgress();
        }else{
            System.out.print("\r");
        }

        String progressChunk = "=";
        String progressPointer = ">";
        String progressBraceStart = "[";
        String progressBraceEnd = "]";
        int progressChunkCount = progress / 2;

        if(progress == 100){
            System.out.print(" ");
            for(int i = 0; i < 100 + progressStatusLength; i++){
                System.out.print(" ");
            }
	        progressStatusLength = 0;
            System.out.print(ConsoleView.ANSI_CLEAR_LINE);
            endProgress();
            return;
        }

        System.out.print(progressBraceStart);
        for(int i = 0; i < 50; i++){
            if(i == progressChunkCount)
                System.out.print(progressPointer);
                
            if(i < progressChunkCount){
                System.out.print(progressChunk);
            }else{
                System.out.print(" ");
            }
        }
        System.out.print(progressBraceEnd);
        System.out.printf(" %d%%", progress);
    }

    @Override
    // start a loading animation concurrently
    public synchronized void startLoading(String message){
	if(loadingThread == null){
		loadingThread = new Thread(() -> {
			int state = 0;
			System.out.print(ConsoleView.ANSI_HIDE_CURSOR);
			while(true){
				System.out.print(ConsoleView.ANSI_CLEAR_LINE);
				System.out.printf("%s\u2022%s %s%s", ConsoleView.ANSI_YELLOW, ConsoleView.ANSI_RESET, message, StringUtils.repeat(".", state));
				if(state > 4){
					state = 0;
				}else{
					state++;
				}
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){
					continue;
				}			
			}

		});
		loadingThread.start();
	}	
	
    }

    @Override
    // stop the loading animation
    public synchronized void endLoading(){
	    if(loadingThread != null){
            loadingThread.stop();
            loadingThread = null;
            System.out.print(ConsoleView.ANSI_SHOW_CURSOR);
            System.out.print(ConsoleView.ANSI_CLEAR_LINE);
	    }
    }
    
}
