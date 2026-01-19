package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    Map<Long, Student> students = new HashMap<>();
    private long lastId = 0;

    //    Create
    public Student createStudent(Student student) {
        student.setId(lastId++);
        students.put(student.getId(), student);
        return student;
    }

    //    read
    public Student findStudent(long id) {
        return students.get(id);
    }

    //update
    public Student updateStudent(Student student) {
        if (students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    //delete
    public Student deleteStudent(Student student) {
        students.remove(student.getId());
        return student;
    }

    public Collection<Student> filterAge(int age) {
        return students.entrySet().stream()
                .filter(element -> element.getValue().getAge() == age)
                .peek(System.out::println)
                .map(element -> findStudent(element.getKey()))
                .collect(Collectors.toList());
    }

    public Collection<Student> getAll() {
        return students.values();
    }
}
