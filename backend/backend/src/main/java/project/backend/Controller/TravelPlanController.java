package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.TravelPlanDTO;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Service.TravelPlanService;

import java.time.LocalDateTime;

/*
MongoDB에서는 컬렉션이 데이터가 실제로 삽입될 때 자동으로 생성됩니다.
즉, MongoRepository를 통해 데이터를 삽입하려고 시도했을 때, 컬렉션이 자동으로 생성됩니다
 */
@RestController
@RequestMapping("/Plan")
public class TravelPlanController
{
    @Autowired
    private TravelPlanService travelPlanService;


    @PostMapping
    public void TravelInsert(@RequestBody TravelPlanDTO travelPlanDTO)
    {
//        private String travelCode;
//        private String location;
//        private LocalDateTime startDate;
//        private LocalDateTime endDate;
//        private int expense;
//        private String founder;
//        private String participants;
//        private String isCalculate;
        travelPlanService.TravelPlanInsert(ConvertTo(travelPlanDTO));
    }

    @PostMapping("/Travel")
    public void TravelUpdate(@RequestBody TravelPlanDTO travelPlanDTO)
    {
        travelPlanService.TravelPlanUpdate(ConvertTo(travelPlanDTO));
    }


    public TravelPlanEntity ConvertTo(TravelPlanDTO travelPlanDTO)
    {
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(travelPlanDTO.getTravelcode())
                .location(travelPlanDTO.getLocation())
                .startDate(travelPlanDTO.getStartDate())
                .endDate(travelPlanDTO.getEndDate())
                .expense(travelPlanDTO.getExpense())
                .founder(travelPlanDTO.getFounder())
                .participants(travelPlanDTO.getParticipants())
                .isCalculate(travelPlanDTO.isCalculate())
                .build();

        return travelPlan;
    }
    public TravelPlanDTO ConvertTo(TravelPlanEntity travelPlanEntity)
    {
        TravelPlanDTO travelPlan = TravelPlanDTO.builder()
                .travelcode(travelPlanEntity.getTravelCode())
                .location(travelPlanEntity.getLocation())
                .startDate(travelPlanEntity.getStartDate())
                .endDate(travelPlanEntity.getEndDate())
                .expense(travelPlanEntity.getExpense())
                .founder(travelPlanEntity.getFounder())
                .participants(travelPlanEntity.getParticipants())
                .isCalculate(travelPlanEntity.isCalculate())
                .build();

        return travelPlan;
    }
}
