package project.backend.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TravelPlanDTO {
    private String id;
    private String travelcode;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int expense;
    private String founder;
    private List<String> participants;
    private boolean isCalculate;
}
