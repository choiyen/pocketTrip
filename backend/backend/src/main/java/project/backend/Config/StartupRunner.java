package project.backend.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class StartupRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=NoYRIAuzpf7UUeZXbpQtOLuDhamPWzi6&searchdate=2025-01-31&data=AP01";

        try {
//            RestTemplate restTemplate = new RestTemplate();
//            String response = restTemplate.getForObject(url, String.class);
//            System.out.println("Startup API Request Successful: " + response);
            // 요청할 URL

            // HttpClient 생성
            HttpClient client = HttpClient.newHttpClient();

            // GET 요청 준비
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET() // GET 요청
                    .build();

            // 요청 보내기 및 응답 받기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답 상태 코드 및 본문 출력
            System.out.println("Response Code: " + response.statusCode());
            System.out.println("Response Body: " + response.body());


        }
        catch (Exception e)
        {
            System.err.println("Error while making initial API request: " + e.getMessage());
        }
    }
}
