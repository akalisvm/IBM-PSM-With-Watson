package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class RecordMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public String getRecordCount() {
        return tableMapper.getCount("Record");
    }

    public void insertRecordCount() {
        tableMapper.insertCount("Record");
    }

    public void updateRecordCount(String count) {
        tableMapper.updateCount("Record", count);
    }

    public Integer getNumberOfRecordsByPatientId(String patientId, Long startTime, Long endTime) {
        return (Integer) graphMapper.runCypherQuery("S=>(@r Record:*); " +
                "RETURN r.id AS id r.createTime AS createTime r.creatorId AS creatorId " +
                "WHERE createTime>=" + startTime + " createTime<=" + endTime + " creatorId=\"" + patientId + "\"").get("num_items");
    }

    public JSONArray getRecords(String patientId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Record:* {creatorId=\"" + patientId + "\"}); RETURN * SORT_DESC createTime").get("rows");
    }

    public void insertRecord(String patientId, String recordId, String recordProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + patientId +
                ")-[CREATED {\"name\":\"record_" + recordId + "\"}]->(Record:record_" + recordId + " " + recordProp + ")");
    }

    public void deleteRecord(String recordId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"record_" + recordId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"record_" + recordId + "\"");
    }
}
