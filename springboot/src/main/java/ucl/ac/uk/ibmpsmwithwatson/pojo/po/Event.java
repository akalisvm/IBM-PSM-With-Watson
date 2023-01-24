package ucl.ac.uk.ibmpsmwithwatson.pojo.po;

import lombok.Data;

import java.util.Date;

@Data
public class Event {
    private String id;
    private Date createTime;
    private String organiserId;
    private String participantId;
    private String title;
    private String description;
    private String platform;
    private Date meetingTime;
    private String repeat;
    private String result;
    private String feedback;
}
