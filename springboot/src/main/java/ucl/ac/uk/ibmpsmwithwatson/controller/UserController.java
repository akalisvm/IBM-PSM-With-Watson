package ucl.ac.uk.ibmpsmwithwatson.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.UserQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.util.Page;
import ucl.ac.uk.ibmpsmwithwatson.service.UserService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

@Api(tags = "User")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public Result<?> getUserById(@PathVariable String userId) {
        return Result.success(userService.getUserById(userId));
    }

    @GetMapping("/number/{doctorId}")
    public Result<?> getNumberOfPatientsByDoctorId(@PathVariable String doctorId) {
        return Result.success(userService.getNumberOfPatientsByDoctorId(doctorId));
    }

    @PostMapping("/get")
    public Result<?> getPatientsByDoctorId(@RequestBody UserQueryDTO dto) {
        Page page = userService.getPatientsByDoctorId(dto);
        return Result.success(page);
    }
}
