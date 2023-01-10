package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;
import ucl.ac.uk.ibmpsmwithwatson.dao.GraphMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.TableMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.JwtUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.MapUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.NHSLoginClient;

@Service
public class LoginService {

    private final NHSLoginClient nhsLoginClient;
    LoginService(NHSLoginClient nhsLoginClient) {
        this.nhsLoginClient = nhsLoginClient;
    }

    @Autowired
    UserService userService;

    public User checkNhsLogin(String code) {
        String accessToken = nhsLoginClient.getAccessToken(code);
        User nhsUser = nhsLoginClient.getUserInfo(accessToken);
        nhsUser.setRole("patient");
        if(userService.getUserByEmail(nhsUser.getEmail()) == null) {
            TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
            GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());
            graphMapper.addNode("User", "user_" + (tableMapper.getCount("User") + 1),
                    JSONUtil.parseObj(nhsUser, false).toString());
        }
        nhsUser.setApp_token(JwtUtil.getToken(MapUtil.toUserMap(nhsUser)));
        return nhsUser;
    }

    public User checkAppLogin(User verifyUser) {
        User appUser = userService.getUserByEmail(verifyUser.getEmail());
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        if(appUser == null || !appUser.getPassword().equals(md5.digestHex(verifyUser.getPassword()))) {
            return null;
        }
        appUser.setApp_token(JwtUtil.getToken(MapUtil.toUserMap(appUser)));
        return appUser;
    }
}
