package ucl.ac.uk.ibmpsmwithwatson.pojo.dto;

import lombok.Data;

@Data
public class RecordQueryDTO {
    private String userId;
    private String userRole;
    private String searchInput;
    private String patientFilter;
    private String resultFilter;
    private String needMeetingFilter;
    private Integer pageNum;
    private Integer pageSize;
}
