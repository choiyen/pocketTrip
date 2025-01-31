//package project.backend.Controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import project.backend.DTO.ResponseDTO;
//import project.backend.Service.RateService;
//
//import javax.net.ssl.*;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//
//@RestController
//@RequestMapping("/rate")
//public class rateController {
//
//    @Autowired
//    private RateService rateService;
//
//    private ResponseDTO responseDTO = new ResponseDTO<>();
//
//    public List<Map<String, Object>> list = new ArrayList<>();
//
//    @Scheduled(cron = "0 0 18 * * *")
//    @PostConstruct
//    public void RatePost()
//    {
//        try
//        {
//            String rate = rateService.getObject();
//            ObjectMapper objectMapper = new ObjectMapper();
//            list = objectMapper.readValue(rate, List.class);
//
//            for (Map<String, Object> map : list)
//            {
//                map.remove("deal_bas_r");
//                map.remove("bkpr");
//                map.remove("kftc_bkpr");
//                map.remove("kftc_deal_bas_r");
//                map.remove("yy_efee_r");
//                map.remove("ten_dd_efee_r");
//                map.remove("result");
//
//                if(map.containsKey("cur_nm"))
//                {
//                    Object value = map.remove("cur_nm");
//                    if(value.toString().contains("말레이지아"))
//                    {
//                        Object text = value.toString().replace("지", "시");
//                        map.put("통화이름", text);
//
//                    }
//                    else if(value.toString().contains("유로"))
//                    {
//                        map.put("통화이름", "유럽연합 " + value);
//                    }
//                    else if(value.toString().contains("위안화"))
//                    {
//                        map.put("통화이름", "중국 " + value);
//                    }
//                    else if(value.toString().contains("덴마아크"))
//                    {
//                        Object text = value.toString().replace("아", "");
//                        map.put("통화이름", text);
//                    }
//                    else
//                    {
//                        map.put("통화이름", value);
//
//                    }
//
//                }
//                if (map.containsKey("cur_unit")) {
//                    // 기존 키의 값을 가져오기
//                    Object value = map.remove("cur_unit");
//                    // 새로운 키와 값으로 추가
//                    map.put("기준통화", value);
//                }
//                if(map.containsKey("ttb"))
//                {
//                    Object value = map.remove("ttb");
//                    map.put("환전구매환율", value);
//                }
//                if(map.containsKey("tts"))
//                {
//                    Object value = map.remove("tts");
//                    map.put("환전판매환율", value);
//                }
//            }
//
//            //한국 돈을 한국 돈으로 환전할 이유는 없음....따라서, 삭제
//            Iterator<Map<String, Object>> iterator = list.iterator();
//
//            while (iterator.hasNext())
//            {
//                Map<String, Object> currency = iterator.next();
//                if (currency.get("통화이름").equals("한국 원")) {
//                    iterator.remove(); // 해당 항목 삭제
//                }
//            }
//
//            System.out.println(list);
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @GetMapping
//    public ResponseEntity<?> responseEntity()
//    {
//        try
//        {
//
//            return ResponseEntity.ok().body(responseDTO.Response("info", "환율 데이터 전송완료!",  list));
//
//        }
//        catch (Exception e)
//        {
//            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
//        }
//    }
//
//    @GetMapping("/country")
//    public  ResponseEntity<?> responseEntity(@RequestBody String country)
//    {
//        try
//        {
//
//            List<Map<String, Object>> result = new ArrayList<>();
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, String> result1 = objectMapper.readValue(country, Map.class);
//            // "country" 키의 값을 가져오기
//            String countrys = result1.get("country");
//
//            for (Map<String, Object> map : list)
//            {
//                // 특정 키가 존재하고, 해당 키의 값이 String인 경우
//                if (map.containsKey("통화이름") && map.get("통화이름") instanceof String)
//                {
//                    String value = (String) map.get("통화이름");
//
//                    if (value.contains(countrys))
//                    {
//                        result.add(map); // 결과 리스트에 추가
//                    }
//                }
//            }
//            return ResponseEntity.ok().body(responseDTO.Response("success","여행 가계부 데이터 삽입을 위한 환율 전송", result));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
//        }
//    }
//
//}
