package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class QuestionnaireMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    private static final String QUESTIONNAIRE = "Questionnaire";

    public void insertCount() {
        tableMapper.insertCount(QUESTIONNAIRE);
    }

    public String queryCount() {
        return tableMapper.queryCount(QUESTIONNAIRE);
    }

    public void updateCount(String count) {
        tableMapper.updateCount(QUESTIONNAIRE, count);
    }

    public void insert(String creatorId, String relProp, String questionnaireId, String questionnaireProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + creatorId +
                ")-[CREATED " + relProp + "]->(Questionnaire:questionnaire_" + questionnaireId + " " + questionnaireProp + ")");
    }

    public JSONObject query(String creatorId) {
        return graphMapper.runCypherQuery("S=>(Questionnaire:* {creatorId=\"" + creatorId + "\"}); RETURN * SORT_DESC createTime");
    }

    public void update(String questionnaireId, String questionnaireProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + questionnaireProp + " where name=\"questionnaire_" + questionnaireId + "\"");
    }

    public void delete(String questionnaireId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"questionnaire_" + questionnaireId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"questionnaire_" + questionnaireId + "\"");
    }
}
