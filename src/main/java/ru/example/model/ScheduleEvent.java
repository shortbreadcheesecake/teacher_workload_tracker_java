package ru.example.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DayOfWeek;

@Entity
public class ScheduleEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    @ManyToOne
    @JoinColumn(name = "workload_id")
    private Workload workload;

    private LocalDate date; // Дата
    private DayOfWeek dayOfWeek; // День недели
    private LocalTime startTime; // Время начала
    private LocalTime endTime; // Время окончания
    private String subject; // Предмет
    private String studentGroup; // Группа
    private String location; // Аудитория
    private String lessonType; // Тип занятия
    private Integer hours; // Количество часов
    private Boolean completed; // Выполнено или нет
    
    // Метод для получения дня недели на русском языке
    public String getRussianDayOfWeek() {
        if (dayOfWeek == null) return "";
        
        switch (dayOfWeek) {
            case MONDAY: return "Понедельник";
            case TUESDAY: return "Вторник";
            case WEDNESDAY: return "Среда";
            case THURSDAY: return "Четверг";
            case FRIDAY: return "Пятница";
            case SATURDAY: return "Суббота";
            case SUNDAY: return "Воскресенье";
            default: return dayOfWeek.toString();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
    public Workload getWorkload() {
        return workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        if (date != null) {
            this.dayOfWeek = date.getDayOfWeek();
        }
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}