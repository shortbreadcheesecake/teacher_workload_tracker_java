package ru.example.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.example.repository.WorkloadRepository;
import ru.example.model.Workload;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkloadService {
    @Autowired
    private WorkloadRepository workloadRepository;

    public Workload save(Workload workload) {
        return workloadRepository.save(workload);
    }

    public List<Workload> findAll() {
        return workloadRepository.findAll();
    }

    public List<Workload> findAllByTeacherId(Long teacherId) {
        return workloadRepository.findAllByTeacherId(teacherId);
    }

    public Optional<Workload> findById(Long id) {
        return workloadRepository.findById(id);
    }

    public void delete(Workload workload) {
        workloadRepository.delete(workload);
    }

    public void deleteById(Long id) {
        workloadRepository.deleteById(id);
    }
    
    public Workload findByTeacherAndSubjectAndStudentGroup(Long teacherId, String subject, String studentGroup) {
        return workloadRepository.findByTeacherIdAndSubjectAndStudentGroup(teacherId, subject, studentGroup);
    }
}
