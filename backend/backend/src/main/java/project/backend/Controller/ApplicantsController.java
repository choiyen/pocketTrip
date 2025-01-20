package project.backend.Controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.DTO.ApplicantsDTO;
import project.backend.Entity.ApplicantsEntity;

@RestController
@RequestMapping("/Applicants")
public class ApplicantsController
{
    public ApplicantsEntity ConvertTo(ApplicantsDTO Applicants)
    {
        ApplicantsEntity Applicantsed = ApplicantsEntity.builder()
                .travelCode(Applicants.getTravelcode())
                .userList(Applicants.getUserList())
                .build();

        return  Applicantsed;
    }
}
