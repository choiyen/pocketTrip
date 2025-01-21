package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Repository.TravelPlanRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Slf4j
@Service
public class TravelPlanService
{
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

    public boolean SelectTravelCode(String travelCode)
    {
        Boolean bool = travelPlanRepository.existsByTravelCode(travelCode).block();
        return  bool;
    }




}
