package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

import java.net.URI;
import java.util.Objects;

public class GraphMapper {

    private final BangDBConfig bangDBConfig;
    private final RestTemplate restTemplate;

    public GraphMapper(BangDBConfig bangDBConfig, RestTemplateBuilder restTemplateBuilder) {
        this.bangDBConfig = bangDBConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public JSONObject addNode(String label, String name, String prop) {
        URI uri = URI.create(bangDBConfig.getAddNodePath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.set("label", label);
        body.set("name", name);
        body.set("prop", prop);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<JSONObject> response = restTemplate.exchange(uri, HttpMethod.POST, entity, JSONObject.class);
        return response.getBody();
    }

    public JSONArray runCypherQuery(String cypher) {
        URI uri = URI.create(bangDBConfig.getCypherQueryPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(cypher, headers);
        ResponseEntity<JSONObject> response = restTemplate.exchange(uri, HttpMethod.POST, entity, JSONObject.class);
        return (JSONArray) Objects.requireNonNull(response.getBody()).get("rows");
    }
}
