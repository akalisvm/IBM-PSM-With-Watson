package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

import java.net.URI;
import java.util.Objects;

public class UserMapper {

    private final BangDBConfig bangDBConfig;
    private final RestTemplate restTemplate;

    UserMapper(BangDBConfig bangDBConfig, RestTemplateBuilder restTemplateBuilder) {
        this.bangDBConfig = bangDBConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public JSONArray getAllUser() {
        URI uri = URI.create(bangDBConfig.getSQLQueryPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject body = new JSONObject();
        body.set("sql", "select * from users");
        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(uri, request, JSONObject.class);
        return (JSONArray) Objects.requireNonNull(response.getBody()).get("rows");
    }

    public static void main(String[] args) {
        UserMapper mapper = new UserMapper(new BangDBConfig(), new RestTemplateBuilder());
        System.out.println(mapper.getAllUser());
    }
}
