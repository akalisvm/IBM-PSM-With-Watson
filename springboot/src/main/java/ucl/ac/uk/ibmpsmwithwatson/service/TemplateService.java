package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.TemplateMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;

import java.util.Date;
import java.util.List;

@Service
public class TemplateService {

    @Autowired
    TemplateMapper templateMapper;

    public Page query(String id, String searchTitle, Integer pageNum, Integer pageSize) {
        JSONArray array = (JSONArray) templateMapper.query(id).get("rows");
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

    public void insert(Template template) {
        String id;
        if(templateMapper.queryCount() == null) {
            templateMapper.insertCount();
            id = "1";
        } else {
            id = templateMapper.queryCount();
        }
        templateMapper.updateCount(String.valueOf(Integer.parseInt(id) + 1));
        template.setId(id);
        template.setCreateTime(new Date());
        String creatorId = template.getCreatorId();
        JSONObject jsonObject = JSONUtil.parseObj(template, false, true);
        jsonObject.setDateFormat("yyyy-MM-dd HH:mm:ss");
        templateMapper.insert(creatorId, id, JSONUtil.toJsonStr(jsonObject));
    }

    public void update(Template template) {
        templateMapper.update(template.getId(), JSONUtil.toJsonStr(template));
    }

    public void delete(String id) {
        templateMapper.delete(id);
    }
}
