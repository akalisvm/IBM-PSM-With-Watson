package ucl.ac.uk.ibmpsmwithwatson.dao;

import cn.hutool.json.JSONArray;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import ucl.ac.uk.ibmpsmwithwatson.config.BangDBConfig;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.EventQueryDTO;

@Component
public class EventMapper {

    TableMapper tableMapper = new TableMapper(new BangDBConfig(), new RestTemplateBuilder());
    GraphMapper graphMapper = new GraphMapper(new BangDBConfig(), new RestTemplateBuilder());

    public String getEventCount() {
        return tableMapper.getCount("Event");
    }

    public void insertEventCount() {
        tableMapper.insertCount("Event");
    }

    public void updateEventCount(String count) {
        tableMapper.updateCount("Event", count);
    }

    public JSONArray getUpcomingEvents(String doctorId, Long meetingTime) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(@e Event:*); " +
                        "RETURN " +
                        "e.id AS id " +
                        "e.organiserId AS organiserId " +
                        "e.participantName AS participantName " +
                        "e.title AS title " +
                        "e.platform AS platform " +
                        "e.meetingTime AS meetingTime " +
                        "e.repeat AS repeat " +
                        "WHERE " +
                        "organiserId=\"" + doctorId + "\" " +
                        "meetingTime>" + meetingTime + " " +
                        "SORT_ASC meetingTime"
        ).get("rows");
    }

    public JSONArray getEvents(EventQueryDTO dto) {
        StringBuilder cypher = new StringBuilder("S=>(@e Event:*); " +
                "RETURN " +
                "e.id AS id " +
                "e.createTime AS createTime " +
                "e.organiserId AS organiserId " +
                "e.organiserName AS organiserName " +
                "e.participantId AS participantId " +
                "e.participantName AS participantName " +
                "e.title AS title " +
                "e.description AS description " +
                "e.platform AS platform " +
                "e.meetingTime AS meetingTime " +
                "e.repeat AS repeat " +
                "e.result AS result " +
                "e.feedback AS feedback " +
                "WHERE ");
        if(dto.getUserRole().equals("doctor")) {
            cypher.append("organiserId=\"").append(dto.getUserId()).append("\" ");
            if(!dto.getPatientFilter().equals("")) {
                cypher.append("participantId=\"").append(dto.getPatientFilter()).append("\" ");
            }
        } else if(dto.getUserRole().equals("patient")) {
            cypher.append("participantId=\"").append(dto.getUserId()).append("\" ");
        }
        if(!dto.getPlatformFilter().equals("")) {
            cypher.append("platform=\"").append(dto.getPlatformFilter()).append("\" ");
        }
        if(!dto.getResultFilter().equals("")) {
            cypher.append("result=\"").append(dto.getResultFilter()).append("\" ");
        }
        cypher.append("SORT_DESC createTime");
        return (JSONArray) graphMapper.runCypherQuery(cypher.toString()).get("rows");
    }

    public JSONArray getPendingEventById(String patientId) {
        String cypher = "S=>(@e Event:*); " +
                "RETURN " +
                "e.id AS id " +
                "e.createTime AS createTime " +
                "e.organiserId AS organiserId " +
                "e.organiserName AS organiserName " +
                "e.participantId AS participantId " +
                "e.participantName AS participantName " +
                "e.title AS title " +
                "e.description AS description " +
                "e.platform AS platform " +
                "e.meetingTime AS meetingTime " +
                "e.repeat AS repeat " +
                "e.result AS result " +
                "e.feedback AS feedback " +
                "WHERE participantId=\"" + patientId + "\" result=\"Pending\"";
        return (JSONArray) graphMapper.runCypherQuery(cypher).get("rows");
    }

    public JSONArray getEventById(String eventId) {
        return (JSONArray) graphMapper.runCypherQuery("S=>(Event:* {id=\"" + eventId + "\"})").get("rows");
    }

    public JSONArray getLastMeetingTime(String doctorId, String patientId, Long meetingTime) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(@e Event:*); " +
                        "RETURN " +
                        "e.id AS id " +
                        "e.organiserId AS organiserId " +
                        "e.participantId AS participantId " +
                        "e.meetingTime AS meetingTime " +
                        "WHERE " +
                        "organiserId=\"" + doctorId + "\" " +
                        "participantId=\"" + patientId + "\" " +
                        "meetingTime<" + meetingTime + " " +
                        "SORT_DESC meetingTime"
        ).get("rows");
    }

    public JSONArray getLastSuccessfulMeetingTime(String doctorId, String patientId, Long meetingTime) {
        return (JSONArray) graphMapper.runCypherQuery(
                "S=>(@e Event:*); " +
                        "RETURN " +
                        "e.id AS id " +
                        "e.organiserId AS organiserId " +
                        "e.participantId AS participantId " +
                        "e.meetingTime AS meetingTime " +
                        "e.result AS result " +
                        "WHERE " +
                        "organiserId=\"" + doctorId + "\" " +
                        "participantId=\"" + patientId + "\" " +
                        "meetingTime<" + meetingTime + " " +
                        "result=\"Success\" " +
                        "SORT_DESC meetingTime"
        ).get("rows");
    }

    public void insertEvent(String doctorId, String patientId, String eventId, String eventProp) {
        graphMapper.runCypherQuery("CREATE (User:user_" + doctorId +
                ")-[SCHEDULED {\"name\":\"event_" + eventId + "\"}]->(Event:event_" + eventId + " " + eventProp + ")");
        graphMapper.runCypherQuery("CREATE (User:user_" + patientId +
                ")-[SHOULD_ATTEND {\"name\":\"event_" + eventId + "\"}]->(Event:event_" + eventId + ")");
    }

    public void updateEvent(String eventId, String eventProp) {
        tableMapper.runSQLQuery("update mygraph set val = " + eventProp + " where name=\"event_" + eventId + "\"");
    }

    public void deleteEvent(String eventId) {
        tableMapper.runSQLQuery("delete from mygraph_rel where name=\"event_" + eventId + "\"");
        tableMapper.runSQLQuery("delete from mygraph where name=\"event_" + eventId + "\"");
    }
}
