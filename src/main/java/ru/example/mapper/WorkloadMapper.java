package ru.example.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.example.dto.WorkloadDto;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.service.TeacherService;

@Component
public class WorkloadMapper {
    
    @Autowired
    private TeacherService teacherService;
    
    public WorkloadDto toDto(Workload entity) {
        if (entity == null) {
            return null;
        }
        
        WorkloadDto dto = new WorkloadDto();
        dto.setId(entity.getId());
        dto.setTeacherId(entity.getTeacher() != null ? entity.getTeacher().getId() : null);
        dto.setPosition(entity.getPosition());
        dto.setSubject(entity.getSubject());
        dto.setLectureHours(entity.getLectureHours());
        dto.setSeminarHours(entity.getSeminarHours());
        dto.setLabHours(entity.getLabHours());
        dto.setCourseWorkHours(entity.getCourseWorkHours());
        dto.setExamHours(entity.getExamHours());
        dto.setTotalHours(entity.getTotalHours());
        dto.setAdditionalDuties(entity.getAdditionalDuties());
        dto.setSemester(entity.getSemester());
        dto.setAcademicYear(entity.getAcademicYear());
        dto.setStudentGroup(entity.getStudentGroup());
        
        return dto;
    }
    
    public Workload toEntity(WorkloadDto dto) {
        if (dto == null) {
            return null;
        }
        
        Workload entity = new Workload();
        entity.setId(dto.getId());
        
        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherService.findById(dto.getTeacherId()).orElse(null);
            entity.setTeacher(teacher);
        }
        
        entity.setPosition(dto.getPosition());
        entity.setSubject(dto.getSubject());
        entity.setLectureHours(dto.getLectureHours());
        entity.setSeminarHours(dto.getSeminarHours());
        entity.setLabHours(dto.getLabHours());
        entity.setCourseWorkHours(dto.getCourseWorkHours());
        entity.setExamHours(dto.getExamHours());
        entity.setTotalHours(dto.getTotalHours());
        entity.setAdditionalDuties(dto.getAdditionalDuties());
        entity.setSemester(dto.getSemester());
        entity.setAcademicYear(dto.getAcademicYear());
        entity.setStudentGroup(dto.getStudentGroup());
        
        return entity;
    }
}
