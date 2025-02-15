package project.backend.Controller;


import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import project.backend.DTO.ExpendituresDTO;
import project.backend.DTO.ResponseDTO;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class SoketController
{

    private ResponseDTO responseDTO = new ResponseDTO<>();

    RestTemplate restTemplate = new RestTemplate();

    @MessageMapping("/travelPlan/{travelCode}")
    @SendToUser("/queue/{travelCode}")//해당 주소를 가진 자기 자신에게 방에 입장했으니, 여행방과 관련된 데이터를 보낸다.
    public ResponseEntity<?> info(@Header("Authorization") String authHeader, @DestinationVariable("travelCode") String travelCode)
    {
        try
        {
            String token = authHeader != null ? authHeader : "";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " +token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://localhost:8080/plan/select/" + travelCode;
            // GET 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if(response.getStatusCode() == HttpStatus.OK)
            {
                String url2 = "http://localhost:8080/expenditures/" + travelCode;
                // GET 요청 보내기
                ResponseEntity<String> response2 = restTemplate.exchange(url2,HttpMethod.GET,entity, String.class);

                if(response2.getStatusCode() == HttpStatus.OK)
                {
                    List list = new ArrayList<>();
                    list.add(response.getBody());
                    list.add(response2.getBody());
                    return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", list));
                }
                else
                {
                    throw new RuntimeException("지출 정보를 불러오는데 실패했어요. 다시 시도해주세요");
                }
            }
            else
            {
                throw new RuntimeException("여행 목록을 불러오는데 실패했어요ㅠㅠㅠㅠ");
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }

    @MessageMapping("/travelPlan/{travelCode}/Insert")
    @SendTo("/topic/insert/{travelCode}")//새로운 데이터가 저장되었으니, 해당 채팅방에 속한 사람 모두에게 DB를 보낸다.
    public ResponseEntity<?> insert(@Header("Authorization") String authHeader, @DestinationVariable("travelCode")  String travelCode, @RequestBody ExpendituresDTO expendituresDTO)
    {
         try
         {
             String token = authHeader != null ? authHeader : "";
             HttpHeaders headers = new HttpHeaders();
             headers.set("Authorization", "Bearer " +token);
             HttpEntity<ExpendituresDTO> entity = new HttpEntity<>(expendituresDTO, headers);
             
             restTemplate.getMessageConverters()
                     .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
             
             String url = "http://localhost:8080/expenditures/" + travelCode;
             
             ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

             if (response.getStatusCode() == HttpStatus.OK)
             {
                     HttpEntity<String> entity2 = new HttpEntity<>(headers);
                     ResponseEntity<String> response2 = restTemplate.exchange(url, HttpMethod.GET, entity2, String.class);
                     if(response2.getStatusCode() == HttpStatus.OK)
                     {
                         return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", Collections.singletonList(response2.getBody())));
                     }
                     else
                     {
                         throw new RuntimeException("비용 목록 불러오기 실패!! 다시 시도해주세요");
                     }
             }
             else
             {
                 throw new RuntimeException("지출 데이터를 저장하는데 실패하였습니다.");
             }
         }
         catch (Exception e)
         {
             return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
         }
    }

    @MessageMapping("/travelPlan/{travelCode}/{expenditureId}/Update")
    @SendTo("/topic/{travelCode}/{expenditureId}/Update")//데이터가 수정되었으니, 해당 채팅방에 속한 사람 모두에게 DB를 보낸다.
    public ResponseEntity<?> Update(@Header("Authorization") String authHeader, @DestinationVariable("travelCode") String travelCode,@DestinationVariable("expenditureId") String expenditureId, @RequestBody ExpendituresDTO expendituresDTO)
    {
        try
        {
            String token = authHeader != null ? authHeader : "";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " +token);
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            HttpEntity<ExpendituresDTO> entity = new HttpEntity<>(expendituresDTO, headers);
            String url = "http://localhost:8080/expenditures/" + travelCode + "/" + expenditureId;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK)
            {
                HttpEntity<String> entity2 = new HttpEntity<>(headers);
                ResponseEntity<String> response2 = restTemplate.exchange(url, HttpMethod.GET, entity2, String.class);
                if (response2.getStatusCode() == HttpStatus.OK) {
                    return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", Collections.singletonList(response2.getBody())));
                }
                else
                {
                    throw new RuntimeException("비용 목록 불러오기 실패");
                }
            }
            else
            {
                throw new RuntimeException("지출 데이터를 저장하는데 실패하였습니다.");
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }

    @MessageMapping("/travelPlan/{travelCode}/{expenditureId}/Delete")
    @SendTo("/Topic/{travelCode}/{expenditureId}/Delete")//데이터가 삭제되었으니, 해당 채팅방에 속한 사람 모두에게 변경된 지출 DB를 보낸다.
    public ResponseEntity<?> Delete(@Header("Authorization") String authHeader, @DestinationVariable("travelCode") String travelCode, @DestinationVariable("expenditureId") String expenditureId)
    {
        try
        {
            String token = authHeader != null ? authHeader : "";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " +token);
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://localhost:8080/expenditures/" + travelCode + "/" + expenditureId;

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK)
            {
                HttpEntity<String> entity2 = new HttpEntity<>(headers);
                ResponseEntity<String> response2 = restTemplate.exchange(url, HttpMethod.GET, entity2, String.class);
                if(response2.getStatusCode() == HttpStatus.OK)
                {
                    return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", Collections.singletonList(response2.getBody())));
                }
                else
                {
                    throw new RuntimeException("비용 목록 갱신 실패!! 다시 시도해줘");
                }
            }
            else
            {
                throw new RuntimeException("지출 데이터를 삭제하는데 실패하였습니다.");
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }
}
