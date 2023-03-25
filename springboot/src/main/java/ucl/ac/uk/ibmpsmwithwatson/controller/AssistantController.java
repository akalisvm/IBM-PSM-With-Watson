package ucl.ac.uk.ibmpsmwithwatson.controller;

import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.assistant.v2.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.util.Message;
import ucl.ac.uk.ibmpsmwithwatson.service.AssistantService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/assistant")
public class AssistantController {

    @Autowired
    AssistantService assistantService;

    @PostMapping("/message")
    public Result<?> getResponse(@RequestBody Message message, HttpServletResponse response) {
        Assistant assistant = assistantService.authenticate();
        List<String> assistantResponse = new ArrayList<>();
        try {
            assistantResponse = assistantService.getTextResponse(
                    assistant, message.getSessionId(), message.getAuthor(), message.getText());
        } catch (NotFoundException e) {
            String newSessionId = assistantService.createSession(assistant);
            assistantResponse = assistantService.getTextResponse(
                    assistant, newSessionId, message.getAuthor(), message.getText());
            Cookie sessionIdCookie = new Cookie("sessionId", newSessionId);
            sessionIdCookie.setPath("/");
            sessionIdCookie.setMaxAge(3600 * 24);
            response.addCookie(sessionIdCookie);
        } catch (RuntimeException re) {
            assistantResponse.add(re.getMessage());
        }
        return Result.success(assistantResponse);
    }

    @GetMapping("/delete/{sessionId}")
    public void deleteSessionId(@PathVariable String sessionId) {
        Assistant assistant = assistantService.authenticate();
        assistantService.deleteSession(assistant, sessionId);
    }
}