package project.backend.Controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.TravelPlanDTO;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Service.AppllicantsService;
import project.backend.Service.TravelPlanService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.beans.Transient;
import java.util.Base64;
import java.util.List;
import java.util.Random;

/*
MongoDB에서는 컬렉션이 데이터가 실제로 삽입될 때 자동으로 생성됩니다.
즉, MongoRepository를 통해 데이터를 삽입하려고 시도했을 때, 컬렉션이 자동으로 생성됩니다
 */
@RestController
@RequestMapping("/plan")
public class TravelPlanController
{


    @Autowired
    private TravelPlanService travelPlanService;

    @Autowired
    private AppllicantsService appllicantsService;

    private final String key = "1234567890123456";



    //시작 날짜를 기준으로 데이터 정렬하여 프론트엔드로 전송
    @PostMapping("/find")
    public ResponseEntity<?> TravelDTO(@AuthenticationPrincipal String userId)
    {
        try
        {
            Flux<TravelPlanEntity> travelPlan = travelPlanService.travelPlanEntityAll(userId);
            Flux<TravelPlanEntity> travelPlanAll = travelPlan.map(travelPlanEntity -> {
                try {
                    TravelPlanEntity travelPlan1 = ConvertToMain(decrypt(travelPlanEntity.getTravelCode(), key), travelPlanEntity);
                    return travelPlan1;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            Flux<TravelPlanDTO> travelPlanAll2 = travelPlanAll.map(travelPlanEntity -> {
               TravelPlanDTO travelPlanDTO = ConvertTo(travelPlanEntity.getTravelCode(), travelPlanEntity);
               return  travelPlanDTO;
            });
            List<TravelPlanDTO> resultList = travelPlanAll2.collectList().block();
            return ResponseEntity.ok().body(resultList);
        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
    //정상적으로 동작 되어짐 확인
    @PostMapping("/insert")
    public ResponseEntity<?> TravelInsert(@AuthenticationPrincipal String userId, @RequestBody TravelPlanDTO travelPlanDTO)
    {
        try
        {
            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 8;
            Random random = new Random();
            String generatedString;
              // 16 바이트 키 (AES-128)

            while (true)
            {

                generatedString = random.ints(leftLimit,rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();

                if(travelPlanService.SelectTravelCode(generatedString) == false)
                {
                    break;
                }
            }

            TravelPlanEntity travelPlan = ConvertTo(encrypt(generatedString, key), travelPlanDTO);
            TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanInsert(travelPlan).block();
            TravelPlanDTO travelPlanDTO1 = ConvertTo(travelPlan1.getTravelCode(), travelPlan1);
            return ResponseEntity.ok().body(travelPlanDTO1);
        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
    //유출되도 상관 없을 것 같은 데이터(기능 동작 확인)
    @PostMapping("/update/{travelcode}")
    public ResponseEntity<?> TravelUpdate( @PathVariable(value = "travelcode") String travelcode, @RequestBody TravelPlanDTO travelPlanDTO)
    {
        try
        {
            if(travelPlanService.SelectTravelCode(travelcode) == true)
            {

                TravelPlanEntity Oldtravelplan = travelPlanService.TravelPlanSelect(encrypt(travelcode, key)).block();
                TravelPlanEntity travelPlan = ConvertTo(encrypt(travelcode, key), travelPlanDTO, Oldtravelplan.getId());
                TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanUpdate(travelPlan).block();
                TravelPlanDTO travelPlanDTO1 = ConvertTo(travelcode, travelPlan1);
                return ResponseEntity.ok().body(travelPlanDTO1);
            }
            else
            {
                ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                        .error("해당 여행 코드의 기록을 찾을 수 없습니다.")
                        .build();
                return ResponseEntity.badRequest().body(responseDTO);
            }

        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //travelcode에 따라 데이터 출력 확인
    @PostMapping("/select/{travelCode}")
    public ResponseEntity<?> TravelSelect(@PathVariable(value = "travelCode") String travelCode)
    {
        try
        {
            TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanSelect(encrypt(travelCode, key)).block();
            Thread.sleep(2000);
            return ResponseEntity.ok().body(ConvertTo(travelCode, travelPlan1));
        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    //데이터베이스에서 데이터 정상 제거 확인
    @PostMapping("/delete/{travelCode}")
    public ResponseEntity<?> TravelDelete(@PathVariable(value = "travelCode") String travelCode)
    {
        try
        {
            if(travelPlanService.SelectTravelCode(travelCode) == true)
            {
                travelPlanService.TravelPlanDelete(encrypt(travelCode, key));
                if(appllicantsService.ApplicantExistance(encrypt(travelCode, key)).block() == true)
                //mongoDB는 NoSql이라 관계에 의한 cascade 삭제를 지원하지 않아 관련 처리 진행
                {
                    appllicantsService.TravelPlanAllDelete(travelCode);
                }
                return ResponseEntity.ok().body("정상적으로 데이터 제거가 완료되었습니다.");
            }
            else
            {
                throw  new RuntimeException("해당 여행 코드를 가진 데이터를 찾을 수 없습니다. 다시 확인해주세요...");
            }

        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    public TravelPlanEntity ConvertTo(String RandomString, TravelPlanDTO travelPlanDTO)
    {
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(RandomString)
                .location(travelPlanDTO.getLocation())
                .startDate(travelPlanDTO.getStartDate())
                .endDate(travelPlanDTO.getEndDate())
                .expense(travelPlanDTO.getExpense())
                .founder(travelPlanDTO.getFounder())
                .participants(travelPlanDTO.getParticipants())
                .isCalculate(travelPlanDTO.isCalculate())
                .id(travelPlanDTO.getId())
                .build();

        return travelPlan;
    }

    public TravelPlanEntity ConvertTo(String travelCode, TravelPlanDTO travelPlanDTO, String id)
    {
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(travelCode)
                .location(travelPlanDTO.getLocation())
                .startDate(travelPlanDTO.getStartDate())
                .endDate(travelPlanDTO.getEndDate())
                .expense(travelPlanDTO.getExpense())
                .founder(travelPlanDTO.getFounder())
                .participants(travelPlanDTO.getParticipants())
                .isCalculate(travelPlanDTO.isCalculate())
                .id(id)
                .build();

        return travelPlan;
    }

    public TravelPlanEntity ConvertToMain(String travelCode, TravelPlanEntity travelPlanEntity)
    {
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(travelCode)
                .location(travelPlanEntity.getLocation())
                .startDate(travelPlanEntity.getStartDate())
                .endDate(travelPlanEntity.getEndDate())
                .expense(travelPlanEntity.getExpense())
                .founder(travelPlanEntity.getFounder())
                .participants(travelPlanEntity.getParticipants())
                .isCalculate(travelPlanEntity.isCalculate())
                .id(travelPlanEntity.getId())
                .build();

        return travelPlan;
    }


    public TravelPlanDTO ConvertTo(String travelCode, TravelPlanEntity travelPlanEntity)
    {
        TravelPlanDTO travelPlan = TravelPlanDTO.builder()
                .travelCode(travelCode)
                .location(travelPlanEntity.getLocation())
                .startDate(travelPlanEntity.getStartDate())
                .endDate(travelPlanEntity.getEndDate())
                .expense(travelPlanEntity.getExpense())
                .founder(travelPlanEntity.getFounder())
                .participants(travelPlanEntity.getParticipants())
                .isCalculate(travelPlanEntity.isCalculate())
                .id(travelPlanEntity.getId())
                .build();

        return travelPlan;
    }

    //암호화 코드 작성
    public static String encrypt(String data, String key) throws Exception
    {
         SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
         byte[] encryptedData = cipher.doFinal(data.getBytes());
         return Base64.getEncoder().encodeToString(encryptedData);
    }

    // 복호화: 암호화된 문자열을 AES 알고리즘을 사용하여 복호화
    public static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedBytes);
        return new String(decryptedData);
    }

    // AES 키 생성 (16 바이트 키)
    public static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);  // 128비트 키 생성
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    //마지막 과제 : 여행 날짜 위주로 데이터 정렬 후 불러오기


}
