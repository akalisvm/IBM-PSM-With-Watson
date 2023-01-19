package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.TemplateMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.SearchingUtil;

import java.util.Date;
import java.util.List;

@Service
public class TemplateService {

    @Autowired
    TemplateMapper templateMapper;

    public Page getTemplates(String doctorId, String searchInput, Integer pageNum, Integer pageSize) {
        JSONArray jsonArray = templateMapper.getTemplates(doctorId);
        List<Template> list = JSONUtil.toList(jsonArray, Template.class);
        SearchingUtil.searchingTemplateByIdOrTitle(list, searchInput);
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }

    public void insert(Template template) {
        String id;
        if(templateMapper.getCount() == null) {
            templateMapper.insertCount();
            id = "1";
        } else {
            id = templateMapper.getCount();
        }
        templateMapper.updateCount(String.valueOf(Integer.parseInt(id) + 1));
        template.setId(id);
        template.setCreateTime(new Date());
        String creatorId = template.getCreatorId();
        templateMapper.insert(creatorId, id, JSONUtil.toJsonStr(template));
    }

    public void update(Template template) {
        JSONObject jsonObject = JSONUtil.parseObj(template);
        jsonObject.putOpt("label", "Template");
        jsonObject.putOpt("name", "template_" + template.getId());
        templateMapper.update(template.getId(), JSONUtil.toJsonStr(jsonObject));
    }

    public void delete(String templateId) {
        templateMapper.delete(templateId);
    }
}
