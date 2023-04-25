package ucl.ac.uk.ibmpsmwithwatson.service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Dialog;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Event;
import ucl.ac.uk.ibmpsmwithwatson.util.Message;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.util.MessageLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AssistantService {

    @Autowired
    UserService userService;

    @Autowired
    DialogService dialogService;

    @Autowired
    EventService eventService;

    static final String APIkey = "<API key>";
    static final String URL = "<URL>";
    static final String EnvironmentID = "<Environment ID>";

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

    public List<String> getResponse(Assistant assistant, String sessionId, String author, String text) {
        User operator = userService.getUserByEmail(author);
        MessageInput input = new MessageInput.Builder().messageType("text").text(text).build();
        MessageOptions options = new MessageOptions.Builder(EnvironmentID, sessionId).input(input).build();
        MessageResponse messageResponse = assistant.message(options).execute().getResult();
        MessageLog.getInstance().addMessage(new Message(
                "", operator.getGiven_name() + " " + operator.getFamily_name(), text));
        List<String> texts = new ArrayList<>();
        List<RuntimeResponseGeneric> generic = messageResponse.getOutput().getGeneric();
        for(RuntimeResponseGeneric runtimeResponseGeneric : generic) {
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
            if(runtimeResponseGeneric.text() != null) {
                texts.add(runtimeResponseGeneric.text());
                MessageLog.getInstance().addMessage(new Message("", "Watson Assistant", runtimeResponseGeneric.text()));
                if (runtimeResponseGeneric.text().endsWith("Enjoy the rest of your day!")) {
                    List<String> userMessages = new ArrayList<>();
                    for(Message message : MessageLog.getInstance().getMessages()) {
                        if(!message.getAuthor().equals("Watson Assistant")) {
                            userMessages.add(message.getText());
                        }
                    }
                    switch (MessageLog.getInstance().getAction()) {
                        case "sdm":
                            Dialog dialog = dialogService.getDialog(operator.getId());
                            if (dialog == null) {
                                dialogService.insertDialog(operator.getId(), MessageLog.getInstance().getMessages());
                            } else {
                                dialog.setCreateTime(new Date());
                                dialog.setMessages(MessageLog.getInstance().getMessages());
                                dialogService.updateDialog(dialog);
                            }
                            break;
                        case "schedule": {
                            String id = userMessages.get(0);
                            try {
                                Integer.parseInt(id);
                            } catch (Exception e) {
                                throw new RuntimeException("Invalid patient ID.");
                            }

                            User participant = userService.getUserById(id);
                            if (participant == null) {
                                throw new RuntimeException("The patient ID does not exist.");
                            }

                            List<String> platforms = Arrays.asList("Microsoft Teams", "Webex", "WhatsApp", "Phone Call");
                            if (!platforms.contains(userMessages.get(3))) {
                                throw new RuntimeException("Unrecognised platform.");
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date;
                            try {
                                date = sdf.parse(userMessages.get(4));
                            } catch (ParseException e) {
                                throw new RuntimeException("Invalid date format.");
                            }

                            String repeat;
                            if (userMessages.size() == 7) {
                                try {
                                    repeat = userMessages.get(6);
                                    if (Integer.parseInt(repeat) < 1) {
                                        throw new RuntimeException("Too small repeat number of weeks.");
                                    }
                                    if (Integer.parseInt(repeat) > 12) {
                                        throw new RuntimeException("Too large repeat number of weeks.");
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException("Invalid repeat number of weeks.");
                                }
                            }

                            Event event = new Event();
                            event.setOrganiserId(MessageLog.getInstance().getOperatorId());
                            event.setOrganiserName(MessageLog.getInstance().getOperatorName());
                            event.setParticipantId(id);
                            event.setParticipantName(participant.getGiven_name() + " " + participant.getFamily_name());
                            event.setTitle(userMessages.get(1));
                            event.setDescription(userMessages.get(2));
                            event.setPlatform(userMessages.get(3));
                            event.setMeetingTime(date);
                            if (userMessages.size() == 7) {
                                repeat = userMessages.get(6);
                                if (repeat.equals("1")) {
                                    event.setRepeat("Every 1 week");
                                } else {
                                    int intRepeat = Integer.parseInt(repeat);
                                    if (intRepeat % 4 == 0) {
                                        if (intRepeat / 4 == 1) {
                                            event.setRepeat("Every 1 month");
                                        } else {
                                            event.setRepeat("Every " + intRepeat / 4 + " months");
                                        }
                                    } else {
                                        event.setRepeat("Every " + repeat + " weeks");
                                    }
                                }
                            } else {
                                event.setRepeat("Does not repeat");
                            }
                            eventService.insertEvent(event);
                            break;
                        }
                        case "reschedule": {
                            String id = userMessages.get(0);
                            try {
                                Integer.parseInt(id);
                            } catch (Exception e) {
                                throw new RuntimeException("Invalid patient ID.");
                            }
                            User participant = userService.getUserById(id);
                            if (participant == null) {
                                throw new RuntimeException("The patient ID does not exist.");
                            }

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date;
                            try {
                                date = sdf.parse(userMessages.get(1));
                            } catch (ParseException e) {
                                throw new RuntimeException("Invalid date format.");
                            }

                            String repeat;
                            if (userMessages.size() == 4) {
                                try {
                                    repeat = userMessages.get(3);
                                    if (Integer.parseInt(repeat) < 1) {
                                        throw new RuntimeException("Too small repeat number of weeks.");
                                    }
                                    if (Integer.parseInt(repeat) > 12) {
                                        throw new RuntimeException("Too large repeat number of weeks.");
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException("Invalid repeat number of weeks.");
                                }
                            }

                            Event event = eventService.getPendingEventById(id);
                            if (event != null) {
                                event.setMeetingTime(date);
                                if (userMessages.size() == 4) {
                                    repeat = userMessages.get(3);
                                    if (repeat.equals("1")) {
                                        event.setRepeat("Every 1 week");
                                    } else {
                                        int intRepeat = Integer.parseInt(repeat);
                                        if (intRepeat % 4 == 0) {
                                            if (intRepeat / 4 == 1) {
                                                event.setRepeat("Every 1 month");
                                            } else {
                                                event.setRepeat("Every " + intRepeat / 4 + " months");
                                            }
                                        } else {
                                            event.setRepeat("Every " + repeat + " weeks");
                                        }
                                    }
                                } else {
                                    event.setRepeat("Does not repeat");
                                }
                                eventService.updateEvent(event);
                            } else {
                                throw new RuntimeException("The outreach event does not exist.");
                            }
                            break;
                        }
                    }
                }
            }
        }
        return texts;
    }

    public void deleteSession(Assistant assistant, String sessionID) {
        try {
            DeleteSessionOptions options = new DeleteSessionOptions.Builder(EnvironmentID, sessionID).build();
            assistant.deleteSession(options).execute();
        } catch (NotFoundException e) {
            // System.out.println(e.getMessage());
        }
    }
}