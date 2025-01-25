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


    // 로그인
    public UserEntity getByCredentials(String email, String password, PasswordEncoder passwordEncoder) {
        UserEntity originalUser = userRepository.findByEmail(email);
        if(originalUser != null && passwordEncoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
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
}
