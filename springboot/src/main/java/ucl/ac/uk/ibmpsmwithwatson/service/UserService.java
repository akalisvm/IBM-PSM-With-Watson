package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserByEmail(String email) {
        JSONArray array = (JSONArray) userMapper.getUserByEmail(email).get("rows");
        List<User> userList = JSONUtil.toList(array, User.class);
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }
}
