package ru.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.repository.WorkloadRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WorkloadServiceTest {

    @Mock
    private WorkloadRepository workloadRepository;

    @InjectMocks
    private WorkloadService workloadService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAll_ShouldReturnAllWorkloads() {
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

        List<Workload> expectedWorkloads = Arrays.asList(workload1, workload2);

        // Настройка моков
        when(workloadRepository.findAll()).thenReturn(expectedWorkloads);

        // Выполнение метода
        List<Workload> actualWorkloads = workloadService.findAll();

        // Проверка результата
        assertEquals(expectedWorkloads.size(), actualWorkloads.size());
        assertEquals(expectedWorkloads.get(0).getId(), actualWorkloads.get(0).getId());
        assertEquals(expectedWorkloads.get(0).getSubject(), actualWorkloads.get(0).getSubject());
        assertEquals(expectedWorkloads.get(1).getId(), actualWorkloads.get(1).getId());
        assertEquals(expectedWorkloads.get(1).getSubject(), actualWorkloads.get(1).getSubject());

        // Проверка вызова метода репозитория
        verify(workloadRepository, times(1)).findAll();
    }

    @Test
    public void findById_WithExistingId_ShouldReturnWorkload() {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        Workload expectedWorkload = new Workload();
        expectedWorkload.setId(1L);
        expectedWorkload.setTeacher(teacher);
        expectedWorkload.setSubject("Математика");
        expectedWorkload.setStudentGroup("ИС-101");
        expectedWorkload.setTotalHours(72);

        // Настройка моков
        when(workloadRepository.findById(1L)).thenReturn(Optional.of(expectedWorkload));

        // Выполнение метода
        Optional<Workload> actualWorkload = workloadService.findById(1L);

        // Проверка результата
        assertTrue(actualWorkload.isPresent());
        assertEquals(expectedWorkload.getId(), actualWorkload.get().getId());
        assertEquals(expectedWorkload.getSubject(), actualWorkload.get().getSubject());
        assertEquals(expectedWorkload.getStudentGroup(), actualWorkload.get().getStudentGroup());
        assertEquals(expectedWorkload.getTotalHours(), actualWorkload.get().getTotalHours());

        // Проверка вызова метода репозитория
        verify(workloadRepository, times(1)).findById(1L);
    }

    @Test
    public void findById_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Настройка моков
        when(workloadRepository.findById(999L)).thenReturn(Optional.empty());

        // Выполнение метода
        Optional<Workload> actualWorkload = workloadService.findById(999L);

        // Проверка результата
        assertFalse(actualWorkload.isPresent());

        // Проверка вызова метода репозитория
        verify(workloadRepository, times(1)).findById(999L);
    }

    @Test
    public void save_ShouldSaveWorkload() {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        Workload workloadToSave = new Workload();
        workloadToSave.setTeacher(teacher);
        workloadToSave.setSubject("Математика");
        workloadToSave.setStudentGroup("ИС-101");
        workloadToSave.setTotalHours(72);

        Workload savedWorkload = new Workload();
        savedWorkload.setId(1L);
        savedWorkload.setTeacher(teacher);
        savedWorkload.setSubject("Математика");
        savedWorkload.setStudentGroup("ИС-101");
        savedWorkload.setTotalHours(72);

        // Настройка моков
        when(workloadRepository.save(any(Workload.class))).thenReturn(savedWorkload);

        // Выполнение метода
        Workload actualWorkload = workloadService.save(workloadToSave);

        // Проверка результата
        assertNotNull(actualWorkload);
        assertEquals(savedWorkload.getId(), actualWorkload.getId());
        assertEquals(savedWorkload.getSubject(), actualWorkload.getSubject());
        assertEquals(savedWorkload.getStudentGroup(), actualWorkload.getStudentGroup());
        assertEquals(savedWorkload.getTotalHours(), actualWorkload.getTotalHours());

        // Проверка вызова метода репозитория
        verify(workloadRepository, times(1)).save(workloadToSave);
    }

    @Test
    public void delete_ShouldDeleteWorkload() {
        // Подготовка данных
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Иванов И.И.");

        Workload workloadToDelete = new Workload();
        workloadToDelete.setId(1L);
        workloadToDelete.setTeacher(teacher);
        workloadToDelete.setSubject("Математика");
        workloadToDelete.setStudentGroup("ИС-101");
        workloadToDelete.setTotalHours(72);

        // Выполнение метода
        workloadService.delete(workloadToDelete);

        // Проверка вызова метода репозитория
        verify(workloadRepository, times(1)).delete(workloadToDelete);
    }

    @Test
    public void deleteById_ShouldDeleteWorkloadById() {
        // Выполнение метода
        workloadService.deleteById(1L);

        // Проверка вызова метода репозитория
        verify(workloadRepository, times(1)).deleteById(1L);
    }

    @Test
    public void findAllByTeacherId_ShouldReturnWorkloadsForTeacher() {
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

        List<Workload> expectedWorkloads = Arrays.asList(workload1, workload2);

        // Настройка моков
        when(workloadRepository.findAllByTeacherId(1L)).thenReturn(expectedWorkloads);

        // Выполнение метода
        List<Workload> actualWorkloads = workloadService.findAllByTeacherId(1L);

        // Проверка результата
        assertEquals(expectedWorkloads.size(), actualWorkloads.size());
        assertEquals(expectedWorkloads.get(0).getId(), actualWorkloads.get(0).getId());
        assertEquals(expectedWorkloads.get(0).getSubject(), actualWorkloads.get(0).getSubject());
        assertEquals(expectedWorkloads.get(1).getId(), actualWorkloads.get(1).getId());
        assertEquals(expectedWorkloads.get(1).getSubject(), actualWorkloads.get(1).getSubject());

        // Проверка вызова метода репозитория
        verify(workloadRepository, times(1)).findAllByTeacherId(1L);
    }
}
