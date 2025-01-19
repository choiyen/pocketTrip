package project.backend.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import project.backend.Entity.UserEntity;


@Repository
public interface UserRepository extends ReactiveMongoRepository<UserEntity, String>
{
}
