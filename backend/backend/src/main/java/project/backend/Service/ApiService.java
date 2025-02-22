package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.backend.DTO.ExpendituresDTO;

@Slf4j
@Service
public class ApiService<T> {

    private final RestTemplate restTemplate;

    @Autowired
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate; // 싱글톤으로 관리되는 RestTemplate 주입
    }

    public String getApiResponse(String url) {
        return restTemplate.getForObject(url, String.class);
    }
    //            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

    public ResponseEntity<String> exchange(String url, String type, HttpEntity<T> entity)
    {
        try {
            if(type.equals("GET"))
            {
                return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            }
            else if(type.equals("POST"))
            {
                return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            }
            else if(type.equals("PUT"))
            {
                return restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            }
            else if(type.equals("DELETE"))
            {
                return restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            }
        }
        catch (Exception e)
        {
            log.error("Restemplate 요청 중  오류가 발생했습니다. 다시 시도해주세요.");
        }

        return null;
    }

}
