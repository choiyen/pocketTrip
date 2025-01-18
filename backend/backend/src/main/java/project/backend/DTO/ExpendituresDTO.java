package project.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExpendituresDTO {
    private String method;
    private boolean isPublic;
    private String payer;
    private LocalDateTime date;
    private int KRW;
    private String category;
    private String purpose;
}
