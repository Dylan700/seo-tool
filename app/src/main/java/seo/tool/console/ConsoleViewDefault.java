package seo.tool.console;

import java.util.List;

public class ConsoleViewDefault implements ConsoleView {

    private boolean hasProgress = false;
    private int currentProgress = 0;

    @Override
    public void printInfo(String message) {
        resetProgressLine();
        System.out.printf("%s[i]%s %s\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET, message);
        printProgress();
    }

    @Override
    public void printWelcome(){
        resetProgressLine();
        System.out.printf("Welcome to the SEO Tool\n\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET);
    }

    @Override
    public void printError(String message) {
        resetProgressLine();
        System.out.printf("%s[x]%s %s\n", ConsoleView.ANSI_RED, ConsoleView.ANSI_RESET, message);
        printProgress();
    }

    @Override
    public void prompt(){
        resetProgressLine();
        System.out.printf("%s>%s ", ConsoleView.ANSI_YELLOW, ConsoleView.ANSI_RESET);
    }

    @Override
    public void printChecks(List<String> checks){
        resetProgressLine();
        System.out.printf("%s[i]%s %d checks found.\n", ConsoleView.ANSI_BLUE, ConsoleView.ANSI_RESET, checks.size());
        for(int i = 0; i < checks.size(); i++){
            System.out.printf("\t%d. %s\n", i+1, checks.get(i));
        }
        printProgress();
    }

    private void startProgress(){
        hasProgress = true;
    }

    private void endProgress(){
        hasProgress = false;
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
            System.out.print("done.");
            for(int i = 0; i < 100; i++){
                System.out.print(" ");
            }
            System.out.println();
            endProgress();
            return;
        }

        System.out.print(progressBraceStart);
        for(int i = 0; i < 60; i++){
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
    
}
