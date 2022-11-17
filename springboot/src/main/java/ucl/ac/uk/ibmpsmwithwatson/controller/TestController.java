package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucl.ac.uk.ibmpsmwithwatson.entity.Result;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public Result<?> test() {
        return Result.success();
    }

}
