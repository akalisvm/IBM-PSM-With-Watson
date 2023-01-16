package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class TemplateMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    private static final String TEMPLATE = "Template";

    public void insertCount() {
        tableMapper.insertCount(TEMPLATE);
    }

    public String queryCount() {
        return tableMapper.queryCount(TEMPLATE);
    }

    public void updateCount(String count) {
        tableMapper.updateCount(TEMPLATE, count);
    }

    public void insert(String creatorId, String templateId, String templateProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + creatorId +
                ")-[CREATED {\"name\":\"template_" + templateId + "\"}]->(Template:template_" + templateId + " " + templateProp + ")");
    }

    public JSONObject query(String creatorId) {
        return graphMapper.runCypherQuery("S=>(Template:* {creatorId=\"" + creatorId + "\"}); RETURN * SORT_ASC createTime");
    }

    public void update(String templateId, String templateProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + templateProp + " where name=\"template_" + templateId + "\"");
    }

    public void delete(String templateId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"template_" + templateId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"template_" + templateId + "\"");
    }
}
