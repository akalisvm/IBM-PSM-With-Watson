package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QuestionnaireMapperTest {

    @Autowired
    QuestionnaireMapper questionnaireMapper;

    private static final String TEMPLATE = "Template";
    private static final String QUESTIONNAIRE = "Questionnaire";

    @Test
    void queryCountNoData() {
        System.out.println(questionnaireMapper.queryCount(TEMPLATE) == null);
    }

    @Test
    void insertCount() {
        System.out.println(questionnaireMapper.insertCount(TEMPLATE));
    }

    @Test
    void queryCountWithData() {
        System.out.println(questionnaireMapper.queryCount(TEMPLATE).equals("1"));
    }

    @Test
    void updateCount() {
        System.out.println(questionnaireMapper.updateCount(TEMPLATE, "2"));
    }

    @Test
    void insertTemplate() {
        String json = "{\"id\":\"1\",\"creatorId\":\"1\",\"createTime\":\"2023-01-11 22:41:02\",\"name\":\"test\",\"questions\":[{\"value\":\"question 1\"}]}";
        System.out.println(questionnaireMapper.insertTemplate("1", "1", json));
    }
}
