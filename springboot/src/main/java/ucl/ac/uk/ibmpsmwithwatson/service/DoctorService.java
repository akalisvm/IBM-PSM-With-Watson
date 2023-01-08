package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.User;
import ucl.ac.uk.ibmpsmwithwatson.util.PaginationUtil;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private UserMapper userMapper;

    public Page getPatients(String givenName, String familyName, Integer pageNum, Integer pageSize) {
        String fullName = givenName + "," + familyName;
        JSONArray array = (JSONArray) userMapper.getPatients(fullName).get("rows");
        List<User> list = JSONUtil.toList(array, User.class);
        return PaginationUtil.pagination(list, pageNum, pageSize, list.size());
    }
}
