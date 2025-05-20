package ru.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.example.model.ScheduleEvent;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.service.ScheduleEventService;
import ru.example.service.TeacherService;
import ru.example.service.WorkloadService;

import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleEventService scheduleEventService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private WorkloadService workloadService;

    @GetMapping
    public String redirectToHome() {
        // Перенаправляем на главную страницу
        return "redirect:/";
    }

    @GetMapping("/teacher/{teacherId}")
    public String showTeacherSchedule(@PathVariable Long teacherId, Model model) {
        Teacher teacher = teacherService.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + teacherId));
        List<ScheduleEvent> events = scheduleEventService.findAllByTeacherId(teacherId);
        model.addAttribute("teacher", teacher);
        model.addAttribute("events", events);
        return "teacher-schedule";
    }

    @GetMapping("/new")
    public String newScheduleEvent(@RequestParam(required = false) Long teacherId, 
                                  @RequestParam(required = false) Long workloadId,
                                  Model model) {
        ScheduleEvent event = new ScheduleEvent();
        
        // Если указан ID преподавателя, предварительно выбираем его
        if (teacherId != null) {
            Teacher teacher = teacherService.findById(teacherId).orElse(null);
            event.setTeacher(teacher);
            // Если преподаватель найден, показываем только его нагрузки
            if (teacher != null) {
                model.addAttribute("workloads", workloadService.findAllByTeacherId(teacherId));
            } else {
                model.addAttribute("workloads", workloadService.findAll());
            }
        } else {
            model.addAttribute("workloads", workloadService.findAll());
        }
        
        // Если указан ID нагрузки, предварительно выбираем её
        if (workloadId != null) {
            Workload workload = workloadService.findById(workloadId).orElse(null);
            if (workload != null) {
                event.setWorkload(workload);
                event.setTeacher(workload.getTeacher());
                event.setSubject(workload.getSubject());
                event.setStudentGroup(workload.getStudentGroup());
            }
        }
        
        model.addAttribute("event", event);
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("lessonTypes", new String[]{"Лекция", "Семинар", "Лабораторная работа", "Курсовая работа", "Экзамен"});
        return "schedule-event-form";
    }

    @PostMapping("/create")
    public String createScheduleEvent(@ModelAttribute ScheduleEvent event) {
        // Если дата установлена, вычисляем день недели
        if (event.getDate() != null) {
            event.setDayOfWeek(event.getDate().getDayOfWeek());
        }
        
        scheduleEventService.save(event);
        
        if (event.getTeacher() != null && event.getTeacher().getId() != null) {
            return "redirect:/schedule/teacher/" + event.getTeacher().getId();
        }
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editScheduleEvent(@PathVariable Long id, Model model) {
        ScheduleEvent event = scheduleEventService.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule event not found with id: " + id));
        model.addAttribute("event", event);
        model.addAttribute("teachers", teacherService.findAll());
        
        // Если у события есть преподаватель, показываем только его нагрузки
        if (event.getTeacher() != null && event.getTeacher().getId() != null) {
            model.addAttribute("workloads", workloadService.findAllByTeacherId(event.getTeacher().getId()));
        } else {
            model.addAttribute("workloads", workloadService.findAll());
        }
        
        model.addAttribute("lessonTypes", new String[]{"Лекция", "Семинар", "Лабораторная работа", "Курсовая работа", "Экзамен"});
        return "schedule-event-form";
    }

    @PostMapping("/{id}")
    public String updateScheduleEvent(@PathVariable Long id, @ModelAttribute ScheduleEvent event) {
        try {
            ScheduleEvent existingEvent = scheduleEventService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule event not found with id: " + id));
            
            // Обновляем поля
            existingEvent.setDate(event.getDate());
            existingEvent.setStartTime(event.getStartTime());
            existingEvent.setEndTime(event.getEndTime());
            existingEvent.setSubject(event.getSubject());
            existingEvent.setStudentGroup(event.getStudentGroup());
            existingEvent.setLocation(event.getLocation());
            existingEvent.setLessonType(event.getLessonType());
            existingEvent.setHours(event.getHours());
            existingEvent.setTeacher(event.getTeacher());
            existingEvent.setWorkload(event.getWorkload());
            
            // Если дата установлена, вычисляем день недели
            if (existingEvent.getDate() != null) {
                existingEvent.setDayOfWeek(existingEvent.getDate().getDayOfWeek());
            }
            
            scheduleEventService.save(existingEvent);
            
            if (existingEvent.getTeacher() != null && existingEvent.getTeacher().getId() != null) {
                return "redirect:/schedule/teacher/" + existingEvent.getTeacher().getId();
            }
            return "redirect:/";
        } catch (Exception e) {
            // В случае ошибки возвращаемся на главную страницу
            return "redirect:/";
        }
    }

    @PostMapping("/{id}/complete")
    public String completeScheduleEvent(@PathVariable Long id) {
        try {
            ScheduleEvent event = scheduleEventService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule event not found with id: " + id));
            Long teacherId = event.getTeacher() != null ? event.getTeacher().getId() : null;
            
            scheduleEventService.markAsCompleted(id);
            
            if (teacherId != null) {
                return "redirect:/schedule/teacher/" + teacherId;
            }
            return "redirect:/";
        } catch (Exception e) {
            // В случае ошибки возвращаемся на главную страницу
            return "redirect:/";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelScheduleEvent(@PathVariable Long id) {
        try {
            ScheduleEvent event = scheduleEventService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule event not found with id: " + id));
            Long teacherId = event.getTeacher() != null ? event.getTeacher().getId() : null;
            
            scheduleEventService.markAsCancelled(id);
            
            if (teacherId != null) {
                return "redirect:/schedule/teacher/" + teacherId;
            }
            return "redirect:/";
        } catch (Exception e) {
            // В случае ошибки возвращаемся на главную страницу
            return "redirect:/";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteScheduleEvent(@PathVariable Long id) {
        try {
            ScheduleEvent event = scheduleEventService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Schedule event not found with id: " + id));
            Long teacherId = event.getTeacher() != null ? event.getTeacher().getId() : null;
            
            scheduleEventService.deleteById(id);
            
            if (teacherId != null) {
                return "redirect:/schedule/teacher/" + teacherId;
            }
            return "redirect:/";
        } catch (Exception e) {
            // В случае ошибки возвращаемся на главную страницу
            return "redirect:/";
        }
    }
}
