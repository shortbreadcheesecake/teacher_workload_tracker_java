package ru.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.model.ScheduleEvent;
import ru.example.model.Workload;
import ru.example.repository.ScheduleEventRepository;
import ru.example.service.WorkloadService;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleEventService {

    @Autowired
    private ScheduleEventRepository scheduleEventRepository;
    
    @Autowired
    private WorkloadService workloadService;

    public List<ScheduleEvent> findAll() {
        return scheduleEventRepository.findAll();
    }

    public Optional<ScheduleEvent> findById(Long id) {
        return scheduleEventRepository.findById(id);
    }

    public List<ScheduleEvent> findAllByTeacherId(Long teacherId) {
        return scheduleEventRepository.findAllByTeacherId(teacherId);
    }
    
    public List<ScheduleEvent> findAllByWorkloadId(Long workloadId) {
        return scheduleEventRepository.findAllByWorkloadId(workloadId);
    }

    public ScheduleEvent save(ScheduleEvent scheduleEvent) {
        return scheduleEventRepository.save(scheduleEvent);
    }

    public void delete(ScheduleEvent scheduleEvent) {
        scheduleEventRepository.delete(scheduleEvent);
    }
    
    public void deleteById(Long id) {
        scheduleEventRepository.deleteById(id);
    }
    
    @Transactional
    public void markAsCompleted(Long eventId) {
        ScheduleEvent event = findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        
        // Сохраняем информацию о занятии перед удалением
        Workload workload = event.getWorkload();
        Integer hours = event.getHours();
        String lessonType = event.getLessonType();
        
        // Уменьшаем соответствующие часы в нагрузке
        if (workload != null && hours != null) {
            workload.decreaseHours(lessonType, hours);
            workloadService.save(workload);
            
            // Если общая нагрузка стала 0, удаляем нагрузку
            if (workload.getTotalHours() != null && workload.getTotalHours() == 0) {
                workloadService.delete(workload);
            }
        }
        
        // Удаляем занятие из расписания
        delete(event);
    }
    
    @Transactional
    public void markAsCancelled(Long eventId) {
        ScheduleEvent event = findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        // Просто удаляем событие без уменьшения часов
        delete(event);
    }
    
    /**
     * Удаляет все события расписания, связанные с указанной нагрузкой
     * @param workloadId идентификатор нагрузки
     */
    @Transactional
    public void deleteAllByWorkloadId(Long workloadId) {
        List<ScheduleEvent> events = findAllByWorkloadId(workloadId);
        for (ScheduleEvent event : events) {
            delete(event);
        }
    }
}