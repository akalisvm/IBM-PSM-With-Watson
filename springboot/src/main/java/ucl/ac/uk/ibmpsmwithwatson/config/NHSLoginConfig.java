package ucl.ac.uk.ibmpsmwithwatson.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class NHSLoginConfig {
    private String environmentUri = "https://oidc.mock.signin.nhs.uk";
    private String tokenPath = "/token";
    private String userInfoPath = "/userinfo";
    private String clientId = "832a7164-93f7-4f23-9c77-4a2205227fab";

    public String getClientId() {
        return clientId;
    }

    public String getTokenEndpoint() {
        return environmentUri + tokenPath;
    }

    public String getUserInfoEndpoint() {
        return environmentUri + userInfoPath;
    }
}