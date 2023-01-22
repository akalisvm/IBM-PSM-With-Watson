package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.RecordMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.RecordQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.Page;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Record;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.RecordVO;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.*;

@Service
public class RecordService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RecordMapper recordMapper;

    public Page getRecords(RecordQueryDTO dto) {
        List<RecordVO> recordVOList = new ArrayList<>();
        if(dto.getUserRole().equals("patient")) {
            recordVOList = JSONUtil.toList(recordMapper.getRecords(dto.getUserId()), RecordVO.class);
        } else if(dto.getUserRole().equals("doctor")) {
            List<User> userList = JSONUtil.toList(userMapper.getPatientsByDoctorId(dto.getUserId()), User.class);
            for(User user : userList) {
                List<RecordVO> tempList = JSONUtil.toList(recordMapper.getRecords(user.getId()), RecordVO.class);
                for(RecordVO recordVO : tempList) {
                    recordVO.setCreatorName(user.getGiven_name() + " " + user.getFamily_name());
                }
                recordVOList.addAll(tempList);
            }
            recordVOList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        }
        SearchingUtil.searchingRecordByIdAndFilters(recordVOList, dto);
        return PaginationUtil.pagination(recordVOList, dto.getPageNum(), dto.getPageSize());
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

    public void deleteBatch(List<String> recordIdList) {
        for(String recordId : recordIdList) {
            recordMapper.delete(recordId);
        }
    }
}
