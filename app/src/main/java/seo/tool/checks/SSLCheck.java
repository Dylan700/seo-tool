package seo.tool.checks;

import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.openqa.selenium.WebDriver;

public class SSLCheck implements SEOCheck{

    @Override
    public CheckResult run(WebDriver driver) {
        String url = driver.getCurrentUrl();
        boolean isUsingSSL = url.startsWith("https");
        boolean isValid = true;
	String reason = null;
        Date notAfterDate = null;
        Date notBeforeDate = null;

        if(isUsingSSL){
            X509Certificate cert = getSSLCertificate(url);
            if(cert == null){
	    	isUsingSSL = false;
                isValid = false;
            }else{
                notAfterDate = cert.getNotAfter();
                notBeforeDate = cert.getNotBefore();
                // check expiry
                try{
                    cert.checkValidity(new Date());
                }catch(Exception e){
		    reason = "it is expired";
                    isValid = false;
                }
            }
        }

        if(!isUsingSSL){
            return new CheckResult(false, "SSL is not used or not auto-redirected!");
        }else{
            return new CheckResult(isValid, String.format("SSL certificate is %s%s. Expires on %s.", isValid ? "valid" : "invalid", reason != null ? " because " + reason : "",  notAfterDate));
        }
    }

    private X509Certificate getSSLCertificate(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            Certificate[] certs = connection.getServerCertificates();
            // filter out and find the ssl certificate
            for (Certificate cert : certs) {
                if (cert.getType().equals("X.509") && cert instanceof X509Certificate) {
                    return (X509Certificate) cert;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
