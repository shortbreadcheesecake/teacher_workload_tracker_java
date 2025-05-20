package ru.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.example.model.Teacher;
import ru.example.service.TeacherService;
import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }

    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    public Teacher getTeacher(@PathVariable Long id) {
        return teacherService.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.deleteById(id);
    }
}
