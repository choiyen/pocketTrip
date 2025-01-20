package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.TravelPlanDTO;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Service.TravelPlanService;
import reactor.core.publisher.Mono;

/*
MongoDB에서는 컬렉션이 데이터가 실제로 삽입될 때 자동으로 생성됩니다.
즉, MongoRepository를 통해 데이터를 삽입하려고 시도했을 때, 컬렉션이 자동으로 생성됩니다
 */
@RestController
public class TravelPlanController
{
    @Autowired
    private TravelPlanService travelPlanService;


    @PostMapping("/insert")
    public ResponseEntity<?> TravelInsert(@RequestBody TravelPlanDTO travelPlanDTO)
    {
        try
        {
            TravelPlanEntity travelPlan = ConvertTo(travelPlanDTO);
            TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanInsert(travelPlan).block();
            TravelPlanDTO travelPlanDTO1 = ConvertTo(travelPlan1);
            return ResponseEntity.ok().body(travelPlanDTO1);
        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    @PostMapping("/update")
    public ResponseEntity<?> TravelUpdate(@RequestBody TravelPlanDTO travelPlanDTO)
    {
        try {
            TravelPlanEntity travelPlan = ConvertTo(travelPlanDTO);
            TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanUpdate(ConvertTo(travelPlanDTO)).block();
            TravelPlanDTO travelPlanDTO1 = ConvertTo(travelPlan1);
            return ResponseEntity.ok().body(travelPlanDTO1);

        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }
    @PostMapping("/select")
    public ResponseEntity<?> TravelSelect(@RequestParam String travelCode)
    {
        try
        {
            TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanSelect(travelCode);
            return ResponseEntity.ok().body(travelPlan1);
        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<?> TravelDelete(@RequestParam String travelCode)
    {
        try {
            travelPlanService.TravelPlanDelete(travelCode);
            return ResponseEntity.ok().body("정상적으로 데이터 제거가 완료되었습니다.");

        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

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
