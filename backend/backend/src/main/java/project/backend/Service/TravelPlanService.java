package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Repository.TravelPlanRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


@Slf4j
@Service
public class TravelPlanService
{
    private final String key = "1234567890123456";

    @Autowired
    private TravelPlanRepository travelPlanRepository;

    public Mono<TravelPlanEntity> TravelPlanInsert(TravelPlanEntity travelPlan)
    {
       return travelPlanRepository.save(travelPlan);
    }

    public Mono<TravelPlanEntity> TravelPlanUpdate(TravelPlanEntity travelPlan)
    {

       return travelPlanRepository.save(travelPlan);
    }

    public void TravelPlanDelete(String TravelCode)
    {
       travelPlanRepository.deleteByTravelCode(TravelCode).block();
    }

    public Mono<TravelPlanEntity> TravelPlanSelect(String travelCode)
    {
        Mono<TravelPlanEntity> travelPlanEntityMono = travelPlanRepository.findByTravelCode(travelCode);
        if(travelPlanEntityMono.block() == null)
        {
            log.warn("travelPlan with Travel Code {} is not find", travelCode);
            throw new RuntimeException("해당 여행 코드를 가진 여행 기록을 찾을 수 없습니다.");
        }
        else
        {
            return travelPlanRepository.findByTravelCode(travelCode);

        }
    }

    public boolean SelectTravelCode(String travelCode) throws Exception {
        Boolean bool = travelPlanRepository.existsByTravelCode(encrypt(travelCode,key)).block();
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



    public Flux<TravelPlanEntity> travelPlanEntityAll(String userId)
    {
        return travelPlanRepository.findByFounder(userId,Sort.by(Sort.Order.asc("startDate")));
    }



}
