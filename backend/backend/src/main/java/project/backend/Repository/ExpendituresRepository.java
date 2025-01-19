package project.backend.Repository;

import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import project.backend.Entity.ExpenditureEntity;

@Repository
public interface ExpendituresRepository extends ReactiveMongoRepository<ExpenditureEntity, String>
{
}
