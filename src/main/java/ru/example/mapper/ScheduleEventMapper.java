package ru.example.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.example.dto.ScheduleEventDto;
import ru.example.model.ScheduleEvent;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.service.TeacherService;
import ru.example.service.WorkloadService;

@Component
public class ScheduleEventMapper {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private WorkloadService workloadService;
    
    public ScheduleEventDto toDto(ScheduleEvent entity) {
        if (entity == null) {
            return null;
        }
        
        ScheduleEventDto dto = new ScheduleEventDto();
        dto.setId(entity.getId());
        dto.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        dto.setWorkloadId(entity.getWorkload() != null ? entity.getWorkload().getId() : null);
        dto.setDate(entity.getDate());
        dto.setDayOfWeek(entity.getRussianDayOfWeek());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setSubject(entity.getSubject());
        dto.setStudentGroup(entity.getStudentGroup());
        dto.setLocation(entity.getLocation());
        dto.setLessonType(entity.getLessonType());
        dto.setHours(entity.getHours());
        dto.setCompleted(entity.getCompleted());
        
        return dto;
    }
    
    public ScheduleEvent toEntity(ScheduleEventDto dto) {
        if (dto == null) {
            return null;
        }
        
        ScheduleEvent entity = new ScheduleEvent();
        entity.setId(dto.getId());
        
        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherService.findById(dto.getTeacherId()).orElse(null);
            entity.setTeacher(teacher);
        }
        
        if (dto.getWorkloadId() != null) {
            Workload workload = workloadService.findById(dto.getWorkloadId()).orElse(null);
            entity.setWorkload(workload);
        }
        
        entity.setDate(dto.getDate());
        if (dto.getDate() != null) {
            entity.setDayOfWeek(dto.getDate().getDayOfWeek());
        }
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setSubject(dto.getSubject());
        entity.setStudentGroup(dto.getStudentGroup());
        entity.setLocation(dto.getLocation());
        entity.setLessonType(dto.getLessonType());
        entity.setHours(dto.getHours());
        entity.setCompleted(dto.getCompleted());
        
        return entity;
    }
}
