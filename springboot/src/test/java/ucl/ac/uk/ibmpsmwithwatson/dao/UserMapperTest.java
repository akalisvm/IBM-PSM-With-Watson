package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void getPatientsByDoctorId() {
        JSONArray array = userMapper.getPatientsByDoctorId("1");
        System.out.println(JSONUtil.toList(array, User.class));
    }

    @Test
    void getNumberOfPatientsByDoctorId() {
        JSONObject object = userMapper.getNumberOfPatientsByDoctorId("1");
        System.out.println(object.get("count"));
    }
}
