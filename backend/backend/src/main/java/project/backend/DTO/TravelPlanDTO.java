package project.backend.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class TravelPlanDTO
{

    private String travelCode;
    private String title;
    private String location;
    private String img;
    private LocalDate startDate;
    private LocalDate endDate;
    private int expense;
    private String founder;
    private Set<String> participants;
    private boolean isCalculate;
    private List<String> profiles;
}
//소켓으로 알림으로 보냄
