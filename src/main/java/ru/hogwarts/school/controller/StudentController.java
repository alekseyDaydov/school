package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("student")
public class StudentController {
private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping()
    public Student createStudent(@RequestBody Student student){
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    public Student findStudent(@RequestParam long id){
        return studentService.findStudent(id);
    }

    @PatchMapping()
    public Student updatestudent(@RequestBody Student student){
        return studentService.updateStudent(student);
    }

    @DeleteMapping
    public Student deleteStudent(@RequestBody Student student){
        return studentService.deleteStudent(student);
    }

    @GetMapping("/filter/{age}")
    public Collection<Student> filterAge(@RequestParam  int age){
        return studentService.filterAge(age);
    }
    @GetMapping()
    public Collection<Student> getAll(){
        return studentService.getAll();
    }
}
