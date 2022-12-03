package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

public class UserMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());

    public JSONArray queryUserByEmail(String email) {
        return tableMapper.runSQLLikeQuery("select * from user where email=\"" + email + "\"");
    }
}
