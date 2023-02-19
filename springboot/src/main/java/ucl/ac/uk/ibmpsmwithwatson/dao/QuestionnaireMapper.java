package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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

    public String getCount() {
        return tableMapper.getCount(QUESTIONNAIRE);
    }

    public void updateCount(String count) {
        tableMapper.updateCount(QUESTIONNAIRE, count);
    }

    public void insert(String doctorId, String relProp, String questionnaireId, String questionnaireProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + doctorId +
                ")-[CREATED " + relProp + "]->(Questionnaire:questionnaire_" +
                questionnaireId + " " + questionnaireProp + ")");
    }

    public JSONObject getNumberOfQuestionnairesByDoctorId(String doctorId) {
        return graphMapper.runCypherQuery("S=>(Questionnaire:* {creatorId=\"" + doctorId + "\"}); RETURN COUNT(*) AS count");
    }

    public JSONArray getQuestionnaires(String doctorId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Questionnaire:* {creatorId=\"" + doctorId + "\"}); RETURN * SORT_ASC createTime").get("rows");
    }

    public JSONArray getQuestionnaireById(String questionnaireId) {
        return (JSONArray) graphMapper.runCypherQuery("S=>(Questionnaire:* {id=\"" + questionnaireId + "\"})").get("rows");
    }

    public JSONArray getTitle(String questionnaireId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(@q Questionnaire:* {id=\"" + questionnaireId + "\"}); RETURN q.title").get("rows");
    }

    public void update(String questionnaireId, String questionnaireProp) {
        tableMapper.runSQLQuery("update mygraph set val = " +
                questionnaireProp + " where name=\"questionnaire_" + questionnaireId + "\"");
    }

    public JSONArray check(String questionnaireId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(User:* {questionnaire=\"" + questionnaireId + "\"})").get("rows");
    }

    public void delete(String questionnaireId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"questionnaire_" + questionnaireId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"questionnaire_" + questionnaireId + "\"");
    }

    public void assign(String questionnaireProp, String questionnaireId) {
        tableMapper.runSQLQuery("update mygraph set val = " + questionnaireProp + " where name=\"user_" + questionnaireId + "\"");
    }
}
