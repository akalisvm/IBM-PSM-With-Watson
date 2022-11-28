package ucl.ac.uk.ibmpsmwithwatson.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BangDBConfig {
    private String environmentUri = "http://43.157.89.132:18080";
    private String serverStatPath = "/server/stat";

    public String getServerStatPath() {
        return environmentUri + serverStatPath;
    }
}
