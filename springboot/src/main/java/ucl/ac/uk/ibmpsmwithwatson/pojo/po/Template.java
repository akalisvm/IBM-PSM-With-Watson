package ucl.ac.uk.ibmpsmwithwatson.pojo.po;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Template {
    private String id;
    private Date createTime;
    private String creatorId;
    private String title;
    private String description;
    private List<Question> questions;
}
