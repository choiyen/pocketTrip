package project.backend.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;


@Service
public class RateService
{
    @Value("${api.key}")
    private String apiKey;

    public String getObject()
    {
        try
        {


            //https 의 SSL 인증서를 우회하는 코드 -> SSL 인증서 자체가 유료라 필요, 현업에선  사용하지 않음.
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {return null;}
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
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

            //여기까지

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            Date today = new Date(); // 현재 날짜 생성


            // Calendar 객체 생성
            Calendar calendar = Calendar.getInstance();

            // 오늘 날짜를 Calendar에 설정
            calendar.setTime(today);

            // 전날로 설정
            calendar.add(Calendar.DATE, -1);

            // 전날 날짜를 Date 객체로 반환
            Date yesterday = calendar.getTime();



            LocalTime currentTime = LocalTime.now();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 형식 지정

            // 오후 6시를 기준으로 동작을 분기
            if (currentTime.isAfter(LocalTime.of(18, 0)))
            {
                // 오후 6시 이후일 때 동작
                // 원하는 형식으로 출력하기 위해 SimpleDateFormat 사용
                String formattedToday = formatter.format(today);
                System.out.println(formattedToday);
                String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + apiKey + "&searchdate=" + formattedToday + "&data=AP01";
                // GET 요청 보내기
                String response = restTemplate.getForObject(url, String.class);


                return  response;
            } else {
                // 오후 6시 이전일 때 동작
                // 원하는 형식으로 출력하기 위해 SimpleDateFormat 사용
                String formattedToday = formatter.format(yesterday);
                System.out.println(formattedToday);
                String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + apiKey + "&searchdate=" + formattedToday + "&data=AP01";
                // GET 요청 보내기


                String response = restTemplate.getForObject(url, String.class);

                return  response;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
}


}
