package ucl.ac.uk.ibmpsmwithwatson.pojo.po;

import lombok.Data;

import java.util.Date;

@Data
public class Record {
    private String id;
    private Date createTime;
    private String creatorId;
    private Questionnaire questionnaire;
}
