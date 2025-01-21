package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ExpenditureEntity;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Repository.ExpendituresRepository;
import project.backend.Repository.TravelPlanRepository;

@Slf4j
@Service
public class ExpenditureService {

    @Autowired
    private ExpendituresRepository expendituresRepository;
    @Autowired
    private TravelPlanRepository travelPlanRepository;

    public ExpenditureEntity create(ExpenditureEntity expenditureEntity) {
        validate(expenditureEntity);

        expendituresRepository.save(expenditureEntity);

        log.info("Entity Id: {} is saved", expenditureEntity.getId());

        return expenditureEntity;
    }

    // 유효성 검사
    private void validate(ExpenditureEntity expenditureEntity){
        if(expenditureEntity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }

        TravelPlanEntity travelPlan = travelPlanRepository.findByTravelCode(expenditureEntity.getTravelCode());

        if(!travelPlan.getParticipants().contains(expenditureEntity.getPayer())){
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }
}
