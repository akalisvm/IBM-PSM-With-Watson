package ucl.ac.uk.ibmpsmwithwatson.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.EventQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Event;
import ucl.ac.uk.ibmpsmwithwatson.service.EventService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import java.util.List;

@Api(tags = "Event")
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/dashboard/{doctorId}")
    public Result<?> getUpcomingEvents(@PathVariable String doctorId) {
        return Result.success(eventService.getUpcomingEvents(doctorId));
    }

    @PostMapping("/get")
    public Result<?> getEvents(@RequestBody EventQueryDTO dto) {
        return Result.success(eventService.getEvents(dto));
    }

    @PostMapping
    public Result<?> insert(@RequestBody Event event) {
        try {
            eventService.insert(event);
        } catch (Exception e) {
            return Result.error("10002", e.getMessage());
        }
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Event event) {
        try {
            eventService.update(event);
        } catch (Exception e) {
            return Result.error("10002", e.getMessage());
        }
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result<?> deleteBatch(@RequestBody List<String> eventIdList) {
        eventService.deleteBatch(eventIdList);
        return Result.success();
    }
}
