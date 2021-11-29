package seo.tool.checks; 

public class CheckResult {

    private boolean isSuccessful;
    private String message;

    public CheckResult(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public CheckResult(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public CheckResult(){
        this.isSuccessful = true;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getMessage() {
        return message;
    }

}