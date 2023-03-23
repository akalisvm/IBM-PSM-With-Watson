package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Dialog;
import ucl.ac.uk.ibmpsmwithwatson.service.DialogService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
@RequestMapping("/dialog")
public class DialogController {

    @Autowired
    DialogService dialogService;

    @GetMapping("/{patientId}")
    public Result<?> getDialog(@PathVariable String patientId) {
        Dialog dialog = dialogService.getDialog(patientId);
        return dialog == null ? Result.success("null") : Result.success(dialog);
    }
}
