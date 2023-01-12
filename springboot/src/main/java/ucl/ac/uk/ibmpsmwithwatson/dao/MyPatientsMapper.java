package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class MyPatientsMapper {

    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public JSONObject queryPatient(String doctor) {
        return graphMapper.runCypherQuery("S=>(User:* {doctor=\"" + doctor + "\"})");
    }
}
