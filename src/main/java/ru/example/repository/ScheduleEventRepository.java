package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.model.ScheduleEvent;
import java.util.List;

public interface ScheduleEventRepository extends JpaRepository<ScheduleEvent, Long> {
    List<ScheduleEvent> findAllByTeacherId(Long teacherId);
    List<ScheduleEvent> findAllByWorkloadId(Long workloadId);
}