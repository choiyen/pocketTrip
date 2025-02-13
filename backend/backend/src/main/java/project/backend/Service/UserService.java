package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.backend.Entity.UserEntity;
import project.backend.Repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MongoTemplate mongoTemplate;
    // 회원가입
    public UserEntity createUser(UserEntity userEntity) {
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        String userid = userEntity.getEmail();

        if(userRepository.existsByEmail(userid)) {
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
        UserEntity updatedUser = userRepository.save(originalUser);

        return updatedUser;
    }

    public List<String> getprofileByEmail(List<String> emails)
    {
       return userRepository.findProfileUrlByEmailIn(emails);
    }
}
