package project.backend.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.print.Doc;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class StartupRunner {


    public void run(String... args) throws Exception {
        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=NoYRIAuzpf7UUeZXbpQtOLuDhamPWzi6&searchdate=2025-01-31&data=AP01";
        StringBuffer sb = new StringBuffer();

        try
        {
            TrustManager[] trustManagers = new TrustManager[] {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
                {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustManagers, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line).append("/n");
            }
            System.out.println(sb.toString());
            br.close();
            in.close();
            connection.disconnect();

        }
        catch (Exception e)
        {
            System.err.println("Error while making initial API request: " + e.getMessage());
        }
    }
}
