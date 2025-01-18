package project.backend.Repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import project.backend.Entity.TravelPlanEntity;

@Repository
public interface TravelPlanRepository extends MongoRepository<TravelPlanEntity, String>
{

}
