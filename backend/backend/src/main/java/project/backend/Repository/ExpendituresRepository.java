package project.backend.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import project.backend.Entity.ExpenditureEntity;
import reactor.core.publisher.Flux;


@Repository
public interface ExpendituresRepository extends ReactiveMongoRepository<ExpenditureEntity, String>
{
    Flux<ExpenditureEntity> findAllByTravelCode(String travelCode);
}
