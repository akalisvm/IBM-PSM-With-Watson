package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.service.QuestionnaireService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    @Autowired
    QuestionnaireService questionnaireService;

    @GetMapping
    public Result<?> query(@RequestParam(defaultValue = "") String creatorId,
                           @RequestParam(defaultValue = "") String searchTitle,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "5") Integer pageSize) {
        Page page = questionnaireService.query(creatorId, searchTitle, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> insert(@RequestBody Questionnaire questionnaire) {
        questionnaireService.insert(questionnaire);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Questionnaire questionnaire) {
        questionnaireService.update(questionnaire);
        return Result.success();
    }

    @DeleteMapping("/{questionnaireId}")
    public Result<?> delete(@PathVariable String questionnaireId) {
        questionnaireService.delete(questionnaireId);
        return Result.success();
    }
}
