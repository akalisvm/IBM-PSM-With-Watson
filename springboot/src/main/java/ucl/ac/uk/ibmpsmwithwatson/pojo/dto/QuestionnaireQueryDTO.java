package ucl.ac.uk.ibmpsmwithwatson.pojo.dto;

import lombok.Data;

@Data
public class QuestionnaireQueryDTO {
    private String doctorId;
    private String searchInput;
    private Integer pageNum;
    private Integer pageSize;
}
