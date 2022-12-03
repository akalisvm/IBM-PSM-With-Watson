package ucl.ac.uk.ibmpsmwithwatson.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void getUserByEmail() {
        System.out.println(userRepository.getUserByEmail("melina.sela@gmail.com"));
    }

}
