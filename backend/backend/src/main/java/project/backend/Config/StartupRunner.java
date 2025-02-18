package project.backend.Config;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StartupRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=NoYRIAuzpf7UUeZXbpQtOLuDhamPWzi6&searchdate=2025-01-31&data=AP01";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            System.out.println("Startup API Request Successful: " + response);
        } catch (Exception e) {
            System.err.println("Error while making initial API request: " + e.getMessage());
        }
    }
}
