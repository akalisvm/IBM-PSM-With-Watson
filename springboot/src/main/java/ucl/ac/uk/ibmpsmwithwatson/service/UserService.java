package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.QuestionnaireMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.UserQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.util.Page;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionnaireMapper questionnaireMapper;

    public Integer getNumberOfPatientsByDoctorId(String doctorId) {
        return (Integer) userMapper.getNumberOfPatientsByDoctorId(doctorId).get("count");
    }

    public Page getPatientsByDoctorId(UserQueryDTO dto) {
        JSONArray jsonArray = userMapper.getPatientsByDoctorId(dto.getDoctorId());
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        SearchingUtil.searchingUser(list, dto.getSearchInput());
        for(User patient : list) {
            if(!patient.getQuestionnaire().equals("")) {
                String title = String.valueOf(questionnaireMapper.getTitle(patient.getQuestionnaire())
                        .getByPath("[0].title"));
                patient.setQuestionnaire(patient.getQuestionnaire() + ": " + title);
            }
        }
        return PaginationUtil.pagination(list, dto.getPageNum(), dto.getPageSize());
    }

    public User getUserById(String userId) {
        JSONArray jsonArray = userMapper.getUserById(userId);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        return list.size() == 0 ? null : list.get(0);
    }
}
