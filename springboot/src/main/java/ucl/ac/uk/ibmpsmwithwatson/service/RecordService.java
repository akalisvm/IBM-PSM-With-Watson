package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.RecordMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Record;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.Date;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    RecordMapper recordMapper;

    public Page getRecords(String patientId, String searchInput, String resultFilter,
                           String needMeetingFilter, Integer pageNum, Integer pageSize) {
        JSONArray jsonArray = recordMapper.getRecords(patientId);
        List<Record> list = JSONUtil.toList(jsonArray, Record.class);
        SearchingUtil.searchingRecordByIdAndFilters(list, searchInput, resultFilter, needMeetingFilter);
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
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
