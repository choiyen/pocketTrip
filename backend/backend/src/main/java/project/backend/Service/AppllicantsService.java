package project.backend.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ApplicantsEntity;
import project.backend.Repository.ApplicantsRepository;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AppllicantsService
{

    @Autowired
    private ApplicantsRepository applicantsRepository;

    public Mono<ApplicantsEntity> ApllicantsInsert(ApplicantsEntity Appllicants)
    {
       return  applicantsRepository.save(Appllicants);
    }
    public Mono<ApplicantsEntity> ApplicantsUpdate(ApplicantsEntity Applicants)
    {
       return applicantsRepository.save(Applicants);
    }

    //참여자 목록에 데이터가 있을 때 데이터 삭제, 데이터 삭제 실행 후 빈배열일 경우, 해당 Document 제거
    public void AppllicantsDelete(String TravelCode, String userid)
    {
       ApplicantsEntity applicantsEntity = applicantsRepository.findByTravelCode(TravelCode);
       List allList = applicantsEntity.getUserList();
       allList.remove(String.valueOf(userid));
        ApplicantsEntity applicants = ApplicantsEntity.builder()
                .userList(allList)
                .travelCode(applicantsEntity.getTravelCode())
                .build();
        applicantsRepository.save(applicants);
       if(allList.isEmpty())
       {
           applicantsRepository.delete(applicantsEntity);
       }
    }
}
