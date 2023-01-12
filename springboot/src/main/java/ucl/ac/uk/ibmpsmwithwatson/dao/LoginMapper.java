package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class LoginMapper {

    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public JSONObject queryUserById(String id) {
        return graphMapper.runCypherQuery("S=>(User:* {id=\"" + id + "\"})");
    }

    public JSONObject queryUserByEmail(String email) {
        return graphMapper.runCypherQuery("S=>(User:* {email=\"" + email + "\"})");
    }
}
