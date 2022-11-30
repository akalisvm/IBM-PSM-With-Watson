package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

import java.net.URI;

public class ServerStatMapper {

    private final BangDBConfig bangDBConfig;
    private final RestTemplate restTemplate;

    ServerStatMapper(BangDBConfig bangDBConfig, RestTemplateBuilder restTemplateBuilder) {
        this.bangDBConfig = bangDBConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public JSONObject getServerStat() {
        URI uri = URI.create(bangDBConfig.getServerStatPath());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setAccessControlAllowOrigin("*");
        headers.setAccessControlMaxAge(3600);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(uri, HttpMethod.GET, entity, JSONObject.class).getBody();
    }

    public static void main(String[] args) {
        ServerStatMapper mapper = new ServerStatMapper(new BangDBConfig(), new RestTemplateBuilder());
        System.out.println(mapper.getServerStat().toStringPretty());
    }
}
