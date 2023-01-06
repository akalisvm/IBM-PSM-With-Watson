package ucl.ac.uk.ibmpsmwithwatson.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class BangDBConfig {
    private String environmentUri = "http://43.157.89.132:18080";
    private String stat = "/server/stat";
    private String db = "/db";
    private String dbname = "/mydb";
    private String query = "/query";
    private String doc = "/doc";
    private String count = "/count";
    private String graph = "/graph";
    private String graph_name = "/mygraph";
    private String node = "/node";
    private String triple = "/triple";

    public String getServerStatPath() {
        return environmentUri + stat;
    }
    public String getSQLQueryPath() {
        return environmentUri + db + dbname + query;
    }
    public String getInsertDataPath(String table) {
        return environmentUri + db + dbname + "/" + table + doc;
    }
    public String getCountPath(String table) {
        return environmentUri + db + dbname + "/" + table + count;
    }
    public String getAddNodePath() {
        return environmentUri + graph + graph_name + node;
    }
    public String getAddTriplePath() {
        return environmentUri + graph + graph_name + triple;
    }
    public String getCypherQueryPath() {
        return environmentUri + graph + graph_name + query;
    }
}
