package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.service.QuestionnaireService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    @Autowired
    QuestionnaireService questionnaireService;

    @PostMapping
    public Result<?> insertQuestionnaire(@RequestBody Template template) {
        questionnaireService.insertTemplate(template);
        return Result.success(template);
    }

    @PutMapping
    public Result<?> updateQuestionnaire() {
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteQuestionnaire(@PathVariable String id) {
        return Result.success();
    }

    @GetMapping("/template")
    public Result<?> queryTemplates(@RequestParam(defaultValue = "") String id,
                                   @RequestParam(defaultValue = "") String searchTitle,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "5") Integer pageSize) {
        Page templates = questionnaireService.queryTemplates(id, searchTitle, pageNum, pageSize);
        return Result.success(templates);
    }

    @PostMapping("/template")
    public Result<?> insertTemplate(@RequestBody Template template) {
        questionnaireService.insertTemplate(template);
        return Result.success(template);
    }

    @PutMapping("/template")
    public Result<?> updateTemplate() {
        return Result.success();
    }

    @DeleteMapping("/template/{id}")
    public Result<?> deleteTemplate(@PathVariable String id) {
        return Result.success();
    }
}
