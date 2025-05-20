package ru.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.example.model.Teacher;
import ru.example.repository.TeacherRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findAll_ShouldReturnAllTeachers() {
        // Подготовка данных
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setName("Иванов И.И.");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setName("Петров П.П.");

        List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);

        // Настройка моков
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        // Выполнение метода
        List<Teacher> actualTeachers = teacherService.findAll();

        // Проверка результата
        assertEquals(expectedTeachers.size(), actualTeachers.size());
        assertEquals(expectedTeachers.get(0).getId(), actualTeachers.get(0).getId());
        assertEquals(expectedTeachers.get(0).getName(), actualTeachers.get(0).getName());
        assertEquals(expectedTeachers.get(1).getId(), actualTeachers.get(1).getId());
        assertEquals(expectedTeachers.get(1).getName(), actualTeachers.get(1).getName());

        // Проверка вызова метода репозитория
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    public void findById_WithExistingId_ShouldReturnTeacher() {
        // Подготовка данных
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setId(1L);
        expectedTeacher.setName("Иванов И.И.");

        // Настройка моков
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(expectedTeacher));

        // Выполнение метода
        Optional<Teacher> actualTeacher = teacherService.findById(1L);

        // Проверка результата
        assertTrue(actualTeacher.isPresent());
        assertEquals(expectedTeacher.getId(), actualTeacher.get().getId());
        assertEquals(expectedTeacher.getName(), actualTeacher.get().getName());

        // Проверка вызова метода репозитория
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    public void findById_WithNonExistingId_ShouldReturnEmptyOptional() {
        // Настройка моков
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        // Выполнение метода
        Optional<Teacher> actualTeacher = teacherService.findById(999L);

        // Проверка результата
        assertFalse(actualTeacher.isPresent());

        // Проверка вызова метода репозитория
        verify(teacherRepository, times(1)).findById(999L);
    }

    @Test
    public void save_ShouldSaveTeacher() {
        // Подготовка данных
        Teacher teacherToSave = new Teacher();
        teacherToSave.setName("Иванов И.И.");

        Teacher savedTeacher = new Teacher();
        savedTeacher.setId(1L);
        savedTeacher.setName("Иванов И.И.");

        // Настройка моков
        when(teacherRepository.save(any(Teacher.class))).thenReturn(savedTeacher);

        // Выполнение метода
        Teacher actualTeacher = teacherService.save(teacherToSave);

        // Проверка результата
        assertNotNull(actualTeacher);
        assertEquals(savedTeacher.getId(), actualTeacher.getId());
        assertEquals(savedTeacher.getName(), actualTeacher.getName());

        // Проверка вызова метода репозитория
        verify(teacherRepository, times(1)).save(teacherToSave);
    }

    @Test
    public void deleteById_ShouldDeleteTeacherById() {
        // Подготовка данных
        Long teacherId = 1L;

        // Выполнение метода
        teacherService.deleteById(teacherId);

        // Проверка вызова метода репозитория
        verify(teacherRepository, times(1)).deleteById(teacherId);
    }

    // Этот метод удален, так как он дублирует предыдущий
}
