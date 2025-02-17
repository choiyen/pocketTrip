package project.backend.Service;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;



import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Calendar;
import java.time.*;





@Service
public class RateService
{
    @Value("${api.key}")
    private String apiKey;
    private static final int MAX_RETRIES = 15;  // 최대 재시도 횟수

    int attempts = 0;
    boolean success = false;

    public String getObject() {
        try {
            // RestTemplate을 생성하면서 HttpURLConnection을 사용하는 기본 설정
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            // 나머지 코드
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
            String response = "";

            while (attempts < MAX_RETRIES && !success) {
                try {
                    attempts++;
                    String formattedDate = getFormattedDate(dayOfWeek, currentTime, calendar, formatter);
                    System.out.println(formattedDate);
                    String url = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + apiKey + "&searchdate=" + formattedDate + "&data=AP01";

                    response = restTemplate.getForObject(url, String.class);
                    success = true;

                } catch (RestClientException e) {
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

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error in getObject method: " + e.getMessage(), e);
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
}
