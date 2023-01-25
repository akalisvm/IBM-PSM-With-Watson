package ucl.ac.uk.ibmpsmwithwatson.service;

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
        return PaginationUtil.pagination(eventVOList, dto.getPageNum(), dto.getPageSize());
    }

    public void insert(Event event) {
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

    public void update(Event event) {
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
