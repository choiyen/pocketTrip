package project.backend.Service;




import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;


import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import project.backend.Config.StartupRunner;



import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Date;
import java.util.Calendar;
import java.time.*;
import java.util.Properties;


@Slf4j
@Service
public class RateService
{
    @Value("${api.key}")
    private String apiKey;
    private static final int MAX_RETRIES = 3;  // 최대 재시도 횟수

    int attempts = 0;
    boolean success = false;

    public String getObject() throws Exception {


        try
        {
            // 리소스 파일 로딩 (krearn.crt) - JAR 내의 리소스 읽기
            Resource resource = new ClassPathResource("krearn.crt");

            String encodedSrtContent = null;
            try (InputStream inputStream = resource.getInputStream()) {
                // JAR 내부의 리소스를 InputStream으로 읽기
                byte[] bytes = inputStream.readAllBytes();  // InputStream에서 바이트 배열로 읽기
                encodedSrtContent = Base64.getEncoder().encodeToString(bytes);  // Base64로 인코딩
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error reading or encoding certificate: " + e.getMessage());
            }  // 나머지 코드
            Date today = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            LocalTime currentTime = LocalTime.now();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            LocalDate currentDate = LocalDate.now();
            LocalDateTime currentDateTime = LocalDateTime.of(currentDate, currentTime);
            DayOfWeek dayOfWeek = currentDateTime.getDayOfWeek();

            int attempts = 0;
            boolean success = false;
//            SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
//            HttpClient httpClient = HttpClient.create().secure(provider -> provider.sslContext(context));
            String formattedDate = getFormattedDate(dayOfWeek, currentTime, calendar, formatter);
            System.out.println(formattedDate);
            String url1 = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + apiKey + "&searchdate=" + formattedDate + "&data=AP01";
            HttpHeaders headers = new HttpHeaders();
            String responce2 = "";
            headers.set("X-SRT-Content", encodedSrtContent);
            RestTemplate restTemplate = new RestTemplate();

            while (attempts < MAX_RETRIES && !success) {
                try {
                    attempts++;
                    responce2 = restTemplate.getForObject(url1, String.class);

                    success = true;

                } catch (WebClientResponseException e)
                {
                    System.out.println("Request failed. Attempt " + attempts + " of " + MAX_RETRIES + ". Error: " + e.getMessage());
                    if (attempts < MAX_RETRIES) {
                        int waitTime = (attempts == 1) ? 3000 : (attempts == 2) ? 5000 : 7000;
                        System.out.println("Retrying in " + waitTime / 1000 + " seconds...");
                        try {
                            Thread.sleep(waitTime);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        System.out.println("Maximum retry attempts reached. Request failed.");
                    }
                }
            }

            return responce2;

        } catch (Exception e) {
            throw new RuntimeException("Error in getObject method: " + e.getMessage(), e);
        }
    }
    // ssl security Exception 방지
    public void disableSslVerification(){
        // TODO Auto-generated method stub
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType){
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType){
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session){
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
    private String getFormattedDate(DayOfWeek dayOfWeek, LocalTime currentTime, Calendar calendar, SimpleDateFormat formatter)
    {
        String formattedDate = "";

        if (dayOfWeek == DayOfWeek.SUNDAY)
        {
            // 일요일인 경우 전전날 날짜인 금요일의 데이터
            calendar.add(Calendar.DATE, -2);
            formattedDate = formatter.format(calendar.getTime());

        }
        else if (dayOfWeek == DayOfWeek.SATURDAY)
        {
            // 토요일인 경우 전날 날짜인 금요일의 데이터
            calendar.add(Calendar.DATE, -1);
            formattedDate = formatter.format(calendar.getTime());
        }
        else if(dayOfWeek == DayOfWeek.MONDAY && currentTime.isBefore(LocalTime.of(12,0)))
        {//월요일 오전일때는 금요일의 API 데이터를 가져온다.
            calendar.add(Calendar.DATE, -3);
            formattedDate = formatter.format(calendar.getTime());
        }
        else
        {
            // 평일일 경우 오후 6시 이후/이전 처리
            if (currentTime.isAfter(LocalTime.of(12, 0)))
            {
                formattedDate = formatter.format(new Date());  // 오늘 날짜로 처리
            }
            else
            {
                calendar.add(Calendar.DATE, -1);
                formattedDate = formatter.format(calendar.getTime());  // 전날 날짜로 처리
            }
        }

        return formattedDate;
    }
    //SSL 인증을 무시하고 HTTPS를 http처럼 post 요청하기
    public static void  ignoreSsl() throws  Exception
    {
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession sslSession)
            {
                return true;
            };
        };
    }
    private static void trustAllHttpsCertificates() throws Exception
    {
        TrustManager[] trustManagers = new TrustManager[1];
        TrustManager tm = new miTm();
        trustManagers[0] = tm;
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustManagers, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    static class miTm implements TrustManager, X509TrustManager
    {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }
        public boolean isClientTrusted(X509Certificate[] certs)
        {
            return true;
        }
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            return;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            return;
        }
    }



}
