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

    public JSONObject runCypherQuery(String cypher) {
        URI uri = URI.create(bangDBConfig.getCypherQueryPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(cypher, headers);
        ResponseEntity<JSONObject> response = restTemplate.exchange(uri, HttpMethod.POST, entity, JSONObject.class);
        return Objects.requireNonNull(response.getBody());
    }

    public void addNode(String label, String name, String prop) {
        runCypherQuery("CREATE (" + label + ":" + name + " " + prop + ")");
    }
}
