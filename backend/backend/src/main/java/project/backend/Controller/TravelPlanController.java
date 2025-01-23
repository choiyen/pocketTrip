package project.backend.Controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.TravelPlanDTO;
import project.backend.Entity.ApplicantsEntity;
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
import java.util.*;

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

    private  ResponseDTO responseDTO = new ResponseDTO<>();

    //시작 날짜를 기준으로 데이터 정렬하여 프론트엔드로 전송
    @PostMapping("/find")
    public ResponseEntity<?> TravelDTO(@AuthenticationPrincipal String userId)
    {
        try
        {
            Flux<TravelPlanEntity> travelPlan = travelPlanService.travelPlanEntityAll(userId);
            Flux<TravelPlanEntity> travelPlanAll = travelPlan.map(travelPlanEntity -> {
                try
                {

                    TravelPlanEntity travelPlan1 = ConvertToMain(decrypt(travelPlanEntity.getTravelCode(), key), travelPlanEntity);
                    return travelPlan1;
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });

            Flux<TravelPlanDTO> travelPlanAll2 = travelPlanAll.map(travelPlanEntity -> {
               TravelPlanDTO travelPlanDTO = ConvertTo(travelPlanEntity.getTravelCode(), travelPlanEntity);
               return  travelPlanDTO;
            });

            List<TravelPlanDTO> resultList = travelPlanAll2.collectList().block();
            return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", resultList));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
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
            System.out.println(userId);
            TravelPlanEntity travelPlan = ConvertTo(userId, encrypt(generatedString, key), travelPlanDTO);
            TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanInsert(travelPlan).block();

            List<Object> list = new ArrayList<>(Collections.singletonList(ConvertTo(generatedString, travelPlan1)));
            return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", list));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }

    }
    //유출되도 상관 없을 것 같은 데이터(기능 동작 확인)
    @PostMapping("/update/{travelcode}")
    public ResponseEntity<?> TravelUpdate(@AuthenticationPrincipal String userId, @PathVariable(value = "travelcode") String travelcode, @RequestBody TravelPlanDTO newtravelPlanDTO)
    {
        try
        {
            if(travelPlanService.SelectTravelCode(travelcode) == true)
            {

                TravelPlanEntity Oldtravelplan = travelPlanService.TravelPlanSelect(encrypt(travelcode, key)).block();
                System.out.println(Oldtravelplan.getFounder());
                if(Oldtravelplan.getFounder().equals(userId) || Oldtravelplan.getParticipants().isEmpty() == false)
                {
                    TravelPlanEntity travelPlan = ConvertTo(Oldtravelplan, newtravelPlanDTO);
                    TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanUpdate(travelPlan).block();
                    List<Object> list = new ArrayList<>(Collections.singletonList(ConvertTo(travelcode, travelPlan1)));
                    return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", list));
                }
                else if(Oldtravelplan.getParticipants().contains(userId) == true)
                {
                    TravelPlanEntity travelPlan = ConvertTo(Oldtravelplan, newtravelPlanDTO);
                    TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanUpdate(travelPlan).block();
                    List<Object> list = new ArrayList<>(Collections.singletonList(ConvertTo(travelcode, travelPlan1)));
                    return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", list));
                }
                else
                {
                    throw new AccessDeniedException("여행 코드는 존재하나, 데이터를 수정할 권한이 없습니다.");
                }
            }
            else
            {
                throw new NoSuchElementException("여행 코드는 존재하나, 데이터를 수정할 권한이 없습니다.");
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage(), null));
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
            List<Object> list = new ArrayList<>(Collections.singletonList(ConvertTo(travelCode, travelPlan1)));
            return ResponseEntity.ok().body(responseDTO.Response("info", "데이터 전송 알림!!", list));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }
    //여행 신청 정보에 대해 개별 승인
    @PostMapping("/check/{travelCode}")
    public ResponseEntity<?> TravelCheckindividual(@AuthenticationPrincipal String userId, @PathVariable(value = "travelCode") String travelCode, @RequestBody String Applicant )
    {
        try
        {
            Mono<TravelPlanEntity> travelPlanEntityMono = travelPlanService.TravelPlanSelect(travelCode);
            if (travelPlanEntityMono.block().getFounder().equals(userId))
            {
                Mono<ApplicantsEntity> applicantsEntityMono = appllicantsService.applicantsSelect(travelCode);
                if(applicantsEntityMono.block().getUserList().contains(Applicant))
                {
                    TravelPlanEntity travelPlan = ConvertToParticipants(Applicant, travelPlanEntityMono);
                    Mono<TravelPlanEntity> travelPlanEntityMono1 = travelPlanService.TravelPlanUpdate(travelPlan);
                    List<Object> list = new ArrayList<>(Collections.singletonList(travelPlanEntityMono1));
                    return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", list));
                }
                else
                {
                    throw new NoSuchElementException ("신청자 목록에 존재하지 않은 사람입니다.");
                }


            }
            else
            {
                throw new AccessDeniedException("데이터를 수정할 수 있는 총무가 아닙니다.");
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }





    //여행신청자 정보에 대해 승인해주는 것 - 단체 승인
    @PostMapping("/checkAll/{travelCode}")
    public ResponseEntity<?> TravelCheckAll(@AuthenticationPrincipal String userId, @PathVariable(value = "travelCode") String travelCode) {

        try {
            Mono<TravelPlanEntity> travelPlanEntityMono = travelPlanService.TravelPlanSelect(travelCode);
            if (travelPlanEntityMono.block().getFounder().equals(userId))
            {
                Mono<ApplicantsEntity> applicantsEntityMono = appllicantsService.applicantsSelect(travelCode);
                TravelPlanEntity travelPlan = ConvertToParticipants(applicantsEntityMono, travelPlanEntityMono);
                Mono<TravelPlanEntity> travelPlanEntityMono1 = travelPlanService.TravelPlanUpdate(travelPlan);
                appllicantsService.TravelPlanAllDelete(travelCode);//전체 승인이 완료되었으므로 데이터 삭제
                List<Object> list = new ArrayList<>(Collections.singletonList(travelPlanEntityMono1));
                return ResponseEntity.ok().body(responseDTO.Response("success", "전송 완료", list));

            }
            else
            {
                throw new RuntimeException("데이터를 수정할 수 있는 총무가 아닙니다.");
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }



    //데이터베이스에서 데이터 정상 제거 확인
    @PostMapping("/delete/{travelCode}")
    public ResponseEntity<?> TravelDelete(@AuthenticationPrincipal String userId, @PathVariable(value = "travelCode") String travelCode)
    {
        try
        {
            if(travelPlanService.SelectTravelCode(travelCode) == true)
            {
                Mono<TravelPlanEntity> travelPlanEntityMono = travelPlanService.TravelPlanSelect(travelCode);
                if(travelPlanEntityMono.block().getFounder().equals(userId) || travelPlanEntityMono.block().getParticipants().contains(userId))
                {
                    travelPlanService.TravelPlanDelete(encrypt(travelCode, key));
                    if(appllicantsService.ApplicantExistance(encrypt(travelCode, key)).block() == true)
                    //mongoDB는 NoSql이라 관계에 의한 cascade 삭제를 지원하지 않아 관련 처리 진행
                    {
                        appllicantsService.TravelPlanAllDelete(travelCode);
                    }
                    List<Object> list = new ArrayList<>();
                    list.add(travelPlanEntityMono);
                    return ResponseEntity.ok().body(responseDTO.Response("info", "정상적으로 데이터 제거가 완료되었습니다.", list));
                }
                else
                {
                    throw  new RuntimeException("해당 데이터를 삭제할 권한이 없습니다.");
                }

            }
            else
            {
                throw  new RuntimeException("해당 여행 코드를 가진 데이터를 찾을 수 없습니다. 다시 확인해주세요...");
            }

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }

    }

    private TravelPlanEntity ConvertTo(String userid, String RandomString, TravelPlanDTO travelPlanDTO)
    {
        Set userset = new HashSet<>();
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(RandomString)
                .location(travelPlanDTO.getLocation())
                .startDate(travelPlanDTO.getStartDate())
                .endDate(travelPlanDTO.getEndDate())
                .expense(travelPlanDTO.getExpense())
                .founder(userid)
                .participants(userset)
                .isCalculate(travelPlanDTO.isCalculate())
                .id(travelPlanDTO.getId())
                .build();

        return travelPlan;
    }

    private TravelPlanEntity ConvertTo(TravelPlanEntity OldEntity, TravelPlanDTO NewDTO)
    {
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(OldEntity.getTravelCode())
                .location(NewDTO.getLocation())
                .startDate(NewDTO.getStartDate())
                .endDate(NewDTO.getEndDate())
                .expense(NewDTO.getExpense())
                .founder(OldEntity.getFounder())
                .participants(OldEntity.getParticipants())
                .isCalculate(NewDTO.isCalculate())
                .id(OldEntity.getId())
                .build();

        return travelPlan;
    }

    private TravelPlanEntity ConvertToMain(String travelCode, TravelPlanEntity travelPlanEntity)
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


    private TravelPlanDTO ConvertTo(String travelCode, TravelPlanEntity travelPlanEntity)
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
    private static String encrypt(String data, String key) throws Exception
    {
         SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
         byte[] encryptedData = cipher.doFinal(data.getBytes());
         return Base64.getEncoder().encodeToString(encryptedData);
    }

    // 복호화: 암호화된 문자열을 AES 알고리즘을 사용하여 복호화
    private static String decrypt(String encryptedData, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedBytes);
        return new String(decryptedData);
    }

    // AES 키 생성 (16 바이트 키)
    private static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);  // 128비트 키 생성
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
    private TravelPlanEntity ConvertToParticipants(Mono<ApplicantsEntity> applicantsEntity, Mono<TravelPlanEntity> travelPlanEntityMono)
    {
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(travelPlanEntityMono.block().getTravelCode())
                .location(travelPlanEntityMono.block().getLocation())
                .startDate(travelPlanEntityMono.block().getStartDate())
                .endDate(travelPlanEntityMono.block().getEndDate())
                .expense(travelPlanEntityMono.block().getExpense())
                .founder(travelPlanEntityMono.block().getFounder())
                .participants(applicantsEntity.block().getUserList())
                .isCalculate(travelPlanEntityMono.block().isCalculate())
                .id(travelPlanEntityMono.block().getId())
                .build();
        return travelPlan;
    }
    private TravelPlanEntity ConvertToParticipants(String Applicants, Mono<TravelPlanEntity> travelPlanEntityMono)
    {
        Set ApplicantsList = new HashSet<>();
        ApplicantsList.addAll(travelPlanEntityMono.block().getParticipants());
        ApplicantsList.add(Applicants);

        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(travelPlanEntityMono.block().getTravelCode())
                .location(travelPlanEntityMono.block().getLocation())
                .startDate(travelPlanEntityMono.block().getStartDate())
                .endDate(travelPlanEntityMono.block().getEndDate())
                .expense(travelPlanEntityMono.block().getExpense())
                .founder(travelPlanEntityMono.block().getFounder())
                .participants(ApplicantsList)
                .isCalculate(travelPlanEntityMono.block().isCalculate())
                .id(travelPlanEntityMono.block().getId())
                .build();
        return travelPlan;
    }
}
