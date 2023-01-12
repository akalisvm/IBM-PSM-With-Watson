package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.QuestionnaireMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Question;
import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;

import java.util.Date;
import java.util.List;

@Service
public class QuestionnaireService {

    @Autowired
    QuestionnaireMapper questionnaireMapper;

    public Page query(String id, String searchTitle, Integer pageNum, Integer pageSize) {
        JSONArray array = (JSONArray) questionnaireMapper.query(id).get("rows");
        List<Questionnaire> list = JSONUtil.toList(array, Questionnaire.class);
        if(!searchTitle.equals("")) {
            Questionnaire temp;
            for(int i = list.size() - 1; i >= 0; i--) {
                temp = list.get(i);
                if(!temp.getTitle().contains(searchTitle)) {
                    list.remove(temp);
                }
            }
        }
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }

    public void insert(Questionnaire questionnaire) {
        System.out.println("service: " + questionnaire.toString());
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
        Question question = new Question();
        question.setValue("Do you need a phone call with your doctor?");
        List<Question> list = questionnaire.getQuestions();
        list.add(question);
        questionnaire.setQuestions(list);
        String creatorId = questionnaire.getCreatorId();
        JSONObject jsonObject = JSONUtil.parseObj(questionnaire, false, true);
        jsonObject.setDateFormat("yyyy-MM-dd HH:mm:ss");
        questionnaireMapper.insert(creatorId,
                JSONUtil.toJsonStr(JSONUtil.createObj().putOpt("name", "questionnaire_" + id)),
                id, JSONUtil.toJsonStr(jsonObject));
    }

    public void delete(String id) {
        questionnaireMapper.delete(id);
    }
}
