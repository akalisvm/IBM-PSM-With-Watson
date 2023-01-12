package ucl.ac.uk.ibmpsmwithwatson.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Template {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+0")
    private Date createTime;
    private String creatorId;
    private String title;
    private List<Question> questions;
}
