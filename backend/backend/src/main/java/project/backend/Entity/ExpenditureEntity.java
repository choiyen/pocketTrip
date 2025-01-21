package project.backend.Entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "expenditures")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class ExpenditureEntity
{
    @Id
    private String id;
    private String travelCode;
    private String purpose;
    private String method;
    private boolean isPublic;
    private String payer;
    private LocalDate date;
    private int KRW;
    private int amount;
    private String currency;
    private String description;

    public static ExpenditureEntity of(String purpose, String travelCode, String method, boolean isPublic, String payer, LocalDate date, int KRW, int amount, String currency, String description) {
        return ExpenditureEntity.builder()
                .purpose(purpose)
                .travelCode(travelCode)
                .method(method)
                .isPublic(isPublic)
                .payer(payer)
                .date(date)
                .KRW(KRW)
                .amount(amount)
                .currency(currency)
                .description(description)
                .build();
    }
}
