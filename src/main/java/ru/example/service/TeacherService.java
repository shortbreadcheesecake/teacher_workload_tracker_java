package ru.example.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.example.repository.TeacherRepository;
import ru.example.model.Teacher;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher save(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }
    
    public Teacher findByName(String name) {
        return teacherRepository.findByName(name);
    }

    public void deleteById(Long id) {
        teacherRepository.deleteById(id);
    }
}
