package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.backend.Entity.UserEntity;
import project.backend.Repository.UserRepository;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 회원가입
    public UserEntity createUser(UserEntity userEntity) {
        if(userEntity == null || userEntity.getUserid() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        String userid = userEntity.getUserid();

        if(userRepository.existsById(userid)) {
            log.warn("User with id {} already exists", userid);
            throw new RuntimeException("User with id " + userid + " already exists");
        }

        return userRepository.save(userEntity);
    }


    // 로그인
    public UserEntity getByCredentials(String userid, String password, PasswordEncoder passwordEncoder) {
        UserEntity originalUser = userRepository.findByUserid(userid);
        if(originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }
}
