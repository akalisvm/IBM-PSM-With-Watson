package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class QuestionnaireMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public JSONObject insertCount(String label) {
        return tableMapper.runSQLQuery("insert into mygraph values \"" + label + "_count\" {\"count\":\"1\"}");
    }

    public String queryCount(String label) {
        JSONArray jsonArray = (JSONArray) tableMapper.runSQLQuery("select * from mygraph where _pk=\"" + label + "_count\"").get("rows");
        return JSONUtil.parseObj(jsonArray.getByPath("[0].v")).getStr("count");
    }

    public JSONObject updateCount(String label, String count) {
        return tableMapper.runSQLQuery("update mygraph set val={\"count\":\"" + count + "\"} where _pk=\"" + label + "_count\"");
    }

    public JSONObject insertTemplate(String creatorId, String templateId, String templateJson) {
        return graphMapper.runCypherQuery("CREATE (User:user_" + creatorId +
                ")-[CREATED]->(Template:template_" + templateId + " " + templateJson + ")");
    }

    public JSONObject queryTemplates(String creatorId) {
        return graphMapper.runCypherQuery("S=>(Template:* {creatorId=\"" + creatorId + "\"}); RETURN * SORT_DESC createTime");
    }
}
