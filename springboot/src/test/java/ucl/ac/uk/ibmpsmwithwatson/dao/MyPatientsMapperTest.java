package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

@SpringBootTest
public class MyPatientsMapperTest {

    @Autowired
    MyPatientsMapper myPatientsMapper;

    @Test
    void getPatients() {
        JSONArray array = (JSONArray) myPatientsMapper.queryPatient("1").get("rows");
        System.out.println(JSONUtil.toList(array, User.class));
    }
}
