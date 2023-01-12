package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.MyPatientsMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;

import java.util.List;

@Service
public class MyPatientsService {

    @Autowired
    private MyPatientsMapper myPatientsMapper;

    public Page queryPatient(String id, String searchName, Integer pageNum, Integer pageSize) {
        JSONArray array = (JSONArray) myPatientsMapper.queryPatient(id).get("rows");
        List<User> list = JSONUtil.toList(array, User.class);
        if(!searchName.equals("")) {
            if(searchName.matches("[a-zA-Z]+")) {
                searchName = searchName.toLowerCase();
                for(int i = list.size() - 1; i >= 0; i--) {
                    User temp = list.get(i);
                    String givenName = temp.getGiven_name().toLowerCase();
                    String familyName = temp.getFamily_name().toLowerCase();
                    if(!givenName.contains(searchName) && !familyName.contains(searchName)) {
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
