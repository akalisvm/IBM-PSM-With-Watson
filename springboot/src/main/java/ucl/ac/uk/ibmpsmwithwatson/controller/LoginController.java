package ucl.ac.uk.ibmpsmwithwatson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucl.ac.uk.ibmpsmwithwatson.common.NHSLoginClient;
import ucl.ac.uk.ibmpsmwithwatson.common.Result;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Value("${server.ip}")
    private String IP;

    private final NHSLoginClient nhsLoginClient;
    LoginController(NHSLoginClient nhsLoginClient) {
        this.nhsLoginClient = nhsLoginClient;
    }

    @GetMapping("/nhs")
    public Result<?> callback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = nhsLoginClient.getAccessToken(code);
        User user = nhsLoginClient.getUserInfo(accessToken);
        ObjectMapper om = new ObjectMapper();
        String userInfo = URLEncoder.encode(om.writeValueAsString(user), "UTF-8");
        Cookie cookie = new Cookie("user", userInfo);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24);
        response.addCookie(cookie);
        response.sendRedirect("http://" + IP + ":8080/patients");
        return Result.success(user);
    }
}
