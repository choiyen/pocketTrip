package project.backend.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionVisitCount {
    private String location;
    private long visitCount;
}
