package ucl.ac.uk.ibmpsmwithwatson.common;

import org.springframework.context.annotation.Configuration;

@Configuration
public class NHSLoginProperties {
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

    public void setEnvironmentUri(String environmentUri) {
        this.environmentUri = environmentUri;
    }

    public void setTokenPath(String tokenPath) {
        this.tokenPath = tokenPath;
    }

    public void setUserInfoPath(String userInfoPath) {
        this.userInfoPath = userInfoPath;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}