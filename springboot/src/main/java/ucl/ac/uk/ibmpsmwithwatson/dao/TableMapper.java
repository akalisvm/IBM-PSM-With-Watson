package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

import java.net.URI;
import java.util.Objects;

public class TableMapper {

    private final BangDBConfig bangDBConfig;
    private final RestTemplate restTemplate;

    public TableMapper(BangDBConfig bangDBConfig, RestTemplateBuilder restTemplateBuilder) {
        this.bangDBConfig = bangDBConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public JSONObject runSQLQuery(String sql) {
        URI uri = URI.create(bangDBConfig.getSQLQueryPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.set("sql", sql);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<JSONObject> response = restTemplate.exchange(uri, HttpMethod.POST, entity, JSONObject.class);
        return Objects.requireNonNull(response.getBody());
    }

    public int getRetvalCount(String label) {
        return (int) runSQLQuery("select count(*) from mygraph where label=\"" + label +"\"").get("retval");
    }

    public void insertCount(String label) {
        runSQLQuery("insert into mygraph values \"" + label + "_count\" {\"count\":\"1\"}");
    }

    public String getCount(String label) {
        JSONArray jsonArray = (JSONArray) runSQLQuery("select * from mygraph where _pk=\"" + label + "_count\"").get("rows");
        return JSONUtil.parseObj(jsonArray.getByPath("[0].v")).getStr("count");
    }

    public void updateCount(String label, String count) {
        runSQLQuery("update mygraph set val={\"count\":\"" + count + "\"} where _pk=\"" + label + "_count\"");
    }
}
