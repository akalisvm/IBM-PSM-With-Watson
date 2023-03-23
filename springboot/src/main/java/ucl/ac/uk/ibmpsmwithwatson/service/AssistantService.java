package ucl.ac.uk.ibmpsmwithwatson.service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Dialog;
import ucl.ac.uk.ibmpsmwithwatson.util.Message;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.util.MessageLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AssistantService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    DialogService dialogService;

    final String APIkey = "hFGcCwRg-hv5smSLqq_wJA_JO1fhDd7S8HHrt1-5zpfI";
    final String URL = "https://api.eu-gb.assistant.watson.cloud.ibm.com/instances/3074a022-733c-4ccf-8389-a74bdcdb89a3";
    final String EnvironmentID = "d9aac63d-6247-444e-901c-27a5b77e01a7";

    public Assistant authenticate() {
        IamAuthenticator authenticator = new IamAuthenticator.Builder().apikey(APIkey).build();
        Assistant assistant = new Assistant("2021-06-14", authenticator);
        assistant.setServiceUrl(URL);
        return assistant;
    }

    public String createSession(Assistant assistant) {
        CreateSessionOptions options = new CreateSessionOptions.Builder(EnvironmentID).build();
        return assistant.createSession(options).execute().getResult().getSessionId();
    }

    public List<String> getTextResponse(Assistant assistant, String sessionId, String author, String text) {
        User operator = dialogService.getUserByEmail(author);
        MessageInput input = new MessageInput.Builder().messageType("text").text(text).build();
        MessageOptions options = new MessageOptions.Builder(EnvironmentID, sessionId).input(input).build();
        MessageResponse messageResponse = assistant.message(options).execute().getResult();
        MessageLog.getInstance().addMessage(new Message(
                "", operator.getGiven_name() + " " + operator.getFamily_name(), text));
        List<String> texts = new ArrayList<>();
        List<RuntimeResponseGeneric> generic = messageResponse.getOutput().getGeneric();
        for(int i = 0; i < generic.size(); i++) {
            if(generic.get(0).text().equals("Let's start the shared decision making dialog.")) {
                MessageLog.getInstance().reset();
                MessageLog.getInstance().setAction("sdm");
                MessageLog.getInstance().setOperatorId(operator.getId());
                MessageLog.getInstance().setOperatorName(operator.getGiven_name() + " " + operator.getFamily_name());
            }
            if(generic.get(0).text().equals("Let's schedule your appointment.")) {
                MessageLog.getInstance().reset();
                MessageLog.getInstance().setAction("schedule");
                MessageLog.getInstance().setOperatorId(operator.getId());
                MessageLog.getInstance().setOperatorName(operator.getGiven_name() + " " + operator.getFamily_name());
            }
            if(generic.get(0).text().equals("Let's reschedule your appointment.")) {
                MessageLog.getInstance().reset();
                MessageLog.getInstance().setAction("reschedule");
                MessageLog.getInstance().setOperatorId(operator.getId());
                MessageLog.getInstance().setOperatorName(operator.getGiven_name() + " " + operator.getFamily_name());
            }
            if(generic.get(i).text() != null) {
                texts.add(generic.get(i).text());
                MessageLog.getInstance().addMessage(new Message("", "Watson Assistant", generic.get(i).text()));
                if(generic.get(i).text().endsWith("Enjoy the rest of your day!")) {
                    if(MessageLog.getInstance().getAction().equals("sdm")) {
                        Dialog dialog = dialogService.getDialog(operator.getId());
                        if(dialog == null) {
                            dialogService.insert(operator.getId(), MessageLog.getInstance().getMessages());
                        } else {
                            dialog.setCreateTime(new Date());
                            dialog.setMessages(MessageLog.getInstance().getMessages());
                            dialogService.update(dialog);
                        }
                        System.out.println(MessageLog.getInstance());
                    }
                }
            }
        }
        return texts;
    }

    public void deleteSession(Assistant assistant, String sessionID) {
        DeleteSessionOptions options = new DeleteSessionOptions.Builder(EnvironmentID, sessionID).build();
        assistant.deleteSession(options).execute();
    }
}