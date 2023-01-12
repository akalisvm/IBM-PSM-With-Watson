package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Template;
import ucl.ac.uk.ibmpsmwithwatson.service.TemplateService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    TemplateService templateService;

    @GetMapping
    public Result<?> query(@RequestParam(defaultValue = "") String id,
                                   @RequestParam(defaultValue = "") String searchTitle,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "5") Integer pageSize) {
        Page templates = templateService.query(id, searchTitle, pageNum, pageSize);
        return Result.success(templates);
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

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable String id) {
        templateService.delete(id);
        return Result.success();
    }
}
