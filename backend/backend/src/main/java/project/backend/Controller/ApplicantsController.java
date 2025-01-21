package project.backend.Controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.ApplicantsDTO;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.TravelPlanDTO;
import project.backend.Entity.ApplicantsEntity;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Service.AppllicantsService;
import project.backend.Service.TravelPlanService;

@Slf4j
@RestController
@RequestMapping("/Applicants")
public class ApplicantsController
{
    @Autowired
    private TravelPlanService travelPlanService;

    @Autowired
    private AppllicantsService appllicantsService;


    //Travel에 데이터가 있을 때만 DB에서 insert 되게 설정
    @PostMapping("/insert/{Travelcode}")
    public ResponseEntity<?> ApplicantsInsert(@PathVariable(value = "Travelcode") String Travelcode, @RequestBody String userid)
    {
        try
        {
            if(travelPlanService.SelectTravelCode(Travelcode) == true)
            {
                ApplicantsEntity applicantsEntity = appllicantsService.ApllicantsInsert(Travelcode, userid).block();
                ApplicantsDTO travelPlanDTO1 = ConvertTo(applicantsEntity);
                return ResponseEntity.ok().body(travelPlanDTO1);
            }
            else
            {
                log.warn("The data with Travelcode {} is not present in the travel document", Travelcode);
                throw new IllegalArgumentException("Travelcode 값이 없는데 넣는 것은 불가능");
                //The data for that travel code is not present in the travel document.
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
    @PostMapping("/Delete/{Travelcode}")
    public ResponseEntity<?> ApplicantsDelete(@PathVariable(value = "Travelcode") String Travelcode, @RequestBody String userid)
    {
        try
        {

            if(travelPlanService.SelectTravelCode(Travelcode) == true)
            {
                ApplicantsEntity applicantsEntity = (ApplicantsEntity) appllicantsService.AppllicantsDelete(Travelcode, userid).block();
                ApplicantsDTO travelPlanDTO1 = ConvertTo(applicantsEntity);
                return ResponseEntity.ok().body(travelPlanDTO1);
            }
            else
            {
                log.warn("The data with Travelcode {} is not present in the travel document", Travelcode);
                throw new IllegalArgumentException("Travelcode 값이 없는데 넣는 것은 불가능");
                //The data for that travel code is not present in the travel document.
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



    public ApplicantsEntity ConvertTo(ApplicantsDTO Applicants)
    {
        ApplicantsEntity Applicantsed = ApplicantsEntity.builder()
                .travelCode(Applicants.getTravelcode())
                .userList(Applicants.getUserList())
                .build();

        return  Applicantsed;
    }
    public ApplicantsDTO ConvertTo(ApplicantsEntity applicants)
    {
        ApplicantsDTO applicantsDTO = ApplicantsDTO.builder()
                .travelcode(applicants.getTravelCode())
                .userList(applicants.getUserList())
                .build();

        return  applicantsDTO;
    }
}
