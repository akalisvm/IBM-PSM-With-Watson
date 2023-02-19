package ucl.ac.uk.ibmpsmwithwatson.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.service.LoginService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Api(tags = "Login")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Value("${server.ip}")
    private String IP;

    @Autowired
    LoginService loginService;

    @GetMapping("/nhs")
    public Result<?> checkNHSLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        User nhsUser = loginService.checkNHSLogin(code);
        setUserInfoCookie(nhsUser, response);
        response.sendRedirect("http://" + IP + ":8080/home");
        return Result.success(nhsUser);
    }

    @PostMapping("/app")
    public Result<?> checkAppLogin(@RequestBody User requestUser, HttpServletResponse response) throws JsonProcessingException, UnsupportedEncodingException {
        User appUser = loginService.checkAppLogin(requestUser);
        if(appUser == null) {
            return Result.error("10001", "Incorrect email or password");
        }
        setUserInfoCookie(appUser, response);
        return Result.success(appUser);
    }

    private void setUserInfoCookie(User user, HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String userInfo = URLEncoder.encode(om.writeValueAsString(user), "UTF-8");
        Cookie cookie = new Cookie("user", userInfo);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24);
        response.addCookie(cookie);
    }
}
