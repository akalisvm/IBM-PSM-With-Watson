package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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

    public JSONObject queryAllData(String table) {
        return runSQLQuery("select * from " + table);
    }

    public JSONObject queryData(String table, String pk) {
        return runSQLQuery("select * from "+ table +" where _pk=\"" + pk + "\"");
    }

    public void insertData(String table, String pk, String doc) {
        runSQLQuery("insert into " + table + " values \"" + pk + "\" " + doc);
    }

    public JSONObject updateData(String table, String pk, String val) {
        return runSQLQuery("update " + table + " set val = " + val + "where _pk=\"" + pk + "\"");
    }

    public JSONObject deleteData(String table, String pk) {
        return runSQLQuery("delete from " + table + " where _pk=\"" + pk + "\"");
    }

    public int getCount(String label) {
        return (int) runSQLQuery("select count(*) from mygraph where label=\"" + label +"\"").get("retval");
    }
}
