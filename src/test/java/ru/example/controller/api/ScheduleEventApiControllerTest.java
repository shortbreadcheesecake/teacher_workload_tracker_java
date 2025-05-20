package ru.example.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.dto.ScheduleEventDto;
import ru.example.mapper.ScheduleEventMapper;
import ru.example.model.ScheduleEvent;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.service.ScheduleEventService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any; // Не используется
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleEventApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleEventService scheduleEventService;

    @MockBean
    private ScheduleEventMapper scheduleEventMapper;

    @Test
    public void getAllScheduleEvents_ShouldReturnListOfEvents() throws Exception {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        Workload workload = new Workload();
        workload.setId(1L);
        workload.setTeacher(teacher);
        workload.setSubject("Математика");
        workload.setStudentGroup("ИС-101");

        ScheduleEvent event1 = new ScheduleEvent();
        event1.setId(1L);
        event1.setTeacher(teacher);
        event1.setWorkload(workload);
        event1.setSubject("Математика");
        event1.setStudentGroup("ИС-101");
        event1.setDate(LocalDate.of(2025, 5, 15));
        event1.setStartTime(LocalTime.of(10, 0));
        event1.setEndTime(LocalTime.of(11, 30));
        event1.setLocation("Аудитория 305");
        event1.setLessonType("Лекция");
        event1.setCompleted(false);

        ScheduleEvent event2 = new ScheduleEvent();
        event2.setId(2L);
        event2.setTeacher(teacher);
        event2.setWorkload(workload);
        event2.setSubject("Математика");
        event2.setStudentGroup("ИС-101");
        event2.setDate(LocalDate.of(2025, 5, 17));
        event2.setStartTime(LocalTime.of(12, 0));
        event2.setEndTime(LocalTime.of(13, 30));
        event2.setLocation("Аудитория 306");
        event2.setLessonType("Семинар");
        event2.setCompleted(false);

        List<ScheduleEvent> events = Arrays.asList(event1, event2);

        ScheduleEventDto eventDto1 = new ScheduleEventDto();
        eventDto1.setId(1L);
        eventDto1.setTeacherId(1L);
        eventDto1.setWorkloadId(1L);
        eventDto1.setSubject("Математика");
        eventDto1.setStudentGroup("ИС-101");
        eventDto1.setDate(LocalDate.of(2025, 5, 15));
        eventDto1.setStartTime(LocalTime.of(10, 0));
        eventDto1.setEndTime(LocalTime.of(11, 30));
        eventDto1.setLocation("Аудитория 305");
        eventDto1.setLessonType("Лекция");
        eventDto1.setCompleted(false);

        ScheduleEventDto eventDto2 = new ScheduleEventDto();
        eventDto2.setId(2L);
        eventDto2.setTeacherId(1L);
        eventDto2.setWorkloadId(1L);
        eventDto2.setSubject("Математика");
        eventDto2.setStudentGroup("ИС-101");
        eventDto2.setDate(LocalDate.of(2025, 5, 17));
        eventDto2.setStartTime(LocalTime.of(12, 0));
        eventDto2.setEndTime(LocalTime.of(13, 30));
        eventDto2.setLocation("Аудитория 306");
        eventDto2.setLessonType("Семинар");
        eventDto2.setCompleted(false);

        // Настройка моков
        when(scheduleEventService.findAll()).thenReturn(events);
        when(scheduleEventMapper.toDto(event1)).thenReturn(eventDto1);
        when(scheduleEventMapper.toDto(event2)).thenReturn(eventDto2);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/schedule"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subject").value("Математика"))
                .andExpect(jsonPath("$[0].location").value("Аудитория 305"))
                .andExpect(jsonPath("$[0].lessonType").value("Лекция"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].subject").value("Математика"))
                .andExpect(jsonPath("$[1].location").value("Аудитория 306"))
                .andExpect(jsonPath("$[1].lessonType").value("Семинар"));
    }

    @Test
    public void getScheduleEventById_WithExistingId_ShouldReturnEvent() throws Exception {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        Workload workload = new Workload();
        workload.setId(1L);
        workload.setTeacher(teacher);
        workload.setSubject("Математика");
        workload.setStudentGroup("ИС-101");

        ScheduleEvent event = new ScheduleEvent();
        event.setId(1L);
        event.setTeacher(teacher);
        event.setWorkload(workload);
        event.setSubject("Математика");
        event.setStudentGroup("ИС-101");
        event.setDate(LocalDate.of(2025, 5, 15));
        event.setStartTime(LocalTime.of(10, 0));
        event.setEndTime(LocalTime.of(11, 30));
        event.setLocation("Аудитория 305");
        event.setLessonType("Лекция");
        event.setCompleted(false);

        ScheduleEventDto eventDto = new ScheduleEventDto();
        eventDto.setId(1L);
        eventDto.setTeacherId(1L);
        eventDto.setWorkloadId(1L);
        eventDto.setSubject("Математика");
        eventDto.setStudentGroup("ИС-101");
        eventDto.setDate(LocalDate.of(2025, 5, 15));
        eventDto.setStartTime(LocalTime.of(10, 0));
        eventDto.setEndTime(LocalTime.of(11, 30));
        eventDto.setLocation("Аудитория 305");
        eventDto.setLessonType("Лекция");
        eventDto.setCompleted(false);

        // Настройка моков
        when(scheduleEventService.findById(1L)).thenReturn(Optional.of(event));
        when(scheduleEventMapper.toDto(event)).thenReturn(eventDto);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/schedule/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Математика"))
                .andExpect(jsonPath("$.location").value("Аудитория 305"))
                .andExpect(jsonPath("$.lessonType").value("Лекция"));
    }

    @Test
    public void getScheduleEventById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Настройка моков
        when(scheduleEventService.findById(anyLong())).thenReturn(Optional.empty());

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/schedule/999"))
                .andExpect(status().isNotFound());
    }
}
