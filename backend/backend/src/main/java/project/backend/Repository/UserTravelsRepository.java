package project.backend.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import project.backend.Entity.UserTravelsEntity;

import java.util.ArrayList;

@Repository("mongoUserTravelsRepository")
public interface UserTravelsRepository extends MongoRepository<UserTravelsEntity, String> {

    UserTravelsEntity findByEmail(String email);

    boolean existsByEmail(String userid);
}
