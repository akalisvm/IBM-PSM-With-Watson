package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.entity.Page;
import ucl.ac.uk.ibmpsmwithwatson.entity.Record;
import ucl.ac.uk.ibmpsmwithwatson.service.RecordService;
import ucl.ac.uk.ibmpsmwithwatson.entity.Result;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    RecordService recordService;

    @GetMapping("/patient")
    public Result<?> getRecordsForPatient(@RequestParam(defaultValue = "") String patientId,
                                          @RequestParam(defaultValue = "") String searchInput,
                                          @RequestParam(defaultValue = "") String resultFilter,
                                          @RequestParam(defaultValue = "") String needMeetingFilter,
                                          @RequestParam(defaultValue = "1") Integer pageNum,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        Page page = recordService.getRecordsForPatient(
                patientId, searchInput, resultFilter, needMeetingFilter, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping("/doctor")
    public Result<?> getRecordsForDoctor(@RequestParam(defaultValue = "") String doctorId,
                                         @RequestParam(defaultValue = "") String searchInput,
                                         @RequestParam(defaultValue = "") String patientFilter,
                                         @RequestParam(defaultValue = "") String resultFilter,
                                         @RequestParam(defaultValue = "") String needMeetingFilter,
                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        Page page = recordService.getRecordsForDoctor(
                doctorId, searchInput, patientFilter, resultFilter, needMeetingFilter, pageNum, pageSize);
        return Result.success(page);
    }

    @PostMapping
    public Result<?> insert(@RequestBody Record record) {
        recordService.insert(record);
        return Result.success();
    }

    @DeleteMapping("/{recordId}")
    public Result<?> delete(@PathVariable String recordId) {
        recordService.delete(recordId);
        return Result.success();
    }
}
