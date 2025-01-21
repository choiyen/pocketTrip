package project.backend.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.DTO.ApplicantsDTO;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.TravelPlanDTO;
import project.backend.Entity.ApplicantsEntity;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Service.AppllicantsService;


@RestController
@RequestMapping("/Applicants")
public class ApplicantsController
{

    @Autowired
    private AppllicantsService appllicantsService;

    @PostMapping("/insert")
    public ResponseEntity<?> ApplicantsInsert(@RequestBody ApplicantsDTO applicantsDTO)
    {
        try
        {
            ApplicantsEntity applicants = ConvertTo(applicantsDTO);
            ApplicantsEntity applicantsEntity = appllicantsService.ApllicantsInsert(applicants).block();
            ApplicantsDTO travelPlanDTO1 = ConvertTo(applicantsEntity);
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
