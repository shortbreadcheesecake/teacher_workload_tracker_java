package ru.example.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.example.dto.ImportResultDto;
import ru.example.model.ScheduleEvent;
import ru.example.model.Teacher;
import ru.example.service.ScheduleEventService;
import ru.example.service.TeacherService;
import ru.example.util.CsvExporter;
import ru.example.util.CsvImporter;
import ru.example.util.ICalExporter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleImportExportApiController {
    
    @Autowired
    private ScheduleEventService scheduleEventService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CsvImporter csvImporter;
    
    @Autowired
    private CsvExporter csvExporter;
    
    @Autowired
    private ICalExporter iCalExporter;
    
    @PostMapping("/import/csv")
    public ResponseEntity<ImportResultDto> importScheduleFromCsv(@RequestParam("file") MultipartFile file) {
        try {
            ImportResultDto result = csvImporter.importSchedule(file.getInputStream());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/export/csv")
    public ResponseEntity<Resource> exportScheduleToCsv(@RequestParam(required = false) Long teacherId) {
        List<ScheduleEvent> events;
        String filename;
        
        if (teacherId != null) {
            Teacher teacher = teacherService.findById(teacherId).orElse(null);
            if (teacher == null) {
                return ResponseEntity.notFound().build();
            }
            events = scheduleEventService.findAllByTeacherId(teacherId);
            filename = "schedule_" + teacher.getName().replaceAll("\\s+", "_") + ".csv";
        } else {
            events = scheduleEventService.findAll();
            filename = "schedule_all.csv";
        }
        
        byte[] csvData = csvExporter.exportToCsv(events);
        ByteArrayResource resource = new ByteArrayResource(csvData);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csvData.length)
                .body(resource);
    }
    
    @GetMapping("/export/ical")
    public ResponseEntity<Resource> exportScheduleToIcal(@RequestParam(required = false) Long teacherId) {
        List<ScheduleEvent> events;
        String filename;
        
        if (teacherId != null) {
            Teacher teacher = teacherService.findById(teacherId).orElse(null);
            if (teacher == null) {
                return ResponseEntity.notFound().build();
            }
            events = scheduleEventService.findAllByTeacherId(teacherId);
            filename = "schedule_" + teacher.getName().replaceAll("\\s+", "_") + ".ics";
        } else {
            events = scheduleEventService.findAll();
            filename = "schedule_all.ics";
        }
        
        byte[] icalData = iCalExporter.exportToICal(events);
        ByteArrayResource resource = new ByteArrayResource(icalData);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/calendar"))
                .contentLength(icalData.length)
                .body(resource);
    }
}
