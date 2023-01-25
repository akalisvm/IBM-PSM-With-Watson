package ucl.ac.uk.ibmpsmwithwatson.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.EventQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Event;
import ucl.ac.uk.ibmpsmwithwatson.service.EventService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;

    @PostMapping("/get")
    public Result<?> get(@RequestBody EventQueryDTO dto) {
        return Result.success(eventService.getEvents(dto));
    }

    @PostMapping
    public Result<?> insert(@RequestBody Event event) {
        eventService.insert(event);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Event event) {
        eventService.update(event);
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result<?> deleteBatch(@RequestBody List<String> eventIdList) {
        eventService.deleteBatch(eventIdList);
        return Result.success();
    }
}
