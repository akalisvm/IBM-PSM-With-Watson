package ucl.ac.uk.ibmpsmwithwatson.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucl.ac.uk.ibmpsmwithwatson.dao.DialogMapper;
import ucl.ac.uk.ibmpsmwithwatson.dao.UserMapper;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Dialog;
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

    public void insertDialog(String patientId, List<Message> messages) {
        String id;
        if(dialogMapper.getDialogCount() == null) {
            dialogMapper.insertDialogCount();
            id = "1";
        } else {
            id = dialogMapper.getDialogCount();
        }
        dialogMapper.updateDialogCount(String.valueOf(Integer.parseInt(id) + 1));
        Dialog dialog = new Dialog();
        dialog.setId(id);
        dialog.setCreateTime(new Date());
        dialog.setCreatorId(patientId);
        dialog.setMessages(messages);
        dialogMapper.insertDialog(patientId, id, JSONUtil.toJsonStr(dialog));
    }

    public void updateDialog(Dialog dialog) {
        JSONObject jsonObject = JSONUtil.parseObj(dialog);
        jsonObject.putOpt("label", "Dialog");
        jsonObject.putOpt("name", "dialog_" + dialog.getId());
        dialogMapper.updateDialog(dialog.getId(), JSONUtil.toJsonStr(jsonObject));
    }
}
