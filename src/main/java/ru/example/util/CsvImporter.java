package ru.example.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.example.dto.ImportResultDto;
import ru.example.model.ScheduleEvent;
import ru.example.model.Teacher;
import ru.example.model.Workload;
import ru.example.service.ScheduleEventService;
import ru.example.service.TeacherService;
import ru.example.service.WorkloadService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class CsvImporter {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private WorkloadService workloadService;

    @Autowired
    private ScheduleEventService scheduleEventService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public ImportResultDto importSchedule(InputStream inputStream) throws IOException {
        ImportResultDto result = new ImportResultDto();
        
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {
            
            result.setTotalImported(0);
            
            for (CSVRecord csvRecord : csvParser) {
                try {
                    result.setTotalImported(result.getTotalImported() + 1);
                    
                    // Получаем данные из CSV
                    String teacherName = csvRecord.get("ФИО преподавателя");
                    String subject = csvRecord.get("Предмет");
                    String studentGroup = csvRecord.get("Группа");
                    String lessonType = csvRecord.get("Тип занятия");
                    String dateStr = csvRecord.get("Дата");
                    String startTimeStr = csvRecord.get("Время начала");
                    String endTimeStr = csvRecord.get("Время окончания");
                    String location = csvRecord.get("Аудитория");
                    
                    // Преобразуем строки в объекты
                    LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);
                    LocalTime startTime = LocalTime.parse(startTimeStr, TIME_FORMATTER);
                    LocalTime endTime = LocalTime.parse(endTimeStr, TIME_FORMATTER);
                    
                    // Находим или создаем преподавателя
                    Teacher teacher = teacherService.findByName(teacherName);
                    if (teacher == null) {
                        teacher = new Teacher();
                        teacher.setName(teacherName);
                        teacher = teacherService.save(teacher);
                    }
                    
                    // Находим подходящую нагрузку или создаем новую
                    Workload workload = workloadService.findByTeacherAndSubjectAndStudentGroup(
                            teacher.getId(), subject, studentGroup);
                    
                    if (workload == null) {
                        workload = new Workload();
                        workload.setTeacher(teacher);
                        workload.setSubject(subject);
                        workload.setStudentGroup(studentGroup);
                        workload = workloadService.save(workload);
                    }
                    
                    // Создаем событие расписания
                    ScheduleEvent event = new ScheduleEvent();
                    event.setTeacher(teacher);
                    event.setWorkload(workload);
                    event.setSubject(subject);
                    event.setStudentGroup(studentGroup);
                    event.setLessonType(lessonType);
                    event.setDate(date);
                    event.setDayOfWeek(date.getDayOfWeek());
                    event.setStartTime(startTime);
                    event.setEndTime(endTime);
                    event.setLocation(location);
                    
                    // Вычисляем количество часов
                    int hours = endTime.getHour() - startTime.getHour();
                    if (endTime.getMinute() > startTime.getMinute()) {
                        hours++;
                    }
                    event.setHours(hours);
                    
                    // Сохраняем событие
                    scheduleEventService.save(event);
                    
                    result.incrementSuccess();
                } catch (Exception e) {
                    result.addError("Ошибка в строке " + csvRecord.getRecordNumber() + ": " + e.getMessage());
                }
            }
        }
        
        return result;
    }
}
