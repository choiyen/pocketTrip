package project.backend.Entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Entity
@Document(collection = "TravelPlan")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlanEntity
{
    private String travelCode;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int expense;
    private String founder;
    private String participants;
    private String isCalculate;
}
