package ru.example.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.example.dto.ScheduleEventDto;
import ru.example.dto.TeacherDto;
import ru.example.dto.WorkloadDto;
import ru.example.mapper.ScheduleEventMapper;
import ru.example.mapper.TeacherMapper;
import ru.example.mapper.WorkloadMapper;
import ru.example.model.Teacher;
import ru.example.service.ScheduleEventService;
import ru.example.service.TeacherService;
import ru.example.service.WorkloadService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teachers")
public class TeacherApiController {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private WorkloadService workloadService;
    
    @Autowired
    private ScheduleEventService scheduleEventService;
    
    @Autowired
    private TeacherMapper teacherMapper;
    
    @Autowired
    private WorkloadMapper workloadMapper;
    
    @Autowired
    private ScheduleEventMapper scheduleEventMapper;
    
    @GetMapping
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        List<Teacher> teachers = teacherService.findAll();
        List<TeacherDto> teacherDtos = teachers.stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(teacherDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id) {
        return teacherService.findById(id)
                .map(teacherMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        Teacher savedTeacher = teacherService.save(teacher);
        return new ResponseEntity<>(teacherMapper.toDto(savedTeacher), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto teacherDto) {
        if (!teacherService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        teacher.setId(id);
        Teacher updatedTeacher = teacherService.save(teacher);
        
        return ResponseEntity.ok(teacherMapper.toDto(updatedTeacher));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        if (!teacherService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}/workloads")
    public ResponseEntity<List<WorkloadDto>> getTeacherWorkloads(@PathVariable Long id) {
        if (!teacherService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(
            workloadService.findAllByTeacherId(id).stream()
                .map(workloadMapper::toDto)
                .collect(Collectors.toList())
        );
    }
    
    @GetMapping("/{id}/schedule")
    public ResponseEntity<List<ScheduleEventDto>> getTeacherSchedule(@PathVariable Long id) {
        if (!teacherService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(
            scheduleEventService.findAllByTeacherId(id).stream()
                .map(scheduleEventMapper::toDto)
                .collect(Collectors.toList())
        );
    }
}
