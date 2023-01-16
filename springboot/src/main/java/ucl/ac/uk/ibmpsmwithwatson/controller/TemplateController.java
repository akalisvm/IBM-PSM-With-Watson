package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.service.TemplateService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
@RequestMapping("/templates")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @GetMapping
    public Result<?> query(@RequestParam(defaultValue = "") String creatorId,
                                   @RequestParam(defaultValue = "") String searchInput,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "5") Integer pageSize) {
        Page page = templateService.query(creatorId, searchInput, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> insert(@RequestBody Template template) {
        templateService.insert(template);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Template template) {
        templateService.update(template);
        return Result.success();
    }

    @DeleteMapping("/{templateId}")
    public Result<?> delete(@PathVariable String templateId) {
        templateService.delete(templateId);
        return Result.success();
    }
}
