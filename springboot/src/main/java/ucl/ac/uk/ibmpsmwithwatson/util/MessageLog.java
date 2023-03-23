package ucl.ac.uk.ibmpsmwithwatson.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MessageLog {

    private MessageLog() {}

    private static class MessageLogInner {
        private static final MessageLog instance = new MessageLog();
    }

    public static MessageLog getInstance() {
        return MessageLogInner.instance;
    }

    private String action = "";
    private String operatorId = "";
    private String operatorName = "";
    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void reset() {
        action = "";
        operatorId = "";
        operatorName = "";
        messages.clear();
    }
}