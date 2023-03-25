package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.EventMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.EventQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Event;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.EventVO;
import ucl.ac.uk.ibmpsmwithwatson.util.Page;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventMapper eventMapper;

    @Autowired
    UserService userService;

    public List<Event> getUpcomingEvents(String doctorId) {
        List<Event> eventList = JSONUtil.toList(eventMapper.getUpcomingEvents(doctorId, new Date().getTime()), Event.class);
        return eventList.size() <= 5 ? eventList : eventList.subList(0, 5);
    }

    public Page getEvents(EventQueryDTO dto) {
        List<EventVO> eventVOList = JSONUtil.toList(eventMapper.getEvents(dto), EventVO.class);
        SearchingUtil.searchingEvent(eventVOList, dto);
        for(EventVO eventVO : eventVOList) {
            // eventVO sets the last meeting time
            JSONArray lastMeetingTimeJson = eventMapper.getLastMeetingTime(
                    eventVO.getOrganiserId(),
                    eventVO.getParticipantId(),
                    eventVO.getMeetingTime().getTime());
            if(lastMeetingTimeJson.size() != 0) {
                eventVO.setLastMeetingTime(
                        new Date((Long) lastMeetingTimeJson.getByPath("[0].meetingTime")));
            }
            // eventVO sets the last successful meeting time
            JSONArray lastSuccessfulMeetingTimeJson = eventMapper.getLastSuccessfulMeetingTime(
                    eventVO.getOrganiserId(),
                    eventVO.getParticipantId(),
                    eventVO.getMeetingTime().getTime());
            if(lastSuccessfulMeetingTimeJson.size() != 0) {
                eventVO.setLastSuccessfulMeetingTime(
                        new Date((Long) lastSuccessfulMeetingTimeJson.getByPath("[0].meetingTime")));
            }
            // eventVO sets the next meeting time
            String[] repeat = eventVO.getRepeat().split(" ");
            if(repeat[2].equals("week") || repeat[2].equals("weeks")) {
                eventVO.setNextMeetingTime(DateUtil.offsetWeek(eventVO.getMeetingTime(), Integer.parseInt(repeat[1])));
            } else if(repeat[2].equals("month") || repeat[2].equals("months")) {
                eventVO.setNextMeetingTime(DateUtil.offsetMonth(eventVO.getMeetingTime(), Integer.parseInt(repeat[1])));
            }
        }
        return PaginationUtil.pagination(eventVOList, dto.getPageNum(), dto.getPageSize());
    }

    public void insert(Event event) throws RuntimeException {
        EventQueryDTO dto = new EventQueryDTO();
        dto.setUserRole("doctor");
        dto.setUserId(event.getOrganiserId());
        dto.setPatientFilter(event.getParticipantId());
        dto.setPlatformFilter("");
        dto.setResultFilter("Pending");
        if(JSONUtil.toList(eventMapper.getEvents(dto), EventVO.class).size() != 0) {
            throw new RuntimeException("You have scheduled a meeting with this patient.");
        }
        String id;
        if(eventMapper.getCount() == null) {
            eventMapper.insertCount();
            id = "1";
        } else {
            id = eventMapper.getCount();
        }
        eventMapper.updateCount(String.valueOf(Integer.parseInt(id) + 1));
        event.setId(id);
        event.setCreateTime(new Date());
        event.setResult("Pending");
        event.setFeedback("");
        eventMapper.insert(event.getOrganiserId(), event.getParticipantId(), id, JSONUtil.toJsonStr(event));
        sendEmail("schedule", userService.getUserById(event.getParticipantId()).getEmail(), event);
    }

    public void update(Event event) throws RuntimeException {
        String originalResult = getEventById(event.getId()).getResult();
        JSONObject jsonObject = JSONUtil.parseObj(event);
        jsonObject.putOpt("label", "Event");
        jsonObject.putOpt("name", "event_" + event.getId());
        eventMapper.update(event.getId(), JSONUtil.toJsonStr(jsonObject));
        if(originalResult.equals("Pending") && !event.getResult().equals("Pending")) {
            Event newEvent = new Event();
            newEvent.setOrganiserId(event.getOrganiserId());
            newEvent.setOrganiserName(event.getOrganiserName());
            newEvent.setParticipantId(event.getParticipantId());
            newEvent.setParticipantName(event.getParticipantName());
            newEvent.setTitle(event.getTitle());
            newEvent.setDescription(event.getDescription());
            newEvent.setPlatform(event.getPlatform());
            newEvent.setRepeat(event.getRepeat());
            String[] repeat = event.getRepeat().split(" ");
            if(repeat[2].equals("week") || repeat[2].equals("weeks")) {
                newEvent.setMeetingTime(DateUtil.offsetWeek(event.getMeetingTime(), Integer.parseInt(repeat[1])));
            } else if(repeat[2].equals("month") || repeat[2].equals("months")) {
                newEvent.setMeetingTime(DateUtil.offsetMonth(event.getMeetingTime(), Integer.parseInt(repeat[1])));
            }
            insert(newEvent);
        }
        sendEmail("reschedule", userService.getUserById(event.getParticipantId()).getEmail(), event);
    }

    public void deleteBatch(List<String> eventIdList) {
        for(String eventId : eventIdList) {
            eventMapper.delete(eventId);
        }
    }

    public Event getEventById(String eventId) {
        JSONArray jsonArray = eventMapper.getEventById(eventId);
        List<Event> list = JSONUtil.toList(jsonArray, Event.class);
        return list.size() == 0 ? null : list.get(0);
    }

    public Event getPendingEventById(String patientId) {
        JSONArray jsonArray = eventMapper.getPendingEventById(patientId);
        List<Event> list = JSONUtil.toList(jsonArray, Event.class);
        return list.size() == 0 ? null : list.get(0);
    }

    private void sendEmail(String type, String email, Event event) {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        HashMap<String, String> map = new HashMap<>();
        map.put("participantName", event.getParticipantName());
        map.put("organiserName", event.getOrganiserName());
        map.put("title", event.getTitle());
        map.put("description", event.getDescription());
        map.put("platform", event.getPlatform());
        map.put("meetingTime", event.getMeetingTime().toString());
        map.put("repeat", event.getRepeat());
        if(type.equals("schedule")) {
            Template template = engine.getTemplate("schedule.html");
            String content = template.render(map);
            MailUtil.send(email, "Outreach event scheduled", content, true);
        } else if(type.equals("reschedule")) {
            Template template = engine.getTemplate("schedule.html");
            String content = template.render(map);
            MailUtil.send(email, "Outreach event rescheduled", content, true);
        }
    }
}
