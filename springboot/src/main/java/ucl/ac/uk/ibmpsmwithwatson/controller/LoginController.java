package ucl.ac.uk.ibmpsmwithwatson.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.util.JwtUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.NHSLoginClient;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
    public Result<?> NHSLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        String accessToken = nhsLoginClient.getAccessToken(code);
        User nhsUser = nhsLoginClient.getUserInfo(accessToken);
        nhsUser.setRole("patient");
        nhsUser.setApp_token(JwtUtil.getToken(nhsUser.toMap(nhsUser)));
        setUserInfoCookie(nhsUser, response);
        response.sendRedirect("http://" + IP + ":8080/personal");
        return Result.success(nhsUser);
    }

    @PostMapping("/app")
    public Result<?> AppLogin(@RequestBody User user, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        if((!user.getEmail().equals("patient@test.com") && !user.getEmail().equals("doctor@test.com") )
                || !user.getPassword().equals("123")) {
            return Result.error("10001", "Incorrect email or password");
        }
        Map<String, String> map = new HashMap<>();
        map.put("email", user.getEmail());
        User appUser = new User();
        appUser.setEmail(user.getEmail());
        if(user.getEmail().startsWith("patient")) {
            appUser.setRole("patient");
        } else {
            appUser.setRole("doctor");
        }
        appUser.setApp_token(JwtUtil.getToken(map));
        setUserInfoCookie(appUser, response);
        return Result.success(appUser);
    }

    private void setUserInfoCookie(@RequestBody User user, HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String userInfo = URLEncoder.encode(om.writeValueAsString(user), "UTF-8");
        Cookie cookie = new Cookie("user", userInfo);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24);
        response.addCookie(cookie);
    }
}
