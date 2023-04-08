package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class DialogMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public String getDialogCount() {
        return tableMapper.getCount("Dialog");
    }

    public void insertDialogCount() {
        tableMapper.insertCount("Dialog");
    }

    public void updateDialogCount(String count) {
        tableMapper.updateCount("Dialog", count);
    }

    public JSONArray getDialog(String patientId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Dialog:* {creatorId=\"" + patientId + "\"}); RETURN * SORT_DESC createTime").get("rows");
    }

    public void insertDialog(String patientId, String dialogId, String dialogProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + patientId +
                ")-[CREATED {\"name\":\"dialog_" + dialogId + "\"}]->(Dialog:dialog_" + dialogId + " " + dialogProp + ")");
    }

    public void updateDialog(String dialogId, String dialogProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + dialogProp + " where name=\"dialog_" + dialogId + "\"");
    }
}
