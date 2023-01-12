package ucl.ac.uk.ibmpsmwithwatson.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Template {
    private String id;
    private Date createTime;
    private String creatorId;
    private String title;
    private List<Question> questions;
}
