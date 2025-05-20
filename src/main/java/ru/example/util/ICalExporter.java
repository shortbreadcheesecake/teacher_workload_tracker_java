package ru.example.util;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.springframework.stereotype.Component;
import ru.example.model.ScheduleEvent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ICalExporter {

    public byte[] exportToICal(List<ScheduleEvent> events) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Teacher Workload Tracker//RU"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);
        
        for (ScheduleEvent event : events) {
            if (event.getDate() == null || event.getStartTime() == null || event.getEndTime() == null) {
                continue;
            }
            
            // Создаем даты начала и окончания события
            java.util.Calendar startCal = java.util.Calendar.getInstance();
            startCal.setTime(Date.from(event.getDate().atTime(event.getStartTime())
                    .atZone(ZoneId.systemDefault()).toInstant()));
            
            java.util.Calendar endCal = java.util.Calendar.getInstance();
            endCal.setTime(Date.from(event.getDate().atTime(event.getEndTime())
                    .atZone(ZoneId.systemDefault()).toInstant()));
            
            DateTime start = new DateTime(startCal.getTime());
            DateTime end = new DateTime(endCal.getTime());
            
            // Создаем название события
            String summary = String.format("%s (%s) - %s", 
                    event.getSubject(), 
                    event.getLessonType(), 
                    event.getStudentGroup());
            
            // Создаем описание события
            String description = String.format("Преподаватель: %s\nАудитория: %s\nТип занятия: %s\nКоличество часов: %d", 
                    event.getTeacher() != null ? event.getTeacher().getName() : "Не указан",
                    event.getLocation() != null ? event.getLocation() : "Не указана",
                    event.getLessonType() != null ? event.getLessonType() : "Не указан",
                    event.getHours() != null ? event.getHours() : 0);
            
            // Создаем событие
            VEvent vEvent = new VEvent(start, end, summary);
            
            // Добавляем уникальный идентификатор
            vEvent.getProperties().add(new Uid(UUID.randomUUID().toString()));
            
            // Добавляем описание
            vEvent.getProperties().add(new Description(description));
            
            // Добавляем место проведения
            if (event.getLocation() != null && !event.getLocation().isEmpty()) {
                vEvent.getProperties().add(new Location(event.getLocation()));
            }
            
            // Добавляем событие в календарь
            calendar.getComponents().add(vEvent);
        }
        
        // Экспортируем календарь в байтовый массив
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CalendarOutputter outputter = new CalendarOutputter();
        try {
            outputter.output(calendar, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при экспорте в iCalendar: " + e.getMessage(), e);
        }
    }
}
