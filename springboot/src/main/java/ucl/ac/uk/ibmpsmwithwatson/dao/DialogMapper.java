package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class DialogMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    private static final String DIALOG = "Dialog";

    public void insertCount() {
        tableMapper.insertCount(DIALOG);
    }

    public String getCount() {
        return tableMapper.getCount(DIALOG);
    }

    public void updateCount(String count) {
        tableMapper.updateCount(DIALOG, count);
    }

    public JSONArray getDialog(String patientId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Dialog:* {creatorId=\"" + patientId + "\"}); RETURN * SORT_DESC createTime").get("rows");
    }

    public void insert(String patientId, String dialogId, String dialogProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + patientId +
                ")-[CREATED {\"name\":\"dialog_" + dialogId + "\"}]->(Dialog:dialog_" + dialogId + " " + dialogProp + ")");
    }

    public void update(String dialogId, String dialogProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + dialogProp + " where name=\"dialog_" + dialogId + "\"");
    }
}
