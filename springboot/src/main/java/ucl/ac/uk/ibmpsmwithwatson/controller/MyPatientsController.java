package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.service.MyPatientsService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
@RequestMapping("/my-patients")
public class MyPatientsController {

    @Autowired
    MyPatientsService myPatientsService;

    @GetMapping
    public Result<?> queryPatient(@RequestParam(defaultValue = "") String id,
                                     @RequestParam(defaultValue = "") String searchName,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Page page = myPatientsService.queryPatient(id, searchName, pageNum, pageSize);
        return Result.success(page);
    }
}
