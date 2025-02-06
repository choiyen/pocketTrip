package project.backend.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.Entity.TravelPlanEntity;
import project.backend.Entity.UserEntity;
import project.backend.Entity.UserTravelsEntity;
import project.backend.Repository.TravelPlanRepository;
import project.backend.Repository.UserTravelsRepository;

import java.util.ArrayList;

@Slf4j
@Service
public class UserTravelsService {

    @Autowired
    private UserTravelsRepository userTravelsRepository;
    @Autowired
    private TravelPlanRepository travelPlanRepository;

    public UserTravelsEntity createUserTravels(UserTravelsEntity userTravelsEntity) {
        if(userTravelsEntity == null || userTravelsEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        String userid = userTravelsEntity.getEmail();

        if(userTravelsRepository.existsByEmail(userid)) {
            log.warn("UserTravels with id {} already exists", userid);
            throw new RuntimeException("UserTravels with id " + userid + " already exists");
        }

        return userTravelsRepository.save(userTravelsEntity);
    }

    public ArrayList<TravelPlanEntity> findTravelList(String email){
        UserTravelsEntity userTravels = userTravelsRepository.findByEmail(email);
        ArrayList<String> travelList = userTravels.getTravelList();
        ArrayList<TravelPlanEntity> travelPlans = new ArrayList<>();
        for (String travelCode : travelList) {
            TravelPlanEntity travelPlan = travelPlanRepository.findByTravelCode(travelCode).block();
            if (travelPlan != null) {
                travelPlans.add(travelPlan);
            }
        }
        return travelPlans;
    }

    public UserTravelsEntity insertUserTravels(String userId, String travelCode) {
        UserTravelsEntity userTravels = userTravelsRepository.findByEmail(userId);

        userTravels.getTravelList().add(travelCode);

        return userTravelsRepository.save(userTravels);
    }
}
