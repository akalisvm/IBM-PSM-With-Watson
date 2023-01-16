package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.MyPatientsMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.List;

@Service
public class MyPatientsService {

    @Autowired
    private MyPatientsMapper myPatientsMapper;

    public Page queryPatient(String id, String searchInput, Integer pageNum, Integer pageSize) {
        JSONArray jsonArray = (JSONArray) myPatientsMapper.queryPatient(id).get("rows");
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        SearchingUtil.searchingUserByName(list, searchInput);
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }

    public List<Questionnaire> queryQuestionnaire(String id) {
        JSONArray jsonArray = (JSONArray) myPatientsMapper.queryQuestionnaire(id).get("rows");
        if(jsonArray.size() == 0) {
            return null;
        }
        List<Questionnaire> list = JSONUtil.toList(jsonArray, Questionnaire.class);
        for(Questionnaire questionnaire : list) {
            questionnaire.setTitle(questionnaire.getId() + ": " + questionnaire.getTitle());
        }
        return list;
    }

    public void assignQuestionnaire(String questionnaireId, List<String> ids) {
        for(String id : ids) {
            User patient = queryPatientById(id);
            if(patient != null) {
                patient.setQuestionnaire(questionnaireId);
                JSONObject jsonObject = JSONUtil.parseObj(patient);
                jsonObject.putOpt("label", "User");
                jsonObject.putOpt("name", "user_" + patient.getId());
                myPatientsMapper.assignQuestionnaire(JSONUtil.toJsonStr(jsonObject), id);
            }
        }
    }

    private User queryPatientById(String id) {
        JSONArray jsonArray = (JSONArray) myPatientsMapper.queryPatientById(id).get("rows");
        List<User> userList = JSONUtil.toList(jsonArray, User.class);
        if(userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }
}
