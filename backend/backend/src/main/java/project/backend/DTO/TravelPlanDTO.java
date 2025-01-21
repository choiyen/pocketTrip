package project.backend.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class TravelPlanDTO {
    private String id;
    private String travelcode;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private int expense;
    private String founder;
    private Set<String> participants;
    private boolean isCalculate;
}
