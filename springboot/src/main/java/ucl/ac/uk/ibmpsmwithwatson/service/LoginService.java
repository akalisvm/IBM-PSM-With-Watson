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
import ucl.ac.uk.ibmpsmwithwatson.dao.TableMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
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
    UserMapper userMapper;

    public User checkNHSLogin(String code) {
        String accessToken = nhsLoginClient.getAccessToken(code);
        User nhsUser = nhsLoginClient.getUserInfo(accessToken);
        nhsUser.setRole("patient");
        if(getUserByEmail(nhsUser.getEmail()) == null) {
            GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());
            String id = String.valueOf(userMapper.getNHSUserId());
            nhsUser.setId(id);
            graphMapper.addNode("User", "user_" + id,
                    JSONUtil.parseObj(nhsUser, false).toString());
        }
        nhsUser.setApp_token(JwtUtil.getToken(MapUtil.toUserMap(nhsUser)));
        return nhsUser;
    }

    public User checkAppLogin(User requestUser) {
        User appUser = getUserByEmail(requestUser.getEmail());
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        if(appUser == null || !appUser.getPassword().equals(md5.digestHex(requestUser.getPassword()))) {
            return null;
        }
        appUser.setApp_token(JwtUtil.getToken(MapUtil.toUserMap(appUser)));
        return appUser;
    }

    private User getUserByEmail(String email) {
        JSONArray jsonArray = userMapper.getUserByEmail(email);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        return list.size() == 0 ? null : list.get(0);
    }
}
