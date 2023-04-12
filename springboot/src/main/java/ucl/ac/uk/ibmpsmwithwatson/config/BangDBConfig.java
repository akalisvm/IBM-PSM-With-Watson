package ucl.ac.uk.ibmpsmwithwatson.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BangDBConfig {
    private String environmentUri = "http://43.157.89.132:18080";
    private String db = "/db";
    private String dbName = "/mydb";
    private String query = "/query";
    private String graph = "/graph";
    private String graphName = "/mygraph";

    public String getSQLQueryPath() {
        return environmentUri + db + dbName + query;
    }
    public String getCypherQueryPath() {
        return environmentUri + graph + graphName + query;
    }
}
