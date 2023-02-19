package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.QuestionnaireMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.QuestionnaireQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.util.Page;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.Date;
import java.util.List;

@Service
public class QuestionnaireService {

    @Autowired
    QuestionnaireMapper questionnaireMapper;

    @Autowired
    UserMapper userMapper;

    public Integer getNumberOfQuestionnairesByDoctorId(String doctorId) {
        return (Integer) questionnaireMapper.getNumberOfQuestionnairesByDoctorId(doctorId).get("count");
    }

    public Page getQuestionnaires(QuestionnaireQueryDTO dto) {
        JSONArray jsonArray = questionnaireMapper.getQuestionnaires(dto.getDoctorId());
        List<Questionnaire> list = JSONUtil.toList(jsonArray, Questionnaire.class);
        SearchingUtil.searchingQuestionnaire(list, dto.getSearchInput());
        return PaginationUtil.pagination(list, dto.getPageNum(), dto.getPageSize());
    }

    public Questionnaire getQuestionnaireById(String questionnaireId) {
        JSONArray jsonArray = questionnaireMapper.getQuestionnaireById(questionnaireId);
        List<Questionnaire> list = JSONUtil.toList(jsonArray, Questionnaire.class);
        if(list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public List<User> check(String questionnaireId) {
        JSONArray jsonArray = questionnaireMapper.check(questionnaireId);
        return JSONUtil.toList(jsonArray, User.class);
    }

    public void insert(Questionnaire questionnaire) {
        String id;
        if(questionnaireMapper.getCount() == null) {
            questionnaireMapper.insertCount();
            id = "1";
        } else {
            id = questionnaireMapper.getCount();
        }
        questionnaireMapper.updateCount(String.valueOf(Integer.parseInt(id) + 1));
        questionnaire.setId(id);
        questionnaire.setCreateTime(new Date());
        questionnaireMapper.insert(questionnaire.getCreatorId(),
                JSONUtil.toJsonStr(JSONUtil.createObj().putOpt("name", "questionnaire_" + id)),
                id, JSONUtil.toJsonStr(questionnaire));
    }

    public void assign(String questionnaireId, List<String> patientIdList) {
        for(String id : patientIdList) {
            User patient = getPatientById(id);
            if(patient != null) {
                patient.setQuestionnaire(questionnaireId);
                JSONObject jsonObject = JSONUtil.parseObj(patient);
                jsonObject.putOpt("label", "User");
                jsonObject.putOpt("name", "user_" + patient.getId());
                questionnaireMapper.assign(JSONUtil.toJsonStr(jsonObject), id);
            }
        }
    }

    public void update(Questionnaire questionnaire) {
        JSONObject jsonObject = JSONUtil.parseObj(questionnaire);
        jsonObject.putOpt("label", "Questionnaire");
        jsonObject.putOpt("name", "questionnaire_" + questionnaire.getId());
        questionnaireMapper.update(questionnaire.getId(), JSONUtil.toJsonStr(jsonObject));
    }

    public void delete(String questionnaireId) {
        JSONArray jsonArray = questionnaireMapper.check(questionnaireId);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        for(User user : list) {
            user.setQuestionnaire("");
            JSONObject jsonObject = JSONUtil.parseObj(user);
            jsonObject.putOpt("label", "User");
            jsonObject.putOpt("name", "user_" + user.getId());
            userMapper.update(user.getId(), JSONUtil.toJsonStr(jsonObject));
        }
        questionnaireMapper.delete(questionnaireId);
    }

    private User getPatientById(String patientId) {
        JSONArray jsonArray = userMapper.getUserById(patientId);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        if(list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
}
