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

import javax.naming.AuthenticationException;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Map<String, String> codeStorage = new HashMap<>(); // 전화번호와 인증 코드 저장
    private final Map<String, Long> expiryStorage = new HashMap<>(); // 코드 만료 시간 저장


    @Autowired
    private TokenProvider tokenProvider;

    private ResponseDTO responseDTO = new ResponseDTO<>();

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registeredUser(@RequestBody UserDTO userDTO){
        try
        {
            UserEntity user = UserEntity.builder()
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .phone(userDTO.getPhone())
                    .build();

            UserEntity registerUser = userService.createUser(user);

            UserDTO responsedUserDTO = UserDTO.builder()
                    .name(registerUser.getName())
                    .email(registerUser.getEmail())
                    .password(registerUser.getPassword())
                    .phone(registerUser.getPhone())
                    .id(registerUser.getId())
                    .build();

            List<Object> list = new ArrayList<>();
            list.add(responsedUserDTO);
            return ResponseEntity.ok().body(responseDTO.Response("success", "우리 앱을 이용해주셔서 감사합니다. 여러분의 기입을 환영합니다.", list));

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }


    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
        try{
            UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword(), passwordEncoder);
            System.out.println(user.getEmail());
            if(user != null)
            {
                String token = tokenProvider.createToken(user);
                UserDTO responseUserDTO = UserDTO.builder()
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .phone(user.getPhone())
                        .id(user.getId())
                        .token(token)
                        .build();
                List<Object> list = new ArrayList<>();
                list.add(responseUserDTO);
                return ResponseEntity.ok().body(responseDTO.Response("success", "오늘도 저희 서비스에 방문해주셔서 감사드려요!!!", list));
            }
            else
            {
               throw new AuthenticationException("회원정보가 존재하지 않습니다. 비밀번호나 아이디를 다시 입력해주세요.");
            }
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }

    // 수정
    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal String userId, @RequestBody UserDTO userDTO){
        try
        {
            UserEntity user = UserEntity.builder()
                    .name(userDTO.getName())
                    .email(userDTO.getEmail())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .phone(userDTO.getPhone())
                    .build();

            UserEntity editUser = userService.updateUser(userId, user);

            UserDTO responsedUserDTO = UserDTO.builder()
                    .name(editUser.getName())
                    .email(editUser.getEmail())
                    .password(editUser.getPassword())
                    .phone(editUser.getPhone())
                    .build();

            List<Object> list = new ArrayList<>();
            list.add(responsedUserDTO);
            return ResponseEntity.ok().body(responseDTO.Response("info", "회원정보 수정 완료!", list));

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }
    }
}
