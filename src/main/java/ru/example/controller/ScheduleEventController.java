package ru.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.example.model.ScheduleEvent;
import ru.example.service.ScheduleEventService;
import java.util.List;

@RestController
@RequestMapping("/schedule-events")
public class ScheduleEventController {
    @Autowired
    private ScheduleEventService scheduleEventService;

    @PostMapping
    public ScheduleEvent createEvent(@RequestBody ScheduleEvent event) {
        return scheduleEventService.save(event);
    }

    @GetMapping
    public List<ScheduleEvent> getAllEvents() {
        return scheduleEventService.findAll();
    }
}
