package ucl.ac.uk.ibmpsmwithwatson.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ucl.ac.uk.ibmpsmwithwatson.config.NHSLoginConfig;
import ucl.ac.uk.ibmpsmwithwatson.entity.Token;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import java.net.URI;
import java.util.Objects;

@Component
public class NHSLoginClient {

    @Value("${server.ip}")
    private String IP;

    private final NHSLoginConfig nhsLoginConfig;
    private final RestTemplate restTemplate;

    NHSLoginClient(NHSLoginConfig nhsLoginConfig, RestTemplateBuilder restTemplateBuilder) {
        this.nhsLoginConfig = nhsLoginConfig;
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getAccessToken(@RequestParam("code") String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", "http://" + IP + ":9090/login/nhs");
        map.add("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
        map.add("client_assertion", NHSLoginTokenService.getJws(nhsLoginConfig.getClientId(), nhsLoginConfig.getTokenEndpoint()));

        URI uri = URI.create(nhsLoginConfig.getTokenEndpoint());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);

        ResponseEntity<Token> response = restTemplate.postForEntity(uri, request, Token.class);

        return Objects.requireNonNull(response.getBody()).getAccess_token();
    }

    public User getUserInfo(String accessToken) {
        URI userinfoUri = URI.create(nhsLoginConfig.getUserInfoEndpoint());
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<String> userInfoHttpEntity = new HttpEntity<>(userInfoHeaders);

        return restTemplate.exchange(userinfoUri, HttpMethod.GET, userInfoHttpEntity, User.class).getBody();
    }

}
