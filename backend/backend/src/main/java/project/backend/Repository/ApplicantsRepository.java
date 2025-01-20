package project.backend.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import project.backend.Entity.ExpenditureEntity;

@Repository
public interface ApplicantsRepository extends ReactiveMongoRepository<ExpenditureEntity, String>
{
}
