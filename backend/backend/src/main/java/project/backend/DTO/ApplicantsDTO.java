package project.backend.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class ApplicantsDTO {
    private String id;
    private String travelcode;
    private List<String> userList;
}
