package project.backend.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import project.backend.Entity.ApplicantsEntity;
import project.backend.Entity.ExpenditureEntity;
import project.backend.Entity.TravelPlanEntity;

@Repository
public interface ApplicantsRepository extends ReactiveMongoRepository<ApplicantsEntity, String>
{
    ApplicantsEntity findByTravelCode(String travelCode);
}
