package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.QuestionnaireMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
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

    public void delete(String questionnaireId) {
        questionnaireMapper.delete(questionnaireId);
    }
}
