package ru.example.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.dto.TeacherDto;
import ru.example.mapper.TeacherMapper;
import ru.example.model.Teacher;
import ru.example.service.TeacherService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Test
    public void getAllTeachers_ShouldReturnListOfTeachers() throws Exception {
        // Подготовка данных
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setName("Иванов И.И.");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setName("Петров П.П.");

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setName("Иванов И.И.");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setName("Петров П.П.");

        // Настройка моков
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teacher1)).thenReturn(teacherDto1);
        when(teacherMapper.toDto(teacher2)).thenReturn(teacherDto2);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Иванов И.И."))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Петров П.П."));
    }

    @Test
    public void getTeacherById_WithExistingId_ShouldReturnTeacher() throws Exception {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setName("Иванов И.И.");

        // Настройка моков
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иванов И.И."));
    }

    @Test
    public void getTeacherById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Настройка моков
        when(teacherService.findById(anyLong())).thenReturn(Optional.empty());

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/teachers/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void deleteTeacher_ShouldDeleteTeacherAndReturnNoContent() throws Exception {
        // Подготовка данных
        Long teacherId = 1L;
        
        // Настройка моков
        when(teacherService.findById(teacherId)).thenReturn(Optional.of(new Teacher()));
        
        // Выполнение запроса и проверка результата
        mockMvc.perform(delete("/api/teachers/" + teacherId))
                .andExpect(status().isNoContent());
    }
}
