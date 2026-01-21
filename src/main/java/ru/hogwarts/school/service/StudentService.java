package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //    Create
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    //    read
    public Student findByStudent(long id) {
        return studentRepository.findById(id).orElse(null);
    }

    //update
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    //delete
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> filterAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> findByAgeBetween(int min, int max){
        return studentRepository.findByAgeBetween(min, max);
    }

    public Student getById(int id){
        return studentRepository.findById(id).get();
    }
}
