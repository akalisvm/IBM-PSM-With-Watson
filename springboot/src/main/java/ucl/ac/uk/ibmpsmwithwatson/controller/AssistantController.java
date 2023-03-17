package ucl.ac.uk.ibmpsmwithwatson.controller;

import com.ibm.watson.assistant.v2.Assistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Message;
import ucl.ac.uk.ibmpsmwithwatson.service.AssistantService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import java.util.List;

@RestController
@RequestMapping("/assistant")
public class AssistantController {

    @Autowired
    AssistantService assistantService;

    @PostMapping("/message")
    public Result<?> getResponse(@RequestBody Message message) {
        Assistant assistant = assistantService.authenticate();
        return Result.success(assistantService.getResponse(assistant, message.getSessionId(), message.getText()));
    }

    @GetMapping("/delete/{sessionId}")
    public void deleteSessionId(@PathVariable String sessionId) {
        Assistant assistant = assistantService.authenticate();
        assistantService.deleteSession(assistant, sessionId);
    }
}