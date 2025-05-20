package ru.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.example.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByName(String name);
}
