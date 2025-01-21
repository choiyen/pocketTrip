package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ExpenditureEntity;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Repository.ExpendituresRepository;
import project.backend.Repository.TravelPlanRepository;
import reactor.core.publisher.Mono;

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

    private Mono<Void> validate(ExpenditureEntity expenditureEntity) {
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

                    return Mono.empty(); // Validation passed
                });
    }

}
