package ucl.ac.uk.ibmpsmwithwatson.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ucl.ac.uk.ibmpsmwithwatson.pojo.dto.EventQueryDTO;
import ucl.ac.uk.ibmpsmwithwatson.pojo.po.Event;
import ucl.ac.uk.ibmpsmwithwatson.service.EventService;
import ucl.ac.uk.ibmpsmwithwatson.util.Result;

import java.util.List;

@Api(tags = "Outreach Event")
@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/upcoming/{doctorId}")
    public Result<?> getUpcomingEvents(@PathVariable String doctorId) {
        return Result.success(eventService.getUpcomingEvents(doctorId));
    }

    @PostMapping("/get")
    public Result<?> getEvents(@RequestBody EventQueryDTO dto) {
        return Result.success(eventService.getEvents(dto));
    }

    @PostMapping
    public Result<?> insertEvent(@RequestBody Event event) {
        try {
            eventService.insertEvent(event);
        } catch (Exception e) {
            return Result.error("10002", e.getMessage());
        }
        return Result.success();
    }

    @PutMapping
    public Result<?> updateEvent(@RequestBody Event event) {
        try {
            eventService.updateEvent(event);
        } catch (Exception e) {
            return Result.error("10002", e.getMessage());
        }
        return Result.success();
    }

    @PostMapping("/delete/batch")
    public Result<?> deleteBatchEvents(@RequestBody List<String> eventIdList) {
        eventService.deleteBatchEvents(eventIdList);
        return Result.success();
    }
}
