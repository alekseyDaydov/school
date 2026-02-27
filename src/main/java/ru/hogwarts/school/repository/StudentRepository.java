package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(int max, int min);

    Optional<Student> findFacultyById(int id);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Long getCountStudent();

    @Query(value = "SELECT AVG(age) From student",nativeQuery = true)
    Float getAverageAgeStudent();

    @Query(value = "SELECT * FROM student  ORDER BY id desc LIMIT 5", nativeQuery = true)
    List<Student> getLastFiveStudent();
}
