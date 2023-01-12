package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.QuestionnaireMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.StringUtil;

import java.util.Date;
import java.util.List;

@Service
public class QuestionnaireService {

    private static final String TEMPLATE = "Template";
    private static final String QUESTIONNAIRE = "Questionnaire";

    @Autowired
    QuestionnaireMapper questionnaireMapper;

    public void insertTemplate(Template template) {
        String id;
        if(questionnaireMapper.queryCount(TEMPLATE) == null) {
            questionnaireMapper.insertCount(TEMPLATE);
            id = "1";
        } else {
            id = questionnaireMapper.queryCount(TEMPLATE);
        }
        questionnaireMapper.updateCount(TEMPLATE, String.valueOf(Integer.parseInt(id) + 1));
        template.setId(id);
        template.setCreateTime(new Date());
        String creatorId = template.getCreatorId();
        JSONObject jsonObject = JSONUtil.parseObj(template, false, true);
        jsonObject.setDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(JSONUtil.toJsonStr(jsonObject));
        questionnaireMapper.insertTemplate(creatorId, id, JSONUtil.toJsonStr(jsonObject));
    }

    public Page queryTemplates(String id, String searchTitle, Integer pageNum, Integer pageSize) {
        JSONArray array = (JSONArray) questionnaireMapper.queryTemplates(id).get("rows");
        List<Template> list = JSONUtil.toList(array, Template.class);
        if(!searchTitle.equals("")) {
            Template temp;
            for(int i = list.size() - 1; i >= 0; i--) {
                temp = list.get(i);
                if(!temp.getTitle().contains(searchTitle)) {
                    list.remove(temp);
                }
            }
        }
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }
}
