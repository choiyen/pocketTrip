package project.backend.Controller;


import com.nimbusds.jwt.JWT;
import com.nimbusds.oauth2.sdk.ResponseType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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
    public ResponseEntity<?> info(@PathVariable String travelCode, SimpMessageHeaderAccessor messageHeaderAccessor)
    {
        try
        {
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://localhost:8080/plan/select/" + travelCode;
            // GET 요청 보내기
            String response = restTemplate.postForObject(url, null, String.class);
            String url2 = "http://localhost:8080/expenditures/" + travelCode;
            // GET 요청 보내기
            String response2 = restTemplate.getForObject(url2, String.class);
            List list = new ArrayList<>();
            list.add(response);
            list.add(response2);

            return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", list));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }

    @MessageMapping("/travelPlan/{travelCode}/Insert")
    @SendTo("/topic/insert/{travelCode}")//새로운 데이터가 저장되었으니, 해당 채팅방에 속한 사람 모두에게 DB를 보낸다.
    public ResponseEntity<?> insert(@PathVariable String travelCode, @RequestBody ExpendituresDTO expendituresDTO, SimpMessageHeaderAccessor messageHeaderAccessor)
    {
         try
         {
             restTemplate.getMessageConverters()
                     .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
             String url = "http://localhost:8080/expenditures/" + travelCode;
             String response = restTemplate.postForObject(url, expendituresDTO, String.class);

             if(response != null)
             {
                    String response2 = restTemplate.getForObject(url, String.class);
                 return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", Collections.singletonList(response2)));
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

    @MessageMapping("/travelPlan/{travelCode}/Update")
    @SendTo("/topic/{travelCode}/{expenditureId}/Update")//데이터가 수정되었으니, 해당 채팅방에 속한 사람 모두에게 DB를 보낸다.
    public ResponseEntity<?> Update(@PathVariable String travelCode,@PathVariable String expenditureId, @RequestBody ExpendituresDTO expendituresDTO, SimpMessageHeaderAccessor messageHeaderAccessor)
    {
        try
        {
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://localhost:8080/expenditures/" + travelCode + "/" + expenditureId;
            restTemplate.put(url, expendituresDTO, String.class);
            String response2 = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", Collections.singletonList(response2)));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }

    @MessageMapping("/travelPlan/{travelCode}/{expenditureId}/Delete")
    @SendTo("/Topic/{travelCode}/{expenditureId}/Delete")//데이터가 삭제되었으니, 해당 채팅방에 속한 사람 모두에게 변경된 지출 DB를 보낸다.
    public ResponseEntity<?> Delete(@PathVariable String travelCode, @PathVariable String expenditureId, @RequestBody ExpendituresDTO expendituresDTO, SimpMessageHeaderAccessor messageHeaderAccessor)
    {
        try
        {
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://localhost:8080/expenditures/" + travelCode + "/" + expenditureId;
            restTemplate.delete(url, String.class);
            String response2 = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", Collections.singletonList(response2)));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }
}
