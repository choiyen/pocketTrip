package project.backend.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
public class ExpendituresDTO {
    private String id;
    private String expenditureId;
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
}
