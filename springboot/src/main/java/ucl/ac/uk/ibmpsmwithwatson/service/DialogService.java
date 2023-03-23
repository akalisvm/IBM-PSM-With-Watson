package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.DialogMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Dialog;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.User;
import ucl.ac.uk.ibmpsmwithwatson.util.Message;

import java.util.Date;
import java.util.List;

@Service
public class DialogService {

    @Autowired
    DialogMapper dialogMapper;

    @Autowired
    UserMapper userMapper;

    public Dialog getDialog(String patientId) {
        List<Dialog> dialogList = JSONUtil.toList(dialogMapper.getDialog(patientId), Dialog.class);
        return dialogList.size() == 1 ? dialogList.get(0) : null;
    }

    public void insert(String patientId, List<Message> messages) {
        String id;
        if(dialogMapper.getCount() == null) {
            dialogMapper.insertCount();
            id = "1";
        } else {
            id = dialogMapper.getCount();
        }
        dialogMapper.updateCount(String.valueOf(Integer.parseInt(id) + 1));
        Dialog dialog = new Dialog();
        dialog.setId(id);
        dialog.setCreateTime(new Date());
        dialog.setCreatorId(patientId);
        dialog.setMessages(messages);
        dialogMapper.insert(patientId, id, JSONUtil.toJsonStr(dialog));
    }

    public void update(Dialog dialog) {
        JSONObject jsonObject = JSONUtil.parseObj(dialog);
        jsonObject.putOpt("label", "Dialog");
        jsonObject.putOpt("name", "dialog_" + dialog.getId());
        dialogMapper.update(dialog.getId(), JSONUtil.toJsonStr(jsonObject));
    }

    public User getUserByEmail(String email) {
        JSONArray jsonArray = userMapper.getUserByEmail(email);
        List<User> list = JSONUtil.toList(jsonArray, User.class);
        return list.size() == 0 ? null : list.get(0);
    }
}
