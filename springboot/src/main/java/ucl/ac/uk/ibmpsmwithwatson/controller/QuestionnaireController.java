package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.service.QuestionnaireService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    @Autowired
    QuestionnaireService questionnaireService;

    @GetMapping
    public Result<?> query(@RequestParam(defaultValue = "") String id,
                           @RequestParam(defaultValue = "") String searchTitle,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "5") Integer pageSize) {
        Page page = questionnaireService.query(id, searchTitle, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> insert(@RequestBody Questionnaire questionnaire) {
        System.out.println("controller: " + questionnaire.toString());
        questionnaireService.insert(questionnaire);
        return Result.success();
    }

    @PutMapping
    public Result<?> update() {
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id) {
        questionnaireService.delete(id);
        return Result.success();
    }
}
