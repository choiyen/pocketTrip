package project.backend.Entity;


import com.mongodb.lang.NonNull;
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
    @NonNull
    private String purpose;
    @NonNull
    private String type;
    @NonNull
    private String method;
    @NonNull
    private boolean isPublic;
    @NonNull
    private String payer;
    @NonNull
    private LocalDateTime date;
    @NonNull
    private int KRW;
    private int amount;
    private String currency;
    @NonNull
    private String description;
}
//기준 통화는 한국돈, 외국돈은 의무 입력 아님.
// 단. 외국돈 만 입력할 경우, 한국돈을 계산해서 집어넣는 기능 있어야 함.
