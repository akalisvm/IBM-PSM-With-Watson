package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;
import ucl.ac.uk.ibmpsmwithwatson.util.StringUtil;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private UserMapper userMapper;

    public Page queryPatients(String id, String searchName, Integer pageNum, Integer pageSize) {
        JSONArray array = (JSONArray) userMapper.queryPatients(id).get("rows");
        List<User> list = JSONUtil.toList(array, User.class);
        if(!searchName.equals("")) {
            if(searchName.matches("[a-zA-Z]+")) {
                searchName = StringUtil.firstUpperCase(searchName);
                User temp;
                for(int i = list.size() - 1; i >= 0; i--) {
                    temp = list.get(i);
                    if(!temp.getGiven_name().contains(searchName)
                            && !temp.getFamily_name().contains(searchName)) {
                        list.remove(temp);
                    }
                }
            } else {
                list.clear();
            }
        }
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }
}
