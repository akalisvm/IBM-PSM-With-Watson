package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.RecordMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Record;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.*;

@Service
public class RecordService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RecordMapper recordMapper;

    public Page getRecordsForPatient(String patientId, String searchInput, String resultFilter,
                           String needMeetingFilter, Integer pageNum, Integer pageSize) {
        JSONArray jsonArray = recordMapper.getRecords(patientId);
        List<Record> list = JSONUtil.toList(jsonArray, Record.class);
        SearchingUtil.searchingRecordByIdAndFilters(list, searchInput, "", resultFilter, needMeetingFilter);
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }

    public Page getRecordsForDoctor(String doctorId, String searchInput, String patientFilter,
                                    String resultFilter, String needMeetingFilter, Integer pageNum, Integer pageSize) {
        JSONArray jsonArray = userMapper.getPatientsByDoctorId(doctorId);
        List<User> userList = JSONUtil.toList(jsonArray, User.class);
        List<Record> recordList = new ArrayList<>();
        for(User user : userList) {
            recordList.addAll(JSONUtil.toList(recordMapper.getRecords(user.getId()), Record.class));
        }
        SearchingUtil.searchingRecordByIdAndFilters(recordList, searchInput, patientFilter, resultFilter, needMeetingFilter);
        recordList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        return PaginationUtil.pagination(recordList, pageNum, pageSize, recordList.size());
    }

    public void insert(Record record) {
        String id;
        if(recordMapper.getCount() == null) {
            recordMapper.insertCount();
            id = "1";
        } else {
            id = recordMapper.getCount();
        }
        recordMapper.updateCount(String.valueOf(Integer.parseInt(id) + 1));
        record.setId(id);
        record.setCreateTime(new Date());
        if(record.getQuestionnaire().getNeedMeeting().equals("")) {
            record.getQuestionnaire().setNeedMeeting("No");
        }
        recordMapper.insert(record.getCreatorId(), id, JSONUtil.toJsonStr(record));
    }

    public void delete(String recordId) {
        recordMapper.delete(recordId);
    }
}
