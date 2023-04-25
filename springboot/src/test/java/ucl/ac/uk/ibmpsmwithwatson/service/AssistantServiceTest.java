package ucl.ac.uk.ibmpsmwithwatson.service;

import com.ibm.watson.assistant.v2.Assistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssistantServiceTest {

    @Autowired
    AssistantService assistantService;

    @Test
    public void getResponse() {
        Assistant assistant = assistantService.authenticate();
        String sessionId = assistantService.createSession(assistant);
        System.out.println(assistantService.getResponse(assistant, sessionId, "melina.sela.test@gmail.com", "how are you"));
        assistantService.deleteSession(assistant, sessionId);
    }
}
