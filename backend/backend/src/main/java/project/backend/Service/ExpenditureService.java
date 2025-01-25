package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ExpenditureEntity;
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

    public ExpenditureEntity create(ExpenditureEntity expenditureEntity) {

        return expendituresRepository.save(expenditureEntity).block();
    }

    public Flux<ExpenditureEntity> findAllByTravelCode(String travelCode) {
        return expendituresRepository.findAllByTravelCode(travelCode);
    }

    public boolean SelectExpenditureId(String expenditureId) throws Exception {
        Boolean bool = expendituresRepository.existsByExpenditureId(encrypt(expenditureId ,key)).block();
        return  bool;
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

    public Mono<ExpenditureEntity> updateExpenditure(String email, String expenditureId, ExpenditureEntity expenditureEntity) {
        return validate(email, expenditureEntity) // 검증
                .then(expendituresRepository.findByExpenditureId(expenditureId)) // 기존 데이터 조회
                .switchIfEmpty(Mono.error(new RuntimeException("Expenditure not found: " + expenditureId))) // 존재하지 않으면 에러 반환
                .flatMap(originalEntity -> {
                    // 업데이트 로직
                    originalEntity.setPurpose(expenditureEntity.getPurpose());
                    originalEntity.setMethod(expenditureEntity.getMethod());
                    originalEntity.setPublic(expenditureEntity.isPublic());
                    originalEntity.setPayer(expenditureEntity.getPayer());
                    originalEntity.setDate(expenditureEntity.getDate());
                    originalEntity.setKRW(expenditureEntity.getKRW());
                    originalEntity.setAmount(expenditureEntity.getAmount());
                    originalEntity.setCurrency(expenditureEntity.getCurrency());
                    originalEntity.setDescription(expenditureEntity.getDescription());
                    return expendituresRepository.save(originalEntity); // 업데이트 후 저장
                })
                .doOnError(e -> log.error("Error updating expenditure: {}", e.getMessage()));
    }

    public Flux<ExpenditureEntity> deleteExpenditure(String email, String travelCode, String expenditureId) {
        return expendituresRepository.findByExpenditureId(expenditureId)
                .switchIfEmpty(Mono.error(new RuntimeException("Expenditure with id " + expenditureId + " does not exist")))
                .flatMap(expenditure ->
                        validate(email, expenditure) // 삭제 전 검증
                                .then(expendituresRepository.deleteByExpenditureId(expenditureId)) // 삭제
                                .thenReturn(travelCode) // travelCode 반환
                )
                .flatMapMany(code -> expendituresRepository.findAllByTravelCode(code)); // travelCode로 지출 목록 반환
    }



    public Mono<Void> validate(String email, ExpenditureEntity expenditureEntity) {
        if (expenditureEntity == null) {
            log.warn("Entity cannot be null");
            return Mono.error(new RuntimeException("Entity cannot be null"));
        }

        return travelPlanRepository.findByTravelCode(expenditureEntity.getTravelCode())
                .flatMap(travelPlan -> {
                    if (travelPlan == null) {
                        log.warn("Travel code {} is not found", expenditureEntity.getTravelCode());
                        return Mono.error(new RuntimeException("Travel code not found"));
                    }
                    if (!travelPlan.getParticipants().contains(expenditureEntity.getPayer())) {
                        log.warn("Unknown user");
                        return Mono.error(new RuntimeException("Unknown user"));
                    }
                    if (!travelPlan.getParticipants().contains(email) && !travelPlan.getFounder().equals(email)) {
                        log.warn("Unknown user");
                        return Mono.error(new RuntimeException("Unknown user"));
                    }
                    return Mono.empty(); // Validation passed
                });
    }
}
