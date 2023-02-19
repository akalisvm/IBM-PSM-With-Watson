package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class UserMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public Integer getNHSUserId() {
        return (Integer) graphMapper.runCypherQuery("S=>(User:*); RETURN COUNT(*) AS count").get("count") + 1;
    }

    public JSONArray getUserById(String userId) {
        return (JSONArray) graphMapper.runCypherQuery("S=>(User:* {id=\"" + userId + "\"})").get("rows");
    }

    public JSONArray getUserByEmail(String email) {
        return (JSONArray) graphMapper.runCypherQuery("S=>(User:* {email=\"" + email + "\"})").get("rows");
    }

    public JSONObject getNumberOfPatientsByDoctorId(String doctorId) {
        return graphMapper.runCypherQuery("S=>(User:* {doctor=\"" + doctorId + "\"}); RETURN COUNT(*) AS count");
    }

    public JSONArray getPatientsByDoctorId(String doctorId) {
        return (JSONArray) graphMapper.runCypherQuery("S=>(User:* {doctor=\"" + doctorId + "\"})").get("rows");
    }

    public void update(String patientId, String patientProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + patientProp + " where name=\"user_" + patientId + "\"");
    }
}
