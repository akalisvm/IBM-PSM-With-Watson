package ucl.ac.uk.ibmpsmwithwatson.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class EventVO {
    private String id;
    private Date createTime;
    private String organiserId;
    private String organiserName;
    private String participantId;
    private String participantName;
    private String title;
    private String description;
    private String platform;
    private Date meetingTime;
    private Date lastMeetingTime;
    private Date lastSuccessfulMeetingTime;
    private Date nextMeetingTime;
    private String repeat;
    private String result;
    private String feedback;
}
