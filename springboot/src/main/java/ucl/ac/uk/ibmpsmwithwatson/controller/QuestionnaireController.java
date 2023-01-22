package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.service.QuestionnaireService;
import ucl.ac.uk.ibmpsmwithwatson.entity.Result;

import java.util.List;

@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {

    @Autowired
    QuestionnaireService questionnaireService;

    @GetMapping
    public Result<?> getQuestionnaires(@RequestParam(defaultValue = "") String doctorId,
                                       @RequestParam(defaultValue = "") String searchInput,
                                       @RequestParam(defaultValue = "1") Integer pageNum,
                                       @RequestParam(defaultValue = "5") Integer pageSize) {
        Page page = questionnaireService.getQuestionnaires(doctorId, searchInput, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<?> getQuestionnaireById(@PathVariable String id) {
        return Result.success(questionnaireService.getQuestionnaireById(id));
    }

    @GetMapping("/check")
    public Result<?> check(@RequestParam(defaultValue = "") String questionnaireId) {
        return Result.success(questionnaireService.check(questionnaireId));
    }

    @PostMapping
    public Result<?> insert(@RequestBody Questionnaire questionnaire) {
        questionnaireService.insert(questionnaire);
        return Result.success();
    }

    @PostMapping("/assign/{questionnaireId}")
    public Result<?> assign(@PathVariable String questionnaireId, @RequestBody List<String> patientIdList) {
        questionnaireService.assign(questionnaireId, patientIdList);
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
