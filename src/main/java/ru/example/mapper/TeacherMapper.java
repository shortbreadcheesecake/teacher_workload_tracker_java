package ru.example.mapper;

import org.springframework.stereotype.Component;
import ru.example.dto.TeacherDto;
import ru.example.model.Teacher;

@Component
public class TeacherMapper {
    
    public TeacherDto toDto(Teacher entity) {
        if (entity == null) {
            return null;
        }
        
        TeacherDto dto = new TeacherDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDepartment(entity.getDepartment());
        
        return dto;
    }
    
    public Teacher toEntity(TeacherDto dto) {
        if (dto == null) {
            return null;
        }
        
        Teacher entity = new Teacher();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDepartment(dto.getDepartment());
        
        return entity;
    }
}
