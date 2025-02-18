package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.backend.Entity.UserEntity;
import project.backend.Repository.UserRepository;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MongoTemplate mongoTemplate;
    // 회원가입
    public UserEntity createUser(UserEntity userEntity)
    {
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        String userid = userEntity.getEmail();

        if(userRepository.existsByEmail(userid))
        {
            log.warn("User with id {} already exists", userid);
            throw new RuntimeException("User with id " + userid + " already exists");
        }

        return userRepository.save(userEntity);
    }


    // 유저 정보 불러오기
    public UserEntity getUserInfo(String email) {
        UserEntity originalUser = userRepository.findByEmail(email);
        if(originalUser == null) {
            log.warn("User with email {} does not exist", email);
        }
        System.out.println(originalUser);
        return originalUser;
    }

    // 로그인
    public UserEntity getByCredentials(String email, String password, PasswordEncoder passwordEncoder) {
        UserEntity originalUser = userRepository.findByEmail(email);
        if(originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }

    //userID가 DB에 있는지 여부 확인
    public Boolean getUserID(String userId)
    {
        Boolean bool = userRepository.existsByEmail(userId);
        return bool;
    }
    // 수정하기
    public UserEntity updateUser(String email, UserEntity userEntity) {

        UserEntity originalUser = userRepository.findByEmail(email);
        if(originalUser == null) {
            log.warn("User with email {} does not exist", email);
        }

        originalUser.setPassword(userEntity.getPassword());
        originalUser.setEmail(userEntity.getEmail());
        originalUser.setName(userEntity.getName());
        originalUser.setPhone(userEntity.getPhone());
        originalUser.setProfile(userEntity.getProfile());
        UserEntity updatedUser = userRepository.save(originalUser);

        return updatedUser;
    }

    public String getprofileByEmail(String email) {

        // Aggregation pipeline 정의
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("email").is(email)),  // 이메일 필터링
                Aggregation.project("profile")  // profile만 반환
        );

        // MongoTemplate을 사용하여 쿼리 실행
        AggregationResults<UserEntity> result = mongoTemplate.aggregate(aggregation, UserEntity.class, UserEntity.class);

        // 결과에서 profile 반환
        if (result.getMappedResults().isEmpty())
        {
            return null;  // 결과가 없으면 null 반환
        }

        return result.getMappedResults().get(0).getProfile();  // 첫 번째 사용자 profile 반환
    }

    public void deteleUserID(String userId)
    {
        userRepository.deleteByEmail(userId);
    }

}
