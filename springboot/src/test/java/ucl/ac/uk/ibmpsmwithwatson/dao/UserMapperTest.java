package ucl.ac.uk.ibmpsmwithwatson.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void getUserByEmail() {
        System.out.println(userMapper.getUserByEmail("melina.sela@gmail.com"));
    }
}
