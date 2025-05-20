package ru.example.dto;

public class WorkloadDto {
    private Long id;
    private Long teacherId;
    private String position;
    private String subject;
    private Integer lectureHours;
    private Integer seminarHours;
    private Integer labHours;
    private Integer courseWorkHours;
    private Integer examHours;
    private Integer totalHours;
    private String additionalDuties;
    private String semester;
    private String academicYear;
    private String studentGroup;
    
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
    }
    
    public Integer getSeminarHours() {
        return seminarHours;
    }
    
    public void setSeminarHours(Integer seminarHours) {
        this.seminarHours = seminarHours;
    }
    
    public Integer getLabHours() {
        return labHours;
    }
    
    public void setLabHours(Integer labHours) {
        this.labHours = labHours;
    }
    
    public Integer getCourseWorkHours() {
        return courseWorkHours;
    }
    
    public void setCourseWorkHours(Integer courseWorkHours) {
        this.courseWorkHours = courseWorkHours;
    }
    
    public Integer getExamHours() {
        return examHours;
    }
    
    public void setExamHours(Integer examHours) {
        this.examHours = examHours;
    }
    
    public Integer getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
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
}
