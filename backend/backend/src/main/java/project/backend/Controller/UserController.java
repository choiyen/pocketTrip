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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    private ResponseDTO responseDTO;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> registeredUser(@RequestBody UserDTO userDTO){
        try
        {
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

            List<Object> list = new ArrayList<>();
            list.add(responsedUserDTO);
            return ResponseEntity.ok().body(responseDTO.Response("success", "우리 앱을 이용해주셔서 감사합니다. 여러분의 기입을 환영합니다.", list));

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage(), null));
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
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage(), null));
        }
    }

    // 수정
    @PutMapping("/edit")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal String userId, @RequestBody UserDTO userDTO){
        try
        {
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

            List<Object> list = new ArrayList<>();
            list.add(responsedUserDTO);
            return ResponseEntity.ok().body(responseDTO.Response("info", "회원정보 수정 완료!", list));

        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage(), null));
        }
    }
}
