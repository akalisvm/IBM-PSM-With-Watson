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

public class Test {

    private final BangDBConfig bangDBConfig;
    private final RestTemplate restTemplate;

    Test(BangDBConfig bangDBConfig, RestTemplateBuilder restTemplateBuilder) {
        this.bangDBConfig = bangDBConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public JSONObject getServerStat() {
        URI serverStatUri = URI.create(bangDBConfig.getServerStatPath());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setContentType(MediaType.TEXT_PLAIN);
        httpHeaders.setAccessControlAllowOrigin("*");
        httpHeaders.setAccessControlMaxAge(3600);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(serverStatUri, HttpMethod.GET, httpEntity, JSONObject.class).getBody();
    }

    public static void main(String[] args) {
        Test test = new Test(new BangDBConfig(), new RestTemplateBuilder());
        System.out.println(test.getServerStat().toStringPretty());
    }
}
