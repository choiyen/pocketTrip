package project.backend.Controller;


import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import project.backend.DTO.ExpendituresDTO;
import project.backend.DTO.ResponseDTO;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
public class SoketController
{

    private ResponseDTO responseDTO = new ResponseDTO<>();

    RestTemplate restTemplate = new RestTemplate();


    // /travelPlan 경로로 들어오는 메시지를 처리
    @MessageMapping("/travelPlan")
    @SendTo("/topic/travelPlan")  // 메시지를 /topic/travelPlan로 전송
    public String handleTravelPlanMessage(String message)
    {
        // 클라이언트로부터 받은 메시지를 처리하고 응답
        System.out.println("받은 메시지: " + message);
        return "서버에서 응답: " + message; // 클라이언트로 응답
    }

    //이거는 일단 성공
    @MessageMapping("/travelPlan/{travelCode}")
    @SendToUser("/queue/{travelCode}")//해당 주소를 가진 자기 자신에게 방에 입장했으니, 여행방과 관련된 데이터를 보낸다.
    public ResponseEntity<?> info(@Header("Authorization") String authHeader, @DestinationVariable("travelCode") String travelCode)
    {
        try
        {
            //System.out.println(authHeader);
            String token = authHeader != null ? authHeader : "";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://localhost:9000/plan/select/" + travelCode;
            // GET 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            //System.out.println(response.getBody());
            if(response.getStatusCode() == HttpStatus.OK)
            {
                String url2 = "http://localhost:9000/expenditures/" + travelCode;
                // GET 요청 보내기
                ResponseEntity<String> response2 = restTemplate.exchange(url2,HttpMethod.GET,entity, String.class);
                //System.out.println(response2.getBody());
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

    //데이터 추가 완료, 전체 송출이 됨을 확인
    @MessageMapping("/travelPlan/{travelCode}/Insert")
    @SendTo("/topic/insert/{travelCode}")//새로운 데이터가 저장되었으니, 해당 채팅방에 속한 사람 모두에게 DB를 보낸다.
    public ResponseEntity<?> insert(@Header("Authorization") String authHeader, @DestinationVariable("travelCode")  String travelCode, @RequestBody ExpendituresDTO expendituresDTO)
    {
         try
         {
             System.out.println("Userssssss" + expendituresDTO);
             String token = authHeader != null ? authHeader : "";
             HttpHeaders headers = new HttpHeaders();
             headers.set("Authorization", token);
             headers.setContentType(MediaType.APPLICATION_JSON);

             HttpEntity<ExpendituresDTO> entity = new HttpEntity<>(expendituresDTO, headers);

             restTemplate.getMessageConverters()
                     .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

             // 2. JSON 처리용 MappingJackson2HttpMessageConverter 추가
             ObjectMapper objectMapper = new ObjectMapper();
             MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

             // 3. HTTP 메시지 컨버터에 추가
             List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
             converters.add(jsonConverter);

             String url = "http://localhost:9000/expenditures/" + travelCode;

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
            System.out.println("Userssssss" + expendituresDTO);
            System.out.println("dfffffff :   " + expendituresDTO);
            String token = authHeader != null ? authHeader : "";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

            // 2. JSON 처리용 MappingJackson2HttpMessageConverter 추가
            ObjectMapper objectMapper = new ObjectMapper();
            MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(objectMapper);

            // 3. HTTP 메시지 컨버터에 추가
            List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
            converters.add(jsonConverter);

            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(expendituresDTO, headers);

            System.out.println("ssss" + entity);
            String url = "http://localhost:9000/expenditures/" + travelCode + "/" + expenditureId;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
            System.out.println("dddd" + response.getBody());
            if (response.getStatusCode() == HttpStatus.OK)
            {
                HttpEntity<String> entity2 = new HttpEntity<>(headers);
                String url2 = "http://localhost:9000/expenditures/" + travelCode;
                ResponseEntity<String> response2 = restTemplate.exchange(url2, HttpMethod.GET, entity2, String.class);
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
    @SendTo("/topic/{travelCode}/{expenditureId}/Delete")//데이터가 삭제되었으니, 해당 채팅방에 속한 사람 모두에게 변경된 지출 DB를 보낸다.
    public ResponseEntity<?> Delete(@Header("Authorization") String authHeader, @DestinationVariable("travelCode") String travelCode, @DestinationVariable("expenditureId") String expenditureId)
    {
        try
        {
            String token = authHeader != null ? authHeader : "";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            String url = "http://localhost:9000/expenditures/" + travelCode + "/" + expenditureId;

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK)
            {
                HttpEntity<String> entity2 = new HttpEntity<>(headers);
                String url2 = "http://localhost:9000/expenditures/" + travelCode;
                ResponseEntity<String> response2 = restTemplate.exchange(url2, HttpMethod.GET, entity2, String.class);
                if(response2.getStatusCode() == HttpStatus.OK)
                {
                    List<Object> objects = new ArrayList<>();
                    objects.add(response.getBody());
                    objects.add(response2.getBody());
                    return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", objects));
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
