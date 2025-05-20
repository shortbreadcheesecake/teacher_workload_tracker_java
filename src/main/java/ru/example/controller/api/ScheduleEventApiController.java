package ru.example.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.ScheduleEventDto;
import ru.example.mapper.ScheduleEventMapper;
import ru.example.model.ScheduleEvent;
import ru.example.service.ScheduleEventService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleEventApiController {
    
    @Autowired
    private ScheduleEventService scheduleEventService;
    
    @Autowired
    private ScheduleEventMapper scheduleEventMapper;
    
    @GetMapping
    public ResponseEntity<List<ScheduleEventDto>> getAllScheduleEvents() {
        List<ScheduleEvent> events = scheduleEventService.findAll();
        List<ScheduleEventDto> eventDtos = events.stream()
                .map(scheduleEventMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleEventDto> getScheduleEventById(@PathVariable Long id) {
        return scheduleEventService.findById(id)
                .map(scheduleEventMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ScheduleEventDto> createScheduleEvent(@Valid @RequestBody ScheduleEventDto eventDto) {
        ScheduleEvent event = scheduleEventMapper.toEntity(eventDto);
        
        // Если дата установлена, вычисляем день недели
        if (event.getDate() != null) {
            event.setDayOfWeek(event.getDate().getDayOfWeek());
        }
        
        ScheduleEvent savedEvent = scheduleEventService.save(event);
        return new ResponseEntity<>(scheduleEventMapper.toDto(savedEvent), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleEventDto> updateScheduleEvent(@PathVariable Long id, @Valid @RequestBody ScheduleEventDto eventDto) {
        if (!scheduleEventService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        ScheduleEvent event = scheduleEventMapper.toEntity(eventDto);
        event.setId(id);
        
        // Если дата установлена, вычисляем день недели
        if (event.getDate() != null) {
            event.setDayOfWeek(event.getDate().getDayOfWeek());
        }
        
        ScheduleEvent updatedEvent = scheduleEventService.save(event);
        
        return ResponseEntity.ok(scheduleEventMapper.toDto(updatedEvent));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleEvent(@PathVariable Long id) {
        if (!scheduleEventService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        scheduleEventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeScheduleEvent(@PathVariable Long id) {
        try {
            scheduleEventService.markAsCompleted(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelScheduleEvent(@PathVariable Long id) {
        try {
            scheduleEventService.markAsCancelled(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
