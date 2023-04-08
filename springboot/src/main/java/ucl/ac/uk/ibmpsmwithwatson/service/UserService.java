package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.QuestionnaireMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.UserQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.UserVO;
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

    @Autowired
    DialogService dialogService;

    public Integer getNumberOfPatientsByDoctorId(String doctorId) {
        return (Integer) userMapper.getNumberOfPatientsByDoctorId(doctorId).get("count");
    }

    public Page getPatientsByDoctorId(UserQueryDTO dto) {
        JSONArray jsonArray = userMapper.getPatientsByDoctorId(dto.getDoctorId());
        List<UserVO> list = JSONUtil.toList(jsonArray, UserVO.class);
        SearchingUtil.searchingUser(list, dto.getSearchInput());
        for(UserVO patient : list) {
            if(!patient.getQuestionnaire().equals("")) {
                String title = String.valueOf(questionnaireMapper.getTitle(patient.getQuestionnaire())
                        .getByPath("[0].title"));
                patient.setQuestionnaire(patient.getQuestionnaire() + ": " + title);
            }
            if(dialogService.getDialog(patient.getId()) != null) {
                patient.setDialog("true");
            } else {
                patient.setDialog("false");
            }
        }
        return PaginationUtil.pagination(list, dto.getPageNum(), dto.getPageSize());
    }

    public User getUserById(String userId) {
        JSONArray jsonArray = userMapper.getUserById(userId);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        return list.size() == 0 ? null : list.get(0);
    }

    public User getUserByEmail(String email) {
        JSONArray jsonArray = userMapper.getUserByEmail(email);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        return list.size() == 0 ? null : list.get(0);
    }
}
