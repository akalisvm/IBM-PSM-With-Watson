package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class RecordMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    private static final String RECORD = "Record";

    public void insertCount() {
        tableMapper.insertCount(RECORD);
    }

    public String getCount() {
        return tableMapper.getCount(RECORD);
    }

    public void updateCount(String count) {
        tableMapper.updateCount(RECORD, count);
    }

    public JSONArray getRecords(String patientId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Record:* {creatorId=\"" + patientId + "\"}); RETURN * SORT_DESC createTime").get("rows");
    }

    public void insert(String patientId, String recordId, String recordProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + patientId +
                ")-[CREATED {\"name\":\"record_" + recordId + "\"}]->(Record:record_" + recordId + " " + recordProp + ")");
    }

    public void update(String recordId, String recordProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + recordProp + " where name=\"record_" + recordId + "\"");
    }

    public void delete(String recordId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"record_" + recordId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"record_" + recordId + "\"");
    }
}
