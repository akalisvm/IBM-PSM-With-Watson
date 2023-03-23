package ucl.ac.uk.ibmpsmwithwatson.util;

import lombok.Data;

@Data
public class Message {
    private String sessionId;
    private String author;
    private String text;

    public Message(String sessionId, String author, String text) {
        this.sessionId = sessionId;
        this.author = author;
        this.text = text;
    }
}
