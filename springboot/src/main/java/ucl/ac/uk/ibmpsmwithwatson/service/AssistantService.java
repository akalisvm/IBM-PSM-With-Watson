package ucl.ac.uk.ibmpsmwithwatson.service;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import org.springframework.stereotype.Service;

@Service
public class AssistantService {

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

    public String getResponse(Assistant assistant, String sessionId, String text) {
        MessageInput input = new MessageInput.Builder().messageType("text").text(text).build();
        MessageOptions options = new MessageOptions.Builder(EnvironmentID, sessionId).input(input).build();
        return assistant.message(options).execute().getResult().getOutput().getGeneric().get(0).text();
    }

    public void deleteSession(Assistant assistant, String sessionID) {
        DeleteSessionOptions options = new DeleteSessionOptions.Builder(EnvironmentID, sessionID).build();
        assistant.deleteSession(options).execute();
    }
}