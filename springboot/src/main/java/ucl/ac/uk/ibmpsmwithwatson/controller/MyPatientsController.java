package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.service.MyPatientsService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import java.util.List;

@RestController
@RequestMapping("/mypatients")
public class MyPatientsController {

    @Autowired
    MyPatientsService myPatientsService;

    @GetMapping
    public Result<?> queryPatient(@RequestParam(defaultValue = "") String id,
                                     @RequestParam(defaultValue = "") String searchInput,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Page page = myPatientsService.queryPatient(id, searchInput, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/questionnaires")
    public Result<?> queryQuestionnaire(@RequestParam(defaultValue = "") String id) {
        return Result.success(myPatientsService.queryQuestionnaire(id));
    }

    @PostMapping("/assign/{questionnaireId}")
    public Result<?> assignQuestionnaire(@PathVariable String questionnaireId,
                                         @RequestBody List<String> ids) {
        myPatientsService.assignQuestionnaire(questionnaireId, ids);
        return Result.success();
    }
}
