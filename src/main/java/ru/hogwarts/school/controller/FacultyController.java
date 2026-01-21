package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;
    private final StudentService studentService;

    public FacultyController(FacultyService facultyService, StudentService studentService) {
        this.facultyService = facultyService;
        this.studentService = studentService;
    }
//
//    public FacultyController(FacultyService facultyService) {
//        this.facultyService = facultyService;
//    }

    @PostMapping()
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("{id}")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable(required = false) Long id,
                                                  @PathVariable(required = false) Long idStudent) {
        Faculty faculty = null;
        if (idStudent != null && idStudent.longValue() > 0) {
            Student student = studentService.getById(idStudent.intValue());
            if (student != null && student.getId() > 0) {
                faculty = facultyService.findFaculty(student.getId());
            }
        }
        if (id != null && id > 0) {
            faculty = facultyService.findFaculty(id);
        }

        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(faculty);
    }

    @PutMapping()
    public ResponseEntity<Faculty> updateFaculty(@RequestBody Faculty faculty) {
        Faculty faculty1 = facultyService.updateFaculty(faculty);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<Collection<Faculty>> filterColor(@RequestParam(required = false) String color,
                                                           @RequestParam(required = false) String find,
                                                           @RequestParam(required = false) Integer idStudent) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.filterColor(color));
        }
        if (find != null && !find.isBlank()) {
            return ResponseEntity.ok(facultyService.findByNameContainsIgnoreCaseOrColorIgnoreCase(find));
        }

        if (idStudent != null && idStudent.longValue() > 0) {
         Collection<Student> students =   facultyService.findFaculty(idStudent).getStudents();
            return ResponseEntity.ok(Collections.emptyList());
        }

        if (color == null && find == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Faculty>> getAll() {
        Collection<Faculty> faculties = facultyService.getAll();
        if (faculties.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(faculties);
    }

}
