package project.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String userid;
    private String password;
    private String name;
    private String email;
    private String phone;
}
