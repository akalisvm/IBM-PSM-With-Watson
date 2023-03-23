package ucl.ac.uk.ibmpsmwithwatson.pojo.po;

import lombok.Data;
import ucl.ac.uk.ibmpsmwithwatson.util.Message;

import java.util.Date;
import java.util.List;

@Data
public class Dialog {
    private String id;
    private Date createTime;
    private String creatorId;
    private List<Message> messages;
}
