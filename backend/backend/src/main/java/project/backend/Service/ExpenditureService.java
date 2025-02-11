package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ExpenditureEntity;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Repository.ExpendituresRepository;
import project.backend.Repository.TravelPlanRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Slf4j
@Service
public class ExpenditureService {

    private final String key = "1234567890123456";



    @Autowired
    private ExpendituresRepository expendituresRepository;
    @Autowired
    private TravelPlanRepository travelPlanRepository;

    public Mono<ExpenditureEntity> create(String email, ExpenditureEntity expenditureEntity) throws Exception {

        String travelCode = encrypt(expenditureEntity.getTravelCode(),key);
        TravelPlanEntity travelPlan = travelPlanRepository.findByTravelCode(travelCode).block();

        if (travelPlan == null) {
            throw new RuntimeException("TravelPlan not found");
        }

        if(!(travelPlan.getParticipants().contains(email) || travelPlan.getFounder().equals(email))) {
            throw new RuntimeException("Participant not found");
        }

        if(!(travelPlan.getParticipants().contains(expenditureEntity.getPayer()) || travelPlan.getFounder().equals(expenditureEntity.getPayer()))) {
            throw new RuntimeException("Payer not found");
        }

        return expendituresRepository.save(expenditureEntity);
    }

    public Flux<ExpenditureEntity> findAllByTravelCode(String travelCode) {
        return expendituresRepository.findAllByTravelCode(travelCode);
    }

    public boolean SelectExpenditureId(String expenditureId) throws Exception {
        Boolean bool = expendituresRepository.existsByExpenditureId(encrypt(expenditureId ,key)).block();
        return  bool;
    }


    public Mono<ExpenditureEntity> updateExpenditure(String email, String expenditureId, ExpenditureEntity expenditureEntity) {
//        return validate(email, expenditureEntity) // 검증
//                .then(expendituresRepository.findByExpenditureId(expenditureId)) // 기존 데이터 조회
//                .switchIfEmpty(Mono.error(new RuntimeException("Expenditure not found: " + expenditureId))) // 존재하지 않으면 에러 반환
//                .flatMap(originalEntity -> {
//                    // 업데이트 로직
//                    originalEntity.setPurpose(expenditureEntity.getPurpose());
//                    originalEntity.setMethod(expenditureEntity.getMethod());
//                    originalEntity.setPublic(expenditureEntity.isPublic());
//                    originalEntity.setPayer(expenditureEntity.getPayer());
//                    originalEntity.setDate(expenditureEntity.getDate());
//                    originalEntity.setKRW(expenditureEntity.getKRW());
//                    originalEntity.setAmount(expenditureEntity.getAmount());
//                    originalEntity.setCurrency(expenditureEntity.getCurrency());
//                    originalEntity.setDescription(expenditureEntity.getDescription());
//                    return expendituresRepository.save(originalEntity); // 업데이트 후 저장
//                })
//                .doOnError(e -> log.error("Error updating expenditure: {}", e.getMessage()));
        ExpenditureEntity originalExpenditure = expendituresRepository.findByExpenditureId(expenditureId).block();
        if(originalExpenditure == null) {
            throw new RuntimeException("Expenditure not found");
        }

        TravelPlanEntity travelPlanEntity = travelPlanRepository.findByTravelCode(originalExpenditure.getTravelCode()).block();
        if(travelPlanEntity == null) {
            throw new RuntimeException("TravelPlan not found");
        }

        // 사용자가 참여자 명단에 없는 경우
        if(!(travelPlanEntity.getParticipants().contains(email) || travelPlanEntity.getFounder().equals(email))) {
            throw new RuntimeException("Participant not found");
        }

        // 지불인이 참여자 명단에 없는 경우
        if(!(travelPlanEntity.getParticipants().contains(expenditureEntity.getPayer()) || travelPlanEntity.getFounder().equals(expenditureEntity.getPayer()))) {
            throw new RuntimeException("Payer not found");
        }

        originalExpenditure.setPurpose(expenditureEntity.getPurpose());
        originalExpenditure.setMethod(expenditureEntity.getMethod());
        originalExpenditure.setPublic(expenditureEntity.isPublic());
        originalExpenditure.setPayer(expenditureEntity.getPayer());
        originalExpenditure.setDate(expenditureEntity.getDate());
        originalExpenditure.setKRW(expenditureEntity.getKRW());
        originalExpenditure.setAmount(expenditureEntity.getAmount());
        originalExpenditure.setCurrency(expenditureEntity.getCurrency());
        originalExpenditure.setDescription(expenditureEntity.getDescription());

        return expendituresRepository.save(originalExpenditure);
    }

    public Flux<ExpenditureEntity> deleteExpenditure(String email, String travelCode, String expenditureId) {
//        return expendituresRepository.findByExpenditureId(expenditureId)
//                .switchIfEmpty(Mono.error(new RuntimeException("Expenditure with id " + expenditureId + " does not exist")))
//                .flatMap(expenditure ->
//                        validate(email, expenditure) // 삭제 전 검증
//                                .then(expendituresRepository.deleteByExpenditureId(expenditureId)) // 삭제
//                                .thenReturn(travelCode) // travelCode 반환
//                )
//                .flatMapMany(code -> expendituresRepository.findAllByTravelCode(code)); // travelCode로 지출 목록 반환

        ExpenditureEntity expenditureEntity = expendituresRepository.findByExpenditureId(expenditureId).block();
        if(expenditureEntity == null) {
            throw new RuntimeException("Expenditure not found");
        }

        TravelPlanEntity travelPlanEntity = travelPlanRepository.findByTravelCode(travelCode).block();
        if(travelPlanEntity == null) {
            throw new RuntimeException("TravelPlan not found");
        }

        // 사용자가 참여자 명단에 없는 경우
        if(!(travelPlanEntity.getParticipants().contains(email) || travelPlanEntity.getFounder().equals(email))) {
            throw new RuntimeException("Participant not found");
        }

        // 지불인이 참여자 명단에 없는 경우
        if(!(travelPlanEntity.getParticipants().contains(expenditureEntity.getPayer()) || travelPlanEntity.getFounder().equals(expenditureEntity.getPayer()))) {
            throw new RuntimeException("Payer not found");
        }

        expendituresRepository.deleteByExpenditureId(expenditureId);

        return expendituresRepository.findAllByTravelCode(travelCode);
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

}
