package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Repository.TravelPlanRepository;
import reactor.core.publisher.Mono;

@Service
public class TravelPlanService
{
    @Autowired
    private TravelPlanRepository travelPlanRepository;

    public Mono<TravelPlanEntity> TravelPlanInsert(TravelPlanEntity travelPlan)
    {
        // 유효성 검사 1) userEntity 혹은 email 이 null 인 경우 예외 던짐
        if (travelPlan == null) {
            throw new RuntimeException("Invalid arguments");
        }

       return travelPlanRepository.save(travelPlan);
    }

    public Mono<TravelPlanEntity> TravelPlanUpdate(TravelPlanEntity travelPlan)
    {
       return travelPlanRepository.save(travelPlan);
    }

    public void TravelPlanDelete(String id)
    {
        travelPlanRepository.deleteById(id);
    }

    public TravelPlanEntity TravelPlanSelect(String travelCode)
    {
       return travelPlanRepository.findByTravelCode(travelCode);
    }

    public boolean SelectTravelCode(String travelCode)
    {
        Boolean bool = travelPlanRepository.existsByTravelCode(travelCode);
        return  bool;
    }




}
