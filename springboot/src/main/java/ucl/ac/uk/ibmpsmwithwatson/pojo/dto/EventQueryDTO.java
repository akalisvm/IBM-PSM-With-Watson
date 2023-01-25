package ucl.ac.uk.ibmpsmwithwatson.pojo.dto;

import lombok.Data;

@Data
public class EventQueryDTO {
    private String userId;
    private String userRole;
    private String searchInput;
    private String patientFilter;
    private String platformFilter;
    private String resultFilter;
    private Integer pageNum;
    private Integer pageSize;
}
