package project.backend.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import project.backend.Entity.UserEntity;

import java.util.Optional;

@Repository("mongoUserRepository")  // MongoDB 용 빈 이름 명시적으로 설정
public interface UserRepository extends MongoRepository<UserEntity, String> {
    // 해당 아이디를 가진 사용자가 존재하는지 여부 확인
    Boolean existsByEmail(String userid);

    // 주어진 아이디로 사용자 찾아 반환
    UserEntity findByEmail(String userid);
}
