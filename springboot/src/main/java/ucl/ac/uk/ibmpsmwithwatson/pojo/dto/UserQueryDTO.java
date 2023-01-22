package ucl.ac.uk.ibmpsmwithwatson.pojo.dto;

import lombok.Data;

@Data
public class UserQueryDTO {
    private String doctorId;
    private String searchInput;
    private String resultFilter;
    private String needMeetingFilter;
    private Integer pageNum;
    private Integer pageSize;
}
