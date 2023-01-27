package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.core.date.DateUtil;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    EventMapper eventMapper;

    public Page getEvents(EventQueryDTO dto) {
        List<EventVO> eventVOList = new ArrayList<>();
        if(dto.getUserRole().equals("patient")) {
            eventVOList = JSONUtil.toList(eventMapper.getEventsByPatientId(dto.getUserId()), EventVO.class);
        } else if(dto.getUserRole().equals("doctor")) {
            eventVOList = JSONUtil.toList(eventMapper.getEventsByDoctorId(dto.getUserId()), EventVO.class);
        }
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
            if(eventVO.getRepeat().equals("Weekly")) {
                eventVO.setNextMeetingTime(DateUtil.offsetWeek(eventVO.getMeetingTime(), 1));
            } else if(eventVO.getRepeat().equals("Monthly")) {
                eventVO.setNextMeetingTime(DateUtil.offsetMonth(eventVO.getMeetingTime(), 1));
            }
        }
        return PaginationUtil.pagination(eventVOList, dto.getPageNum(), dto.getPageSize());
    }

    public void insert(Event event) throws RuntimeException {
        if(eventMapper.getEventByMeetingTime(event.getOrganiserId(), event.getMeetingTime().getTime()).size() != 0) {
            throw new RuntimeException("Time slot has been occupied, please select another time and try again.");
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
    }

    public void update(Event event) throws RuntimeException {
        JSONArray temp = eventMapper.getEventByMeetingTime(event.getOrganiserId(), event.getMeetingTime().getTime());
        if(event.getResult().equals("Pending")
                && temp.size() != 0
                && !event.getId().equals(temp.getByPath("[0].id"))) {
            throw new RuntimeException("Time slot has been occupied, please select another time and try again.");
        }
        JSONObject jsonObject = JSONUtil.parseObj(event);
        jsonObject.putOpt("label", "Event");
        jsonObject.putOpt("name", "event_" + event.getId());
        eventMapper.update(event.getId(), JSONUtil.toJsonStr(jsonObject));
    }

    public void deleteBatch(List<String> eventIdList) {
        for(String eventId : eventIdList) {
            eventMapper.delete(eventId);
        }
    }
}
