package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.RecordQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.Page;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Record;
import ucl.ac.uk.ibmpsmwithwatson.service.RecordService;
import ucl.ac.uk.ibmpsmwithwatson.pojo.vo.Result;

import java.util.List;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    RecordService recordService;

    @PostMapping("/get")
    public Result<?> getRecords(@RequestBody RecordQueryDTO dto) {
        Page page = recordService.getRecords(dto);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> insert(@RequestBody Record record) {
        recordService.insert(record);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result<?> deleteBatch(@RequestBody List<String> recordIdList) {
        recordService.deleteBatch(recordIdList);
        return Result.success();
    }
}
