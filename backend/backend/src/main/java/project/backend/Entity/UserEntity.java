package project.backend.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity
{
    @Id
    private String id;
    private String userid;
    private String password;
    private String name;
    private String email;
    private String phone;
}
