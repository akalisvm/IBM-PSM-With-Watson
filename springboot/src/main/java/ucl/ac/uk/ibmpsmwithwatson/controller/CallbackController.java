package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucl.ac.uk.ibmpsmwithwatson.common.NHSLoginClient;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

@RestController
public class CallbackController {

    private final NHSLoginClient nhsLoginClient;
    CallbackController(NHSLoginClient nhsLoginClient) {
        this.nhsLoginClient = nhsLoginClient;
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        String accessToken = nhsLoginClient.getAccessToken(code);
        User user = nhsLoginClient.getUserInfo(accessToken);
        return user.toString();
    }
}
