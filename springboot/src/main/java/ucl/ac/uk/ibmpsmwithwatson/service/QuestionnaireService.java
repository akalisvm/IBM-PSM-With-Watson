package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.QuestionnaireMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.Date;
import java.util.List;

@Service
public class QuestionnaireService {

    @Autowired
    QuestionnaireMapper questionnaireMapper;

    public Page query(String creatorId, String searchInput, Integer pageNum, Integer pageSize) {
        JSONArray jsonArray = (JSONArray) questionnaireMapper.query(creatorId).get("rows");
        List<Questionnaire> list = JSONUtil.toList(jsonArray, Questionnaire.class);
        SearchingUtil.searchingQuestionnaireByIdOrTitle(list, searchInput);
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }

    public void insert(Questionnaire questionnaire) {
        String id;
        if(questionnaireMapper.queryCount() == null) {
            questionnaireMapper.insertCount();
            id = "1";
        } else {
            id = questionnaireMapper.queryCount();
        }
        questionnaireMapper.updateCount(String.valueOf(Integer.parseInt(id) + 1));
        questionnaire.setId(id);
        questionnaire.setCreateTime(new Date());
        String creatorId = questionnaire.getCreatorId();
        questionnaireMapper.insert(creatorId,
                JSONUtil.toJsonStr(JSONUtil.createObj().putOpt("name", "questionnaire_" + id)),
                id, JSONUtil.toJsonStr(questionnaire));
    }

    public void update(Questionnaire questionnaire) {
        JSONObject jsonObject = JSONUtil.parseObj(questionnaire);
        jsonObject.putOpt("label", "Questionnaire");
        jsonObject.putOpt("name", "questionnaire_" + questionnaire.getId());
        questionnaireMapper.update(questionnaire.getId(), JSONUtil.toJsonStr(jsonObject));
    }

    public List<User> check(String questionnaireId) {
        JSONArray jsonArray = (JSONArray) questionnaireMapper.check(questionnaireId).get("rows");
        return JSONUtil.toList(jsonArray, User.class);
    }

    public void delete(String questionnaireId) {
        JSONArray jsonArray = (JSONArray) questionnaireMapper.check(questionnaireId).get("rows");
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        for(User user : list) {
            user.setQuestionnaire("");
            JSONObject jsonObject = JSONUtil.parseObj(user);
            jsonObject.putOpt("label", "User");
            jsonObject.putOpt("name", "user_" + user.getId());
            questionnaireMapper.clear(user.getId(), JSONUtil.toJsonStr(jsonObject));
        }
        questionnaireMapper.delete(questionnaireId);
    }
}
