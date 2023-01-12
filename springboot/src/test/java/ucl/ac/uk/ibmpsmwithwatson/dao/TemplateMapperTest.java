package ucl.ac.uk.ibmpsmwithwatson.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TemplateMapperTest {

    @Autowired
    TemplateMapper templateMapper;

    @Test
    void queryCountNoData() {
        System.out.println(templateMapper.queryCount() == null);
    }

    @Test
    void insertCount() {
        templateMapper.insertCount();
    }

    @Test
    void queryCountWithData() {
        System.out.println(templateMapper.queryCount().equals("1"));
    }

    @Test
    void updateCount() {
        templateMapper.updateCount("1");
    }

    @Test
    void deleteTemplate() {
        templateMapper.deleteTemplate("3");
    }
}
