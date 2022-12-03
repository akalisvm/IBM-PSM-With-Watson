package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class UserMapper {

    // TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public JSONArray getUserByEmail(String email) {
        return graphMapper.runCypherQuery("S=>(Person:* {email=\""+ email +"\"})\n");
    }

    public static void main(String[] args) {
        UserMapper userMapper = new UserMapper();
        System.out.println(userMapper.getUserByEmail("melina.sela@gmail.com"));
    }
}
