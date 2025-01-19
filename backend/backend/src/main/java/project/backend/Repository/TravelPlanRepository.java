package project.backend.Repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import project.backend.Entity.TravelPlanEntity;

@Repository
public interface TravelPlanRepository extends ReactiveMongoRepository<TravelPlanEntity, String>
{
    // 주어진 이메일을 가진 사용자가 존재하는지 여부 확인
    Boolean existsByTravelCode(String travelCode);

    // 주어진 travelCode로 여행정보를 찾아 반환
    TravelPlanEntity findByTravelCode(String travelCode);

}
