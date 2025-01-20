package project.backend.Entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
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
    private String id;
    private String travelCode;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int expense;
    private String founder;
    private List<String> participants;
    private boolean isCalculate;
}
