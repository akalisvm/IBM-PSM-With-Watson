package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class TemplateMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public String getTemplateCount() {
        return tableMapper.getCount("Template");
    }

    public void insertTemplateCount() {
        tableMapper.insertCount("Template");
    }

    public void updateTemplateCount(String count) {
        tableMapper.updateCount("Template", count);
    }

    public JSONObject getNumberOfTemplatesByDoctorId(String doctorId) {
        return graphMapper.runCypherQuery("S=>(Template:* {creatorId=\"" + doctorId + "\"}); RETURN COUNT(*) AS count");
    }

    public JSONArray getTemplates(String doctorId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Template:* {creatorId=\"" + doctorId + "\"}); RETURN * SORT_ASC createTime").get("rows");
    }

    public void insertTemplate(String doctorId, String templateId, String templateProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + doctorId +
                ")-[CREATED {\"name\":\"template_" + templateId + "\"}]->(Template:template_" + templateId + " " + templateProp + ")");
    }

    public void updateTemplate(String templateId, String templateProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + templateProp + " where name=\"template_" + templateId + "\"");
    }

    public void deleteTemplate(String templateId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"template_" + templateId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"template_" + templateId + "\"");
    }
}
