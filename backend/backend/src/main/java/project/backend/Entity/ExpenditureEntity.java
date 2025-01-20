package project.backend.Entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

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
    private LocalDateTime date;
    private int KRW;
    private int amount;
    private String currency;
    private String description;
}
