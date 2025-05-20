package ru.example.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;
import ru.example.model.ScheduleEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CsvExporter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static final String[] CSV_HEADERS = {
            "ФИО преподавателя", "Предмет", "Группа", "Тип занятия", "Дата", 
            "День недели", "Время начала", "Время окончания", "Аудитория", "Часы"
    };

    public byte[] exportToCsv(List<ScheduleEvent> events) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try (OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader(CSV_HEADERS))) {
            
            for (ScheduleEvent event : events) {
                csvPrinter.printRecord(
                        event.getTeacher() != null ? event.getTeacher().getName() : "",
                        event.getSubject(),
                        event.getStudentGroup(),
                        event.getLessonType(),
                        event.getDate() != null ? event.getDate().format(DATE_FORMATTER) : "",
                        event.getRussianDayOfWeek(),
                        event.getStartTime() != null ? event.getStartTime().format(TIME_FORMATTER) : "",
                        event.getEndTime() != null ? event.getEndTime().format(TIME_FORMATTER) : "",
                        event.getLocation(),
                        event.getHours()
                );
            }
            
            csvPrinter.flush();
            return out.toByteArray();
            
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте в CSV: " + e.getMessage(), e);
        }
    }
}
