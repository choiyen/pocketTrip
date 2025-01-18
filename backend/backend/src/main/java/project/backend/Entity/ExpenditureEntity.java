package project.backend.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Entity
@Document(collection = "expenditures")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ExpenditureEntity
{
    @Id
    private String id;
    private String purpose;
    private String type;
    private String method;
    private boolean isPublic;
    private String payer;
}
