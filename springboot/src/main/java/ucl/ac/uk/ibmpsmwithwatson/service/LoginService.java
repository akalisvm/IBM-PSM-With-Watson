package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;
import ucl.ac.uk.ibmpsmwithwatson.dao.GraphMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.LoginMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.TableMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.JwtUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.MapUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.NHSLoginClient;

import java.util.List;

@Service
public class LoginService {

    private final NHSLoginClient nhsLoginClient;
    LoginService(NHSLoginClient nhsLoginClient) {
        this.nhsLoginClient = nhsLoginClient;
    }

    @Autowired
    LoginMapper loginMapper;

    public User checkNhsLogin(String code) {
        String accessToken = nhsLoginClient.getAccessToken(code);
        User nhsUser = nhsLoginClient.getUserInfo(accessToken);
        nhsUser.setRole("patient");
        if(queryUserByEmail(nhsUser.getEmail()) == null) {
            TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
            GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());
            graphMapper.addNode("User", "user_" + (tableMapper.getCount("User") + 1),
                    JSONUtil.parseObj(nhsUser, false).toString());
        }
        nhsUser.setApp_token(JwtUtil.getToken(MapUtil.toUserMap(nhsUser)));
        return nhsUser;
    }

    public User checkAppLogin(User verifyUser) {
        User appUser = queryUserByEmail(verifyUser.getEmail());
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        if(appUser == null || !appUser.getPassword().equals(md5.digestHex(verifyUser.getPassword()))) {
            return null;
        }
        appUser.setApp_token(JwtUtil.getToken(MapUtil.toUserMap(appUser)));
        return appUser;
    }

    private User queryUserByEmail(String email) {
        JSONArray array = (JSONArray) loginMapper.queryUserByEmail(email).get("rows");
        List<User> userList = JSONUtil.toList(array, User.class);
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }
}
