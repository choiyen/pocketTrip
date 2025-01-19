package project.backend.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "TravelPlan")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlanEntity
{
    @Id
    private String Id;
    private String travelCode;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int expense;
    private String founder;
    private List<String> participants;
    private boolean isCalculate;
}
