package ru.example.controller.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.example.dto.WorkloadDto;
import ru.example.mapper.WorkloadMapper;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.service.WorkloadService;

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
public class WorkloadApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkloadService workloadService;

    @MockBean
    private WorkloadMapper workloadMapper;

    @Test
    public void getAllWorkloads_ShouldReturnListOfWorkloads() throws Exception {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        Workload workload1 = new Workload();
        workload1.setId(1L);
        workload1.setTeacher(teacher);
        workload1.setSubject("Математика");
        workload1.setStudentGroup("ИС-101");
        workload1.setTotalHours(72);

        Workload workload2 = new Workload();
        workload2.setId(2L);
        workload2.setTeacher(teacher);
        workload2.setSubject("Информатика");
        workload2.setStudentGroup("ИС-102");
        workload2.setTotalHours(54);

        List<Workload> workloads = Arrays.asList(workload1, workload2);

        WorkloadDto workloadDto1 = new WorkloadDto();
        workloadDto1.setId(1L);
        workloadDto1.setTeacherId(1L);
        workloadDto1.setSubject("Математика");
        workloadDto1.setStudentGroup("ИС-101");
        workloadDto1.setTotalHours(72);

        WorkloadDto workloadDto2 = new WorkloadDto();
        workloadDto2.setId(2L);
        workloadDto2.setTeacherId(1L);
        workloadDto2.setSubject("Информатика");
        workloadDto2.setStudentGroup("ИС-102");
        workloadDto2.setTotalHours(54);

        // Настройка моков
        when(workloadService.findAll()).thenReturn(workloads);
        when(workloadMapper.toDto(workload1)).thenReturn(workloadDto1);
        when(workloadMapper.toDto(workload2)).thenReturn(workloadDto2);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/workloads"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subject").value("Математика"))
                .andExpect(jsonPath("$[0].studentGroup").value("ИС-101"))
                .andExpect(jsonPath("$[0].totalHours").value(72))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].subject").value("Информатика"))
                .andExpect(jsonPath("$[1].studentGroup").value("ИС-102"))
                .andExpect(jsonPath("$[1].totalHours").value(54));
    }

    @Test
    public void getWorkloadById_WithExistingId_ShouldReturnWorkload() throws Exception {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        Workload workload = new Workload();
        workload.setId(1L);
        workload.setTeacher(teacher);
        workload.setSubject("Математика");
        workload.setStudentGroup("ИС-101");
        workload.setTotalHours(72);

        WorkloadDto workloadDto = new WorkloadDto();
        workloadDto.setId(1L);
        workloadDto.setTeacherId(1L);
        workloadDto.setSubject("Математика");
        workloadDto.setStudentGroup("ИС-101");
        workloadDto.setTotalHours(72);

        // Настройка моков
        when(workloadService.findById(1L)).thenReturn(Optional.of(workload));
        when(workloadMapper.toDto(workload)).thenReturn(workloadDto);

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/workloads/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Математика"))
                .andExpect(jsonPath("$.studentGroup").value("ИС-101"))
                .andExpect(jsonPath("$.totalHours").value(72));
    }

    @Test
    public void getWorkloadById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Настройка моков
        when(workloadService.findById(anyLong())).thenReturn(Optional.empty());

        // Выполнение запроса и проверка результата
        mockMvc.perform(get("/api/workloads/999"))
                .andExpect(status().isNotFound());
    }
}
