package project.backend.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.DTO.ExpendituresDTO;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.UserDTO;
import project.backend.Entity.ExpenditureEntity;
import project.backend.Service.ExpenditureService;
import project.backend.Service.UserService;

import java.time.LocalDate;

/*
MongoDB에서는 컬렉션이 데이터가 실제로 삽입될 때 자동으로 생성됩니다.
즉, MongoRepository를 통해 데이터를 삽입하려고 시도했을 때, 컬렉션이 자동으로 생성됩니다
 */
@RestController
@RequestMapping("/expenditures")
public class ExpendituresController {

    @Autowired
    private ExpenditureService expenditureService;

    // 지출 추가
    @PostMapping
    public ResponseEntity<?> createExpenditure(@AuthenticationPrincipal String userId, @RequestBody ExpendituresDTO expendituresDTO){
        try {

            ExpenditureEntity expenditureEntity = ExpenditureEntity
                    .of(expendituresDTO.getPurpose(),
                            expendituresDTO.getTravelCode(),
                            expendituresDTO.getMethod(),
                            expendituresDTO.isPublic(),
                            userId,
                            expendituresDTO.getDate(),
                            expendituresDTO.getKRW(),
                            expendituresDTO.getAmount(),
                            expendituresDTO.getCurrency(),
                            expendituresDTO.getDescription());

            ExpenditureEntity createdEntity = expenditureService.create(expenditureEntity);

            ExpendituresDTO responsedDTO = ExpendituresDTO.builder()
                    .travelCode(createdEntity.getTravelCode())
                    .purpose(createdEntity.getPurpose())
                    .method(createdEntity.getMethod())
                    .isPublic(createdEntity.isPublic())
                    .payer(createdEntity.getPayer())
                    .date(createdEntity.getDate())
                    .KRW(createdEntity.getKRW())
                    .amount(createdEntity.getAmount())
                    .currency(createdEntity.getCurrency())
                    .description(createdEntity.getDescription())
                    .build();

            return ResponseEntity.ok().body(responsedDTO);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 지출 수정

    // 지출 삭제

    // 지출 목록
}



//마지막 과제 : 카드 별, 현금 별로 데이터 뽑아오는 함수 추가
