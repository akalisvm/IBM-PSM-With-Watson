package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.UserQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.Page;
import ucl.ac.uk.ibmpsmwithwatson.service.UserService;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.Result;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/{id}")
    public Result<?> getUserById(@PathVariable String id) {
        return Result.success(userService.getUserById(id));
    }

    @PostMapping("/patients")
    public Result<?> getPatientsByDoctorId(@RequestBody UserQueryDTO dto) {
        Page page = userService.getPatientsByDoctorId(dto);
        return Result.success(page);
    }
}
