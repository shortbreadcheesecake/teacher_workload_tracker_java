package ru.example.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleEventDto {
    private Long id;
    private Long teacherId;
    private Long workloadId;
    private LocalDate date;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String subject;
    private String studentGroup;
    private String location;
    private String lessonType;
    private Integer hours;
    private Boolean completed;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getTeacherId() {
        return teacherId;
    }
    
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    
    public Long getWorkloadId() {
        return workloadId;
    }
    
    public void setWorkloadId(Long workloadId) {
        this.workloadId = workloadId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(String dayOfWeek) {
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
