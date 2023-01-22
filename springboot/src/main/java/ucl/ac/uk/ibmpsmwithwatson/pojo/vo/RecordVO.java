package ucl.ac.uk.ibmpsmwithwatson.pojo.vo;

import lombok.Data;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Questionnaire;

import java.util.Date;

@Data
public class RecordVO {
    private String id;
    private Date createTime;
    private String creatorId;
    private String creatorName;
    private Questionnaire questionnaire;
}
