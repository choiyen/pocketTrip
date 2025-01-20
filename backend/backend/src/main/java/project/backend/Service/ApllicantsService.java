package project.backend.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.DTO.ApplicantsDTO;
import project.backend.Entity.ApplicantsEntity;
import project.backend.Repository.ApplicantsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApllicantsService
{

    @Autowired
    private ApplicantsRepository applicantsRepository;

    public void ApllicantsInsert(ApplicantsEntity Appllicants)
    {
        applicantsRepository.save(Appllicants);
    }
    public void ApplicantsUpdate(ApplicantsEntity Applicants)
    {
        applicantsRepository.save(Applicants);
    }
    public void ApllicantsDelete(String TravelCode, String userid)
    {
       ApplicantsEntity applicantsEntity = applicantsRepository.findByTravelCode(TravelCode);
       List allList = applicantsEntity.getUserList();
       allList.remove(String.valueOf(userid));
       ApplicantsEntity applicants = ApplicantsEntity.builder()
               .userList(allList)
               .travelCode(applicantsEntity.getTravelCode())
                                    .build();
       applicantsRepository.save(applicants);

    }

}
