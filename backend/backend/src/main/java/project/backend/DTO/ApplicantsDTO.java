package project.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApplicantsDTO {
    private String travelcode;
    private List<String> userList;
}