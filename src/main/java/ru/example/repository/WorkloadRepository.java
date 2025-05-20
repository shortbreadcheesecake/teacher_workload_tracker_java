package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.model.Workload;
import java.util.List;

public interface WorkloadRepository extends JpaRepository<Workload, Long> {
    List<Workload> findAllByTeacherId(Long teacherId);
    Workload findByTeacherIdAndSubjectAndStudentGroup(Long teacherId, String subject, String studentGroup);
}