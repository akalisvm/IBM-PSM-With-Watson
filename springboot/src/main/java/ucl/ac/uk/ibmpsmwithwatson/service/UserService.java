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

    public Page getPatientsByDoctorId(UserQueryDTO dto) {
        JSONArray jsonArray = userMapper.getPatientsByDoctorId(dto.getDoctorId());
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        SearchingUtil.searchingUserByName(list, dto.getSearchInput());
        for(User patient : list) {
            if(!patient.getQuestionnaire().equals("")) {
                String title = String.valueOf(questionnaireMapper.getTitle(patient.getQuestionnaire())
                        .getByPath("[0].title"));
                patient.setQuestionnaire(patient.getQuestionnaire() + ": " + title);
            }
        }
        return PaginationUtil.pagination(list, dto.getPageNum(), dto.getPageSize());
    }

    public User getUserById(String id) {
        JSONArray jsonArray = userMapper.getUserById(id);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        if(list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
}
