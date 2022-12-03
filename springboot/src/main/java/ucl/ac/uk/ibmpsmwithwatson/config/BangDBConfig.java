package ucl.ac.uk.ibmpsmwithwatson.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BangDBConfig {
    private String environmentUri = "http://43.157.89.132:18080";
    private String serverStatPath = "/server/stat";
    private String dbPath = "/db";
    private String dbnamePath = "/mydb";
    private String queryPath = "/query";
    private String docPath = "/doc";
    private String countPath = "/count";

    public String getServerStatPath() {
        return environmentUri + serverStatPath;
    }
    public String getSQLQueryPath() {
        return environmentUri + dbPath + dbnamePath + queryPath;
    }
    public String getInsertDataPath(String table) {
        return environmentUri + dbPath + dbnamePath + "/" + table + docPath;
    }
    public String getCountPath(String table) {
        return environmentUri + dbPath + dbnamePath + "/" + table + countPath;
    }
}
