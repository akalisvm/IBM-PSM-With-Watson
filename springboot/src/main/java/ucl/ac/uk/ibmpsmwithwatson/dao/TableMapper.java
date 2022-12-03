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

    public JSONArray runSQLQuery(String sql) {
        URI uri = URI.create(bangDBConfig.getSQLQueryPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.set("sql", sql);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<JSONObject> response = restTemplate.exchange(uri, HttpMethod.POST, entity, JSONObject.class);
        System.out.println(response);
        return (JSONArray) Objects.requireNonNull(response.getBody()).get("rows");
    }

    public JSONArray queryAllData(String table) {
        return runSQLQuery("select * from " + table);
    }

    public JSONArray queryData(String table, String pk) {
        return runSQLQuery("select * from "+ table +" where _pk=\"" + pk + "\"");
    }

    public void insertData(String table, String pk, String doc) {
        runSQLQuery("insert into " + table + " values \"" + pk + "\" " + doc);
    }

    public JSONArray updateData(String table, String pk, String val) {
        return runSQLQuery("update " + table + " set val = " + val + "where _pk=\"" + pk + "\"");
    }

    public JSONArray deleteData(String table, String pk) {
        return runSQLQuery("delete from " + table + " where _pk=\"" + pk + "\"");
    }

    public int getCount(String table) {
        URI uri = URI.create(bangDBConfig.getCountPath(table));
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return (int) Objects.requireNonNull(restTemplate.exchange(uri, HttpMethod.GET, entity, JSONObject.class).getBody()).get("count");
    }

    public static void main(String[] args) {
        TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
        // System.out.println(mapper.queryAllData());
        // System.out.println(mapper.insertData("test", "user4", "{\"name\":\"Arjun\",\"org\":\"Google\",\"likes\":[\"books\",\"basketball\",\"food\"]}}"));
        // System.out.println(mapper.updateData("test", "user3", "{\"firstname\":\"sinha\",\"city\":\"delhi\"}"));
        // System.out.println(mapper.queryData("test", "user3"));
        // System.out.println(mapper.deleteData("test", "user1"));
        // System.out.println(mapper.queryAllData());
        System.out.println(tableMapper.getCount("test"));
    }
}
