package project.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Repository.TravelPlanRepository;

@Service
public class TravelPlanService
{
    @Autowired
    private TravelPlanRepository travelPlanRepository;

    public void TravelPlanInsert(TravelPlanEntity travelPlan)
    {
        travelPlanRepository.save(travelPlan);
    }

    public void TravelPlanUpdate(TravelPlanEntity travelPlan)
    {
        travelPlanRepository.save(travelPlan);
    }

    public void TravelPlanDelete(String id)
    {
        travelPlanRepository.deleteById(id);
    }

    public void TravelPlanSelect(String travelCode)
    {
        travelPlanRepository.findByTravelCode(travelCode);
    }




}
