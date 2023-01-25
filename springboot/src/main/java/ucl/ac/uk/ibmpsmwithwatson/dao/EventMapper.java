package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;

@Component
public class EventMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    private static final String EVENT = "Event";

    public void insertCount() {
        tableMapper.insertCount(EVENT);
    }

    public String getCount() {
        return tableMapper.getCount(EVENT);
    }

    public void updateCount(String count) {
        tableMapper.updateCount(EVENT, count);
    }

    public JSONArray getEventsByPatientId(String patientId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Event:* {participantId=\"" + patientId + "\"}); RETURN * SORT_DESC createTime").get("rows");
    }

    public JSONArray getEventsByDoctorId(String doctorId) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(Event:* {organiserId=\"" + doctorId + "\"}); RETURN * SORT_DESC createTime").get("rows");
    }

    public void insert(String doctorId, String patientId, String eventId, String eventProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + doctorId +
                ")-[SCHEDULED {\"name\":\"event_" + eventId + "\"}]->(Event:event_" + eventId + " " + eventProp + ")");
        graphMapper.runCypherQuery("CREATE (User:user_" + patientId +
                ")-[SHOULD_ATTEND {\"name\":\"event_" + eventId + "\"}]->(Event:event_" + eventId + ")");
    }

    public void update(String eventId, String eventProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + eventProp + " where name=\"event_" + eventId + "\"");
    }

    public void delete(String eventId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"event_" + eventId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"event_" + eventId + "\"");
    }
}
