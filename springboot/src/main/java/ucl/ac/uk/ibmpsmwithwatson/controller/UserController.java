package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.service.UserService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/patients")
    public Result<?> getPatientsByDoctorId(@RequestParam(defaultValue = "") String doctorId,
                                           @RequestParam(defaultValue = "") String searchInput,
                                           @RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        Page page = userService.getPatientsByDoctorId(doctorId, searchInput, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/user/{id}")
    public Result<?> getUserById(@PathVariable String id) {
        return Result.success(userService.getUserById(id));
    }
}
