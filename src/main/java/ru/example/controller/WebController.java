package ru.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.service.TeacherService;
import ru.example.service.WorkloadService;
import ru.example.service.ScheduleEventService;
import java.util.List;

@Controller
public class WebController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private WorkloadService workloadService;
    @Autowired
    private ScheduleEventService scheduleEventService;

    @GetMapping("/")
    public String home() {
        return "redirect:/teachers/list";
    }

    @GetMapping("/teachers/list")
    public String teachers(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        return "teachers";
    }

    @GetMapping("/teachers/new")
    public String newTeacher(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teacher-form";
    }

    @PostMapping("/teachers/create")
    public String createTeacher(@ModelAttribute Teacher teacher) {
        teacherService.save(teacher);
        return "redirect:/teachers/list";
    }

    @GetMapping("/teachers/{id}/profile")
    public String teacherProfile(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.findById(id).orElseThrow();
        model.addAttribute("teacher", teacher);
        return "teacher-profile";
    }

    @GetMapping("/teachers/{id}/workloads")
    public String teacherWorkloads(@PathVariable Long id, Model model) {
        Teacher teacher = teacherService.findById(id).orElseThrow();
        model.addAttribute("teacher", teacher);
        model.addAttribute("workloads", workloadService.findAllByTeacherId(id));
        return "teacher_workloads";
    }

    @GetMapping("/teachers/{id}/workloads/new")
    public String newWorkload(@PathVariable Long id, Model model) {
        model.addAttribute("teacher", teacherService.findById(id).orElseThrow());
        model.addAttribute("workload", new Workload());
        return "workload-form";
    }

    @PostMapping("/teachers/{id}/workloads/create")
    public String createWorkload(@PathVariable Long id, @ModelAttribute Workload workload) {
        Teacher teacher = teacherService.findById(id).orElseThrow();
        workload.setTeacher(teacher);
        
        // Инициализация нулевых значений для часов
        if (workload.getLectureHours() == null) workload.setLectureHours(0);
        if (workload.getSeminarHours() == null) workload.setSeminarHours(0);
        if (workload.getLabHours() == null) workload.setLabHours(0);
        if (workload.getCourseWorkHours() == null) workload.setCourseWorkHours(0);
        if (workload.getExamHours() == null) workload.setExamHours(0);
        
        // Обновление общего количества часов
        int total = workload.getLectureHours() + workload.getSeminarHours() + workload.getLabHours() + 
                   workload.getCourseWorkHours() + workload.getExamHours();
        workload.setTotalHours(total);
        
        workloadService.save(workload);
        return "redirect:/teachers/" + id + "/workloads";
    }

    @GetMapping("/teachers/{id}/workloads/{workloadId}/edit")
    public String editWorkload(@PathVariable Long id, @PathVariable Long workloadId, Model model) {
        Teacher teacher = teacherService.findById(id).orElseThrow();
        Workload workload = workloadService.findById(workloadId).orElseThrow();
        if (!workload.getTeacher().getId().equals(id)) {
            return "redirect:/teachers/" + id + "/workloads";
        }
        model.addAttribute("teacher", teacher);
        model.addAttribute("workload", workload);
        return "workload-form";
    }

    @PostMapping("/teachers/{id}/workloads/{workloadId}")
    public String updateWorkload(@PathVariable Long id, @PathVariable Long workloadId, @ModelAttribute Workload workload) {
        // Проверяем существование преподавателя
        teacherService.findById(id).orElseThrow();
        Workload existingWorkload = workloadService.findById(workloadId).orElseThrow();
        if (!existingWorkload.getTeacher().getId().equals(id)) {
            return "redirect:/teachers/" + id + "/workloads";
        }
        
        // Обновляем все поля
        existingWorkload.setPosition(workload.getPosition());
        existingWorkload.setSubject(workload.getSubject());
        existingWorkload.setLectureHours(workload.getLectureHours() != null ? workload.getLectureHours() : 0);
        existingWorkload.setSeminarHours(workload.getSeminarHours() != null ? workload.getSeminarHours() : 0);
        existingWorkload.setLabHours(workload.getLabHours() != null ? workload.getLabHours() : 0);
        existingWorkload.setCourseWorkHours(workload.getCourseWorkHours() != null ? workload.getCourseWorkHours() : 0);
        existingWorkload.setExamHours(workload.getExamHours() != null ? workload.getExamHours() : 0);
        existingWorkload.setAdditionalDuties(workload.getAdditionalDuties());
        existingWorkload.setSemester(workload.getSemester());
        existingWorkload.setAcademicYear(workload.getAcademicYear());
        existingWorkload.setStudentGroup(workload.getStudentGroup());
        
        // Обновление общего количества часов
        int total = existingWorkload.getLectureHours() + existingWorkload.getSeminarHours() + 
                   existingWorkload.getLabHours() + existingWorkload.getCourseWorkHours() + 
                   existingWorkload.getExamHours();
        existingWorkload.setTotalHours(total);
        
        workloadService.save(existingWorkload);
        return "redirect:/teachers/" + id + "/workloads";
    }

    @PostMapping("/teachers/{id}/workloads/{workloadId}/delete")
    @Transactional
    public String deleteWorkload(@PathVariable Long id, @PathVariable Long workloadId) {
        try {
            // Проверяем существование преподавателя
            teacherService.findById(id).orElseThrow(() -> new RuntimeException("Преподаватель не найден"));
            
            // Проверяем существование нагрузки
            Workload workload = workloadService.findById(workloadId).orElseThrow();
            
            // Проверяем, принадлежит ли нагрузка данному преподавателю
            if (!workload.getTeacher().getId().equals(id)) {
                return "redirect:/teachers/" + id + "/workloads";
            }
            
            // Удаляем связанные события расписания, если они есть
            scheduleEventService.deleteAllByWorkloadId(workloadId);
            
            // Удаляем нагрузку
            workloadService.deleteById(workloadId);
            
            return "redirect:/teachers/" + id + "/workloads";
        } catch (Exception e) {
            // Логирование ошибки
            System.err.println("Ошибка при удалении нагрузки: " + e.getMessage());
            return "redirect:/teachers/" + id + "/workloads";
        }
    }
    
    @PostMapping("/teachers/{id}/delete")
    public String deleteTeacher(@PathVariable Long id) {
        try {
            // Проверяем, существует ли преподаватель
            Teacher teacher = teacherService.findById(id).orElseThrow();
            
            // Проверяем, есть ли у преподавателя нагрузки
            List<Workload> workloads = workloadService.findAllByTeacherId(id);
            if (!workloads.isEmpty()) {
                // Удаляем все нагрузки преподавателя
                for (Workload workload : workloads) {
                    workloadService.delete(workload);
                }
            }
            
            // Удаляем преподавателя
            teacherService.deleteById(id);
            
            return "redirect:/teachers/list";
        } catch (Exception e) {
            // В случае ошибки возвращаемся на страницу со списком преподавателей
            return "redirect:/teachers/list";
        }
    }
}