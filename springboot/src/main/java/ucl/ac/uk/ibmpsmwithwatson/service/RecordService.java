package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.RecordMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.RecordQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.util.Page;
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

    public List<String> getHistoryXData() {
        List<String> xData = new ArrayList<>();
        Date today = DateUtil.endOfDay(new Date());
        xData.add(DateUtil.format(today, "yyyy-MM-dd"));
        for(int i = 1; i < 7; i++) {
            xData.add(0, DateUtil.format(DateUtil.offsetDay(today, -1 * i), "yyyy-MM-dd"));
        }
        return xData;
    }

    public List<Integer> getHistoryYData(String doctorId) {
        List<User> patientList = JSONUtil.toList(userMapper.getPatientsByDoctorId(doctorId), User.class);
        Long[] xData = new Long[8];
        Date today = DateUtil.endOfDay(new Date());
        xData[7] = today.getTime();
        for(int i = 1; i <= 7; i++) {
            xData[7 - i] = DateUtil.offsetDay(today, -1 * i).getTime();
        }
        List<Integer> yData = new ArrayList<>();
        for(int i = 1; i <= 7; i++) {
            int count = 0;
            for(User patient : patientList) {
                count += recordMapper.getNumberOfRecordsByPatientId(patient.getId(), xData[i - 1], xData[i]);
            }
            yData.add(count);
        }
        return yData;
    }

    public Page getRecords(RecordQueryDTO dto) {
        List<RecordVO> recordVOList = new ArrayList<>();
        if(dto.getUserRole().equals("patient")) {
            recordVOList = JSONUtil.toList(recordMapper.getRecords(dto.getUserId()), RecordVO.class);
        } else if(dto.getUserRole().equals("doctor")) {
            List<User> patientList = JSONUtil.toList(userMapper.getPatientsByDoctorId(dto.getUserId()), User.class);
            for(User patient : patientList) {
                List<RecordVO> tempList = JSONUtil.toList(recordMapper.getRecords(patient.getId()), RecordVO.class);
                for(RecordVO recordVO : tempList) {
                    recordVO.setCreatorName(patient.getGiven_name() + " " + patient.getFamily_name());
                }
                recordVOList.addAll(tempList);
            }
            recordVOList.sort((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()));
        }
        SearchingUtil.searchingRecord(recordVOList, dto);
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
