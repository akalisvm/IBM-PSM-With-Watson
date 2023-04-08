package ucl.ac.uk.ibmpsmwithwatson.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.RecordQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.util.Page;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Record;
import ucl.ac.uk.ibmpsmwithwatson.service.RecordService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import java.util.List;

@Api(tags = "Healthcare Record")
@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    RecordService recordService;

    @GetMapping("/history/xdata")
    public Result<?> getHistoryXData() {
        return Result.success(recordService.getHistoryXData());
    }

    @GetMapping("/history/ydata/{doctorId}")
    public Result<?> getHistoryYData(@PathVariable String doctorId) {
        return Result.success(recordService.getHistoryYData(doctorId));
    }

    @PostMapping("/get")
    public Result<?> getRecords(@RequestBody RecordQueryDTO dto) {
        Page page = recordService.getRecords(dto);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> insertRecord(@RequestBody Record record) {
        recordService.insertRecord(record);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result<?> deleteBatchRecords(@RequestBody List<String> recordIdList) {
        recordService.deleteBatchRecords(recordIdList);
        return Result.success();
    }
}
