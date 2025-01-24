package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class RateService
{
    @Value("${api.key}")
    private String apiKey;

    public String getObject()
    {
        RestTemplate restTemplate = new RestTemplate();

        Date today = new Date(); // 현재 날짜 생성

        // 원하는 형식으로 출력하기 위해 SimpleDateFormat 사용
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 형식 지정
        String formattedToday = formatter.format(today);

        // API URL 설정
        String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + apiKey + "&searchdate=" + formattedToday + "&data=AP01";

        // GET 요청 보내기
        String response = restTemplate.getForObject(url, String.class);

        // 응답 출력
        System.out.println(response);

        return response;
    }

}
