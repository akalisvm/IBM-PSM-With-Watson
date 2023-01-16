package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class MyPatientsMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public JSONObject queryPatientById(String id) {
        return graphMapper.runCypherQuery("S=>(User:* {id=\"" + id + "\"})");
    }

    public JSONObject queryPatient(String doctor) {
        return graphMapper.runCypherQuery("S=>(User:* {doctor=\"" + doctor + "\"})");
    }

    public JSONObject queryQuestionnaire(String creatorId) {
        return graphMapper.runCypherQuery("S=>(@q Questionnaire: {creatorId=\"" + creatorId +
                "\"}); RETURN q.id, q.title, q.createTime AS createTime SORT_ASC createTime");
    }

    public void assignQuestionnaire(String prop, String id) {
        tableMapper.runSQLQuery("update mygraph set val = " + prop + " where name=\"user_" + id + "\"");
    }
}
