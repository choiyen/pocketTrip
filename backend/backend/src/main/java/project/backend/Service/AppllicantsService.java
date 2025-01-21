package project.backend.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.ApplicantsEntity;
import project.backend.Repository.ApplicantsRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppllicantsService
{

    @Autowired
    private ApplicantsRepository applicantsRepository;

    public Mono<ApplicantsEntity> ApllicantsInsert(String TravelCode, String userid)
    {
        if(applicantsRepository.existsByTravelCode(TravelCode) == true)
        {
            ApplicantsEntity applicantsEntity = applicantsRepository.findByTravelCode(TravelCode).block();
            List<String> uplist = new ArrayList<>();
            uplist.addAll(applicantsEntity.getUserList());
            uplist.add(userid);
            ApplicantsEntity applicants = ApplicantsEntity.builder()
                    .travelCode(TravelCode)
                    .userList(uplist)
                    .build();

            return applicantsRepository.save(applicants);
        }
        else
        {
            List<String> uplist = new ArrayList<>();
            uplist.add(userid);
            ApplicantsEntity applicants = ApplicantsEntity.builder()
                    .travelCode(TravelCode)
                    .userList(uplist)
                    .build();

            return applicantsRepository.save(applicants);

        }

    }

    //참여자 목록에 데이터가 있을 때 데이터 삭제, 데이터 삭제 실행 후 빈배열일 경우, 해당 Document 제거
    public Mono<? extends Object> AppllicantsDelete(String TravelCode, String userid)
    {
       ApplicantsEntity applicantsEntity = applicantsRepository.findByTravelCode(TravelCode).block();
       List allList = applicantsEntity.getUserList();
       allList.remove(String.valueOf(userid));

        if(allList.isEmpty())
        {
           return applicantsRepository.delete(applicantsEntity);
        }
        else
        {
            ApplicantsEntity applicants = ApplicantsEntity.builder()
                    .userList(allList)
                    .travelCode(applicantsEntity.getTravelCode())
                    .build();
            return applicantsRepository.save(applicants);
        }
    }

    public Boolean ApplicantExistance(String TravelCode)
    {
        return applicantsRepository.existsByTravelCode(TravelCode);
    }

    public void TravelPlanAllDelete(String TravelCode)
    {
        applicantsRepository.deleteByTravelCode(TravelCode).block();
    }

}
