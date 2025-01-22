package project.backend.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import project.backend.DTO.ExpendituresDTO;
import project.backend.DTO.ResponseDTO;
import project.backend.DTO.UserDTO;
import project.backend.Entity.ExpenditureEntity;
import project.backend.Entity.UserEntity;
import project.backend.Service.ExpenditureService;
import project.backend.Service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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
    @PostMapping("/{travelCode}")
    public ResponseEntity<?> createExpenditure(@AuthenticationPrincipal String userId, @RequestBody ExpendituresDTO expendituresDTO, @PathVariable String travelCode) {
        try {
            int leftLimit = 48; // numeral '0'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 8;
            Random random = new Random();
            String generatedString;
            // 16 바이트 키 (AES-128)

            while (true) {
                generatedString = random.ints(leftLimit, rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                if (expenditureService.SelectExpenditureId(generatedString) == false) {
                    break;
                }
            }

            ExpenditureEntity expenditure = ExpenditureEntity.builder()
                    .travelCode(travelCode)
                    .expenditureId(generatedString)
                    .purpose(expendituresDTO.getPurpose())
                    .method(expendituresDTO.getMethod())
                    .isPublic(expendituresDTO.isPublic())
                    .payer(expendituresDTO.getPayer())
                    .date(expendituresDTO.getDate())
                    .KRW(expendituresDTO.getKRW())
                    .amount(expendituresDTO.getAmount())
                    .currency(expendituresDTO.getCurrency())
                    .description(expendituresDTO.getDescription())
                    .build();

            ExpenditureEntity createExpenditure = expenditureService.create(expenditure);
            ExpendituresDTO responsedDTO = ExpendituresDTO.builder()
                    .travelCode(createExpenditure.getTravelCode())
                    .expenditureId(createExpenditure.getExpenditureId())
                    .purpose(createExpenditure.getPurpose())
                    .method(createExpenditure.getMethod())
                    .isPublic(createExpenditure.isPublic())
                    .payer(createExpenditure.getPayer())
                    .date(createExpenditure.getDate())
                    .KRW(createExpenditure.getKRW())
                    .amount(createExpenditure.getAmount())
                    .currency(createExpenditure.getCurrency())
                    .description(createExpenditure.getDescription())
                    .build();

            return ResponseEntity.ok().body(responsedDTO);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 지출 목록
    @GetMapping("/{travelCode}")
    public Mono<ResponseEntity<Flux<ExpenditureEntity>>> findAllExpenditures(@AuthenticationPrincipal String userId, @PathVariable String travelCode) {
        Flux<ExpenditureEntity> expenditure = expenditureService.findAllByTravelCode(travelCode);

        // ResponseEntity에 Flux를 래핑해서 반환
        return Mono.just(ResponseEntity.ok().body(expenditure));
    }


    // 지출 수정
    @PutMapping("/{travelCode}/{expenditureId}")
    public ResponseEntity<?> updateExpenditure(@AuthenticationPrincipal String userId, @RequestBody ExpendituresDTO expendituresDTO, @PathVariable String travelCode, @PathVariable String expenditureId) {
        try {
            ExpenditureEntity expenditure = ExpenditureEntity.builder()
                    .travelCode(travelCode)
                    .expenditureId(expenditureId)
                    .purpose(expendituresDTO.getPurpose())
                    .method(expendituresDTO.getMethod())
                    .isPublic(expendituresDTO.isPublic())
                    .payer(expendituresDTO.getPayer())
                    .date(expendituresDTO.getDate())
                    .KRW(expendituresDTO.getKRW())
                    .amount(expendituresDTO.getAmount())
                    .currency(expendituresDTO.getCurrency())
                    .description(expendituresDTO.getDescription())
                    .build();

            ExpenditureEntity updateExpenditure = expenditureService.updateExpenditure(expenditureId, expenditure);

            ExpendituresDTO responsedDTO = ExpendituresDTO.builder()
                    .id(updateExpenditure.getId())
                    .travelCode(updateExpenditure.getTravelCode())
                    .expenditureId(updateExpenditure.getExpenditureId())
                    .purpose(updateExpenditure.getPurpose())
                    .method(updateExpenditure.getMethod())
                    .isPublic(updateExpenditure.isPublic())
                    .payer(updateExpenditure.getPayer())
                    .date(updateExpenditure.getDate())
                    .KRW(updateExpenditure.getKRW())
                    .amount(updateExpenditure.getAmount())
                    .currency(updateExpenditure.getCurrency())
                    .description(updateExpenditure.getDescription())
                    .build();

            return ResponseEntity.ok().body(responsedDTO);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // 지출 삭제


//마지막 과제 : 카드 별, 현금 별로 데이터 뽑아오는 함수 추가
}