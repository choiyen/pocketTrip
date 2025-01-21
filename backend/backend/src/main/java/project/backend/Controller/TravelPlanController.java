package project.backend.Controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.TravelPlanDTO;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Service.AppllicantsService;
import project.backend.Service.TravelPlanService;
import reactor.core.publisher.Mono;

/*
MongoDB에서는 컬렉션이 데이터가 실제로 삽입될 때 자동으로 생성됩니다.
즉, MongoRepository를 통해 데이터를 삽입하려고 시도했을 때, 컬렉션이 자동으로 생성됩니다
 */
@RestController
@RequestMapping("/plan")
public class TravelPlanController
{
    @Autowired
    private TravelPlanService travelPlanService;

    @Autowired
    private AppllicantsService appllicantsService;

    //정상적으로 동작 되어짐 확인
    @PostMapping("/insert")
    public ResponseEntity<?> TravelInsert(@AuthenticationPrincipal String userId,  @RequestBody TravelPlanDTO travelPlanDTO)
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
    //유출되도 상관 없을 것 같은 데이터(기능 동작 확인)
    @PostMapping("/update/{id}")
    public ResponseEntity<?> TravelUpdate( @PathVariable(value = "id") String id, @RequestBody TravelPlanDTO travelPlanDTO)
    {
        try
        {
            if(travelPlanService.SelectTravelCode(id) == true)
            {
                TravelPlanEntity Oldtravelplan = travelPlanService.TravelPlanSelect(id).block();
                TravelPlanEntity travelPlan = ConvertTo(id, travelPlanDTO, Oldtravelplan.getId());
                TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanUpdate(travelPlan).block();
                TravelPlanDTO travelPlanDTO1 = ConvertTo(travelPlan1);
                return ResponseEntity.ok().body(travelPlanDTO1);
            }
            else
            {
                ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                        .error("해당 여행 코드의 기록을 찾을 수 없습니다.")
                        .build();
                return ResponseEntity.badRequest().body(responseDTO);
            }

        }
        catch (Exception e)
        {
            ResponseDTO<Object> responseDTO = ResponseDTO.builder()
                    .error(e.getMessage())
                    .build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

    }

    //travelcode에 따라 데이터 출력 확인
    @PostMapping("/select/{travelCode}")
    public ResponseEntity<?> TravelSelect(@PathVariable(value = "travelCode") String travelCode)
    {
        try
        {
            TravelPlanEntity travelPlan1 = travelPlanService.TravelPlanSelect(travelCode).block();
            Thread.sleep(2000);
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

    //데이터베이스에서 데이터 정상 제거 확인
    @PostMapping("/delete/{travelCode}")
    public ResponseEntity<?> TravelDelete(@PathVariable(value = "travelCode") String travelCode)
    {
        try
        {
            if(travelPlanService.SelectTravelCode(travelCode) == true)
            {
                travelPlanService.TravelPlanDelete(travelCode);
                if(appllicantsService.ApplicantExistance(travelCode).block() == true)
                //mongoDB는 NoSql이라 관계에 의한 cascade 삭제를 지원하지 않아 관련 처리 진행
                {
                    appllicantsService.TravelPlanAllDelete(travelCode);
                }
                return ResponseEntity.ok().body("정상적으로 데이터 제거가 완료되었습니다.");
            }
            else
            {
                throw  new RuntimeException("해당 여행 코드를 가진 데이터를 찾을 수 없습니다. 다시 확인해주세요...");
            }

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
                .travelCode(travelPlanDTO.getTravelCode())
                .location(travelPlanDTO.getLocation())
                .startDate(travelPlanDTO.getStartDate())
                .endDate(travelPlanDTO.getEndDate())
                .expense(travelPlanDTO.getExpense())
                .founder(travelPlanDTO.getFounder())
                .participants(travelPlanDTO.getParticipants())
                .isCalculate(travelPlanDTO.isCalculate())
                .id(travelPlanDTO.getId())
                .build();

        return travelPlan;
    }

    public TravelPlanEntity ConvertTo(String travelCode, TravelPlanDTO travelPlanDTO, String id)
    {
        TravelPlanEntity travelPlan = TravelPlanEntity.builder()
                .travelCode(travelPlanDTO.getTravelCode())
                .travelCode(travelCode)
                .location(travelPlanDTO.getLocation())
                .startDate(travelPlanDTO.getStartDate())
                .endDate(travelPlanDTO.getEndDate())
                .expense(travelPlanDTO.getExpense())
                .founder(travelPlanDTO.getFounder())
                .participants(travelPlanDTO.getParticipants())
                .isCalculate(travelPlanDTO.isCalculate())
                .id(id)
                .build();

        return travelPlan;
    }


    public TravelPlanDTO ConvertTo(TravelPlanEntity travelPlanEntity)
    {
        TravelPlanDTO travelPlan = TravelPlanDTO.builder()
                .travelCode(travelPlanEntity.getTravelCode())
                .location(travelPlanEntity.getLocation())
                .startDate(travelPlanEntity.getStartDate())
                .endDate(travelPlanEntity.getEndDate())
                .expense(travelPlanEntity.getExpense())
                .founder(travelPlanEntity.getFounder())
                .participants(travelPlanEntity.getParticipants())
                .isCalculate(travelPlanEntity.isCalculate())
                .id(travelPlanEntity.getId())
                .build();

        return travelPlan;
    }

    //마지막 과제 : 여행 날짜 위주로 데이터 정렬 후 불러오기
}
