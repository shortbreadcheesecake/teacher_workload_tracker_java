package ru.example.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Workload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String position; // Должность
    private String subject; // Предметы
    private Integer lectureHours; // Часы лекций
    private Integer seminarHours; // Часы семинаров
    private Integer labHours; // Часы лабораторных работ
    private Integer courseWorkHours; // Часы курсовых работ
    private Integer examHours; // Часы экзаменов
    private Integer totalHours; // Общая нагрузка
    private String additionalDuties; // Дополнительные обязанности
    private String semester; // Семестр
    private String academicYear; // Учебный год
    private String studentGroup; // Учебная группа
    
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getLectureHours() {
        return lectureHours;
    }

    public void setLectureHours(Integer lectureHours) {
        this.lectureHours = lectureHours;
        updateTotalHours();
    }

    public Integer getSeminarHours() {
        return seminarHours;
    }

    public void setSeminarHours(Integer seminarHours) {
        this.seminarHours = seminarHours;
        updateTotalHours();
    }

    public Integer getLabHours() {
        return labHours;
    }

    public void setLabHours(Integer labHours) {
        this.labHours = labHours;
        updateTotalHours();
    }

    public Integer getCourseWorkHours() {
        return courseWorkHours;
    }

    public void setCourseWorkHours(Integer courseWorkHours) {
        this.courseWorkHours = courseWorkHours;
        updateTotalHours();
    }

    public Integer getExamHours() {
        return examHours;
    }

    public void setExamHours(Integer examHours) {
        this.examHours = examHours;
        updateTotalHours();
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
    }
    
    private void updateTotalHours() {
        int total = 0;
        if (lectureHours != null) total += lectureHours;
        if (seminarHours != null) total += seminarHours;
        if (labHours != null) total += labHours;
        if (courseWorkHours != null) total += courseWorkHours;
        if (examHours != null) total += examHours;
        this.totalHours = total;
    }

    public String getAdditionalDuties() {
        return additionalDuties;
    }

    public void setAdditionalDuties(String additionalDuties) {
        this.additionalDuties = additionalDuties;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getStudentGroup() {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup) {
        this.studentGroup = studentGroup;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    
    // Метод для уменьшения часов определенного типа
    public void decreaseHours(String lessonType, int hours) {
        switch (lessonType) {
            case "Лекция":
                if (lectureHours != null) {
                    lectureHours = Math.max(0, lectureHours - hours);
                }
                break;
            case "Семинар":
                if (seminarHours != null) {
                    seminarHours = Math.max(0, seminarHours - hours);
                }
                break;
            case "Лабораторная работа":
                if (labHours != null) {
                    labHours = Math.max(0, labHours - hours);
                }
                break;
            case "Курсовая работа":
                if (courseWorkHours != null) {
                    courseWorkHours = Math.max(0, courseWorkHours - hours);
                }
                break;
            case "Экзамен":
                if (examHours != null) {
                    examHours = Math.max(0, examHours - hours);
                }
                break;
        }
        updateTotalHours();
    }
}