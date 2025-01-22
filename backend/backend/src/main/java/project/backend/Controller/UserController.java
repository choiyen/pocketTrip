package project.backend.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.UserDTO;
import project.backend.Entity.UserEntity;
import project.backend.Security.TokenProvider;
import project.backend.Service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registeredUser(@RequestBody UserDTO userDTO){
        try {
            UserEntity user = UserEntity.builder()
                    .name(userDTO.getName())
                    .userid(userDTO.getUserid())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .phone(userDTO.getPhone())
                    .email(userDTO.getEmail())
                    .build();

            UserEntity registerUser = userService.createUser(user);
            UserDTO responsedUserDTO = UserDTO.builder()
                    .name(registerUser.getName())
                    .userid(registerUser.getUserid())
                    .password(registerUser.getPassword())
                    .phone(registerUser.getPhone())
                    .email(registerUser.getEmail())
                    .id(registerUser.getId())
                    .build();

            return ResponseEntity.ok().body(responsedUserDTO);
        } catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
//            throw new RuntimeException(e);

            return ResponseEntity.ok().body(responseDTO);
        }
    }


    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
        try{
            UserEntity user = userService.getByCredentials(userDTO.getUserid(), userDTO.getPassword(), passwordEncoder);
            System.out.println(user.getUserid());
            if(user != null)
            {
                String token = tokenProvider.createToken(user);
                UserDTO responseUserDTO = UserDTO.builder()
                        .name(user.getName())
                        .userid(user.getUserid())
                        .password(user.getPassword())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .id(user.getId())
                        .token(token)
                        .build();
                return ResponseEntity.ok().body(responseUserDTO);
            } else {
                ResponseDTO responseDTO = ResponseDTO.builder()
                        .error("Login Failed")
                        .build();
                return ResponseEntity.ok().body(responseDTO);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 수정
    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal String userId, @RequestBody UserDTO userDTO){
        try {
            UserEntity user = UserEntity.builder()
                    .name(userDTO.getName())
                    .userid(userId)
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .phone(userDTO.getPhone())
                    .email(userDTO.getEmail())
                    .build();

            UserEntity editUser = userService.updateUser(userId, user);

            UserDTO responsedUserDTO = UserDTO.builder()
                    .name(editUser.getName())
                    .userid(editUser.getUserid())
                    .password(editUser.getPassword())
                    .phone(editUser.getPhone())
                    .email(editUser.getEmail())
                    .build();

            return ResponseEntity.ok().body(responsedUserDTO);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
