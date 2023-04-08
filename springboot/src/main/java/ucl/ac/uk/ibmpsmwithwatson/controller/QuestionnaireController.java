package ucl.ac.uk.ibmpsmwithwatson.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.QuestionnaireQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.util.Page;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Questionnaire;
import ucl.ac.uk.ibmpsmwithwatson.service.QuestionnaireService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import java.util.List;

@Api(tags = "Healthcare Questionnaire")
@RestController
@RequestMapping("/questionnaires")
public class QuestionnaireController {

    @Autowired
    QuestionnaireService questionnaireService;

    @GetMapping("/number/{doctorId}")
    public Result<?> getNumberOfQuestionnairesByDoctorId(@PathVariable String doctorId) {
        return Result.success(questionnaireService.getNumberOfQuestionnairesByDoctorId(doctorId));
    }

    @PostMapping("/get")
    public Result<?> getQuestionnaires(@RequestBody QuestionnaireQueryDTO dto) {
        Page page = questionnaireService.getQuestionnaires(dto);
        return Result.success(page);
    }

    @GetMapping("/{questionnaireId}")
    public Result<?> getQuestionnaireById(@PathVariable String questionnaireId) {
        return Result.success(questionnaireService.getQuestionnaireById(questionnaireId));
    }

    @GetMapping("/check/{questionnaireId}")
    public Result<?> checkQuestionnaire(@PathVariable String questionnaireId) {
        return Result.success(questionnaireService.checkQuestionnaire(questionnaireId));
    }

    @PostMapping
    public Result<?> insertQuestionnaire(@RequestBody Questionnaire questionnaire) {
        questionnaireService.insertQuestionnaire(questionnaire);
        return Result.success();
    }

    @PostMapping("/assign/{questionnaireId}")
    public Result<?> assignQuestionnaire(@PathVariable String questionnaireId, @RequestBody List<String> patientIdList) {
        questionnaireService.assignQuestionnaire(questionnaireId, patientIdList);
        return Result.success();
    }

    @PutMapping
    public Result<?> updateQuestionnaire(@RequestBody Questionnaire questionnaire) {
        questionnaireService.updateQuestionnaire(questionnaire);
        return Result.success();
    }

    @DeleteMapping("/{questionnaireId}")
    public Result<?> deleteQuestionnaire(@PathVariable String questionnaireId) {
        questionnaireService.deleteQuestionnaire(questionnaireId);
        return Result.success();
    }
}
