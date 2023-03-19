package ucl.ac.uk.ibmpsmwithwatson.pojo.po;

import lombok.Data;

@Data
public class Message {
    private String sessionId;
    private String author;
    private String text;
}
