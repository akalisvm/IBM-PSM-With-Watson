package ucl.ac.uk.ibmpsmwithwatson.common;

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
import ucl.ac.uk.ibmpsmwithwatson.entity.Token;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;

import java.net.URI;
import java.util.Objects;

@Component
public class NHSLoginClient {

    private static final String GRANT_TYPE = "authorization_code";
    private static final String CLIENT_ASSERTION_TYPE = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";
    private static final String REDIRECT_URI = "http://localhost:9090/callback";

    private final NHSLoginProperties nhsLoginProperties;
    private final RestTemplate restTemplate;

    NHSLoginClient(NHSLoginProperties nhsLoginProperties, RestTemplateBuilder restTemplateBuilder) {
        this.nhsLoginProperties = nhsLoginProperties;
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getAccessToken(@RequestParam("code") String code) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", GRANT_TYPE);
        map.add("code", code);
        map.add("redirect_uri", REDIRECT_URI);
        map.add("client_assertion_type", CLIENT_ASSERTION_TYPE);
        map.add("client_assertion", TokenService.getJws(nhsLoginProperties.getClientId(), nhsLoginProperties.getTokenEndpoint()));

        URI uri = URI.create(nhsLoginProperties.getTokenEndpoint());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);

        ResponseEntity<Token> response = restTemplate.postForEntity(uri, request, Token.class);

        return Objects.requireNonNull(response.getBody()).getAccess_token();
    }

    public User getUserInfo(String accessToken) {
        URI userinfoUri = URI.create(nhsLoginProperties.getUserInfoEndpoint());
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken);

        HttpEntity<String> userInfoHttpEntity = new HttpEntity<>(userInfoHeaders);

        return restTemplate.exchange(userinfoUri, HttpMethod.GET, userInfoHttpEntity, User.class).getBody();
    }

}
