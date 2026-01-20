package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColor(String color);
//    List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String find);
    List<Faculty> findFacultyByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String findText, String find );
}
