package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import java.util.List;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void getUserByEmail() {
        JSONArray array = (JSONArray) userMapper.getUserByEmail("seraphina.angelia@gmail.com").get("rows");
        List<User> userList = JSONUtil.toList(array, User.class);
        if(userList.size() != 0) {
            System.out.println(userList.get(0));
        } else {
            System.out.println("No such user");
        }
    }

    @Test
    void getPatients() {
        JSONArray array = (JSONArray) userMapper.getPatients("Melina,Sela").get("rows");
        System.out.println(JSONUtil.toList(array, User.class));
    }
}
